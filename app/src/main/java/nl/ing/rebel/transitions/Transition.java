package nl.ing.rebel.transitions;

import android.text.InputType;
import android.util.Pair;
import android.widget.EditText;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import lombok.val;

/**
 * Created by jorryt on 18/11/16.
 */

public abstract class Transition {
    public List<Pair<String, Integer>> getInputFields() {
        final List<Pair<String, Integer>> inputFields = new LinkedList<>();
        for (val f : getClass().getFields()) {
            if (!java.lang.reflect.Modifier.isStatic(f.getModifiers()))
                inputFields.add(Pair.create(f.getName(), getAndroidType(f.getType())));
        }
        return inputFields;
    }

    private Integer getAndroidType(final Class<?> fieldType) {
        if (Date.class.equals(fieldType)) return InputType.TYPE_CLASS_DATETIME;
        if (Integer.class.equals(fieldType)) return InputType.TYPE_CLASS_NUMBER;
        if (String.class.equals(fieldType)) return InputType.TYPE_CLASS_TEXT;

        return InputType.TYPE_CLASS_TEXT;
    }
}
