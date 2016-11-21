package nl.ing.rebel.transitions;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import lombok.val;

/**
 * Created by jorryt on 18/11/16.
 */

public abstract class Transitions {
    public List<Class<? extends Transition>> getTransitions() {
        val list = new LinkedList<Class<? extends Transition>>();
        val classes = getClass().getDeclaredClasses();
        for (val cl : classes) {
            if (Transition.class.equals(cl.getSuperclass()))
                list.add((Class<? extends Transition>)cl);
        }
        return list;
    }
}
