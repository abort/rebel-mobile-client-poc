package nl.ing.rebel.screens;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lombok.Setter;
import lombok.val;
import nl.ing.rebel.R;
import nl.ing.rebel.transitions.Transition;

import static android.content.ContentValues.TAG;

/**
 * Created by jorryt on 18/11/16.
 */

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
        for (val p : transition.getInputFields()) {
            Log.d("BLA", "add field " + p.first);
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

    private Map<String, String> getFilledFields() {
        final Map<String, String> map = new HashMap<>();
        for (val e : textBoxes.entrySet()) {
            map.put(e.getKey(), e.getValue().getText().toString());
        }
        return map;
    }
}
