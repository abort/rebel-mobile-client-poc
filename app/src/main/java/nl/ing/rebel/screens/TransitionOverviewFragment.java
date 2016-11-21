package nl.ing.rebel.screens;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lombok.Setter;
import lombok.val;
import nl.ing.rebel.MainActivity;
import nl.ing.rebel.R;
import nl.ing.rebel.transitions.AccountTransitions;
import nl.ing.rebel.transitions.Transition;
import nl.ing.rebel.transitions.Transitions;

/**
 * Created by jorryt on 18/11/16.
 */

public class TransitionOverviewFragment extends Fragment {
    @Setter private Transitions transitions;
    private List<Transition> transitionList = new LinkedList<>();
    private Map<Button, Transition> buttonTransitionMap = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (transitions == null) throw new RuntimeException("Transitions not set!");
        val list = transitions.getTransitions();
        for (val t : list) {
            try {
                transitionList.add(t.newInstance());
            }
            catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        // TODO create awesome view

        final val currentFragmentManager = getActivity().getSupportFragmentManager();
        val v = (LinearLayout)inflater.inflate(R.layout.fragment_transitions, container, false);
        for (final val t : list) {
            val b = new Button(getContext());
            b.setText(t.getSimpleName());
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        val transitionFragment = new TransitionFragment();
                        transitionFragment.setTransition(t.newInstance());

                        currentFragmentManager.beginTransaction().replace(R.id.container, transitionFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
                    }
                    catch (java.lang.InstantiationException e) {
                        e.printStackTrace();
                    }
                    catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            });
            v.addView(b);
        }
        return v;
    }

}
