package nl.ing.rebel.screens;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import lombok.Setter;
import lombok.val;
import nl.ing.rebel.R;
import nl.ing.rebel.models.Account;
import nl.ing.rebel.transitions.Transition;
import nl.ing.rebel.transitions.Transitions;

import static android.content.ContentValues.TAG;

public class TransitionFragment extends Fragment {
    @Setter private Transition transition;
    private Gson gson = new Gson();
    private Map<String, EditText> textBoxes = new HashMap<>();
    private String restEndPoint = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        if (savedInstanceState == null) return;
        val storedTransitions = savedInstanceState.getParcelable("class");
        if (storedTransitions != null) transition = (Transition)storedTransitions;
        val baseURL = (String)savedInstanceState.get("baseURL");
        if (baseURL != null) restEndPoint = baseURL;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("class", transition);
        outState.putString("baseURL", restEndPoint);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       //super.onCreateView(inflater, container, savedInstanceState);
        val args = getArguments();
        if (args.containsKey("baseURL"))
            restEndPoint = args.getString("baseURL");

        val top = (LinearLayout)inflater.inflate(R.layout.fragment_transition, container, false);
        val text = new TextView(getActivity());
        text.setText(transition.getClass().getSimpleName() + " Transition");
        text.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        text.setTextSize(20.0f);
        text.setPadding(10, 10, 20, 40);
        text.setTypeface(Typeface.DEFAULT_BOLD);
        top.addView(text);
        for (val p : transition.getInputFields()) {
            val e = new EditText(getActivity());
            e.setHint(p.first);
            e.setInputType(p.second);
            top.addView(e);
            textBoxes.put(p.first, e);
        }

        final val endPoint = restEndPoint + transition.getClass().getSimpleName();
        val button = new Button(getActivity());
        button.setText("Post");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                val json = gson.toJson(getFilledFields());
                Log.d("BLA", "please post: " + json);
                val task = new HttpRequestTask();
                task.execute(endPoint, json, getActorId());
            }
        });
        top.addView(button);

        return top;
    }

    private Map<String, Map<String, String>> getFilledFields() {
        final Map<String, String> map = new HashMap<>();
        for (val e : textBoxes.entrySet()) {
            if ("id".equals(e.getKey())) continue;
            map.put(e.getKey(), e.getValue().getText().toString());
        }

        Map<String, Map<String, String>> wrapper = new HashMap<>();
        wrapper.put(transition.getClass().getSimpleName(), map);
        return wrapper;
    }

    public String getActorId() {
        // TODO generalize
        if (textBoxes.get("id") == null) return textBoxes.get("iban").getText().toString();
        return textBoxes.get("id").getText().toString();
    }

    private class HttpRequestTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... parameters) {
            try {
                val endPoint = parameters[0].replace("{iban}", parameters[2]).replace("{id}", parameters[2]);
                Log.i("TAG", "Executing: " + endPoint);
                val requestHeaders = new HttpHeaders();
                requestHeaders.setContentType(MediaType.APPLICATION_JSON);
                val requestEntity = new HttpEntity<String>(parameters[1], requestHeaders);

                Log.i("HTTP", "Sending json request to " + endPoint + ":\n" + requestEntity.getBody());

                val restTemplate = new RestTemplate();
                // restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                val accountResponse = restTemplate.exchange(endPoint, HttpMethod.POST, requestEntity, String.class);
                return accountResponse.getBody();
            }
            catch (Exception e) {
                System.out.println("Could not request accounts: " + e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) result = "Failed/No Response";
            Toast.makeText(getActivity(), "Post Result: " + result, Toast.LENGTH_SHORT).show();
        }
    }
}
