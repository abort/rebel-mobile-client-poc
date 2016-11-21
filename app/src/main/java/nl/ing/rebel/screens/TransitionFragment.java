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

import com.google.gson.Gson;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
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

import static android.content.ContentValues.TAG;

public class TransitionFragment extends Fragment {
    @Setter private Transition transition;
    private Gson gson = new Gson();
    private Map<String, EditText> textBoxes = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       //super.onCreateView(inflater, container, savedInstanceState);

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

        val button = new Button(getActivity());
        button.setText("Post");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("BLA", "please post: " + gson.toJson(getFilledFields()));


            }
        });
        top.addView(button);

        return top;
    }

    private Map<String, Map<String, String>> getFilledFields() {
        final Map<String, String> map = new HashMap<>();
        for (val e : textBoxes.entrySet()) {
            map.put(e.getKey(), e.getValue().getText().toString());
        }

        Map<String, Map<String, String>> wrapper = new HashMap<>();
        wrapper.put(transition.getClass().getSimpleName(), map);
        return wrapper;
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... parameters) {
            try {
                val url = "http://localhost:8080/account/" + parameters[0];
                val restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                ResponseEntity<String> accountResponse =
                        restTemplate.exchange(url, HttpMethod.POST, null, new ParameterizedTypeReference<String>() {
                        });
                return accountResponse.getBody();
            }
            catch (Exception e) {
                System.out.println("Could not request accounts");
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("log", "post execute: " + result);
        }
    }


}
