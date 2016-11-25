package nl.ing.rebel.transitions;

import android.os.Parcel;
import android.os.Parcelable;
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

public abstract class Transition implements Parcelable {
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getClass().getName());
    }

    public static final Parcelable.Creator<Transition> CREATOR
            = new Parcelable.Creator<Transition>() {
        public Transition createFromParcel(Parcel in) {
            return fromParcel(in);
        }

        public Transition[] newArray(int size) {
            return new Transition[size];
        }
    };

    public static Transition fromParcel(Parcel p) {
        val className = p.readString();

        try {
            return ((Class<? extends Transition>)Transition.class.getClassLoader().loadClass(className)).newInstance();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch (InstantiationException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

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
