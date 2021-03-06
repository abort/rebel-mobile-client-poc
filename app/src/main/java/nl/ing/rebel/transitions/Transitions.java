package nl.ing.rebel.transitions;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import lombok.val;
import nl.ing.rebel.annotations.Final;
import nl.ing.rebel.annotations.Initial;
import nl.ing.rebel.annotations.RestEndPoint;

/**
 * Created by jorryt on 18/11/16.
 */

@RestEndPoint(url = "")
public abstract class Transitions implements Parcelable {
    // TODO: use parcel
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getClass().getName());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Transitions> CREATOR
            = new Parcelable.Creator<Transitions>() {
        public Transitions createFromParcel(Parcel in) {
            return fromParcel(in);
        }

        public Transitions[] newArray(int size) {
            return new Transitions[size];
        }
    };

    public static Transitions fromParcel(Parcel p) {
        val className = p.readString();

        try {
            return ((Class<? extends Transitions>)Transitions.class.getClassLoader().loadClass(className)).newInstance();
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

    public String getRestEndpointBase() {
        return getClass().getAnnotation(RestEndPoint.class).url();
    }

    public List<Class<? extends Transition>> getTransitions() {
        val list = new LinkedList<Class<? extends Transition>>();
        val classes = (Class<? extends Transition>[])getClass().getDeclaredClasses();

        Log.w("bla", Arrays.toString(classes));

        Arrays.sort(classes, new Comparator<Class<? extends Transition>>() {
            @Override
            public int compare(Class<? extends Transition> a, Class<? extends Transition> b) {
                boolean isInitial = a.isAnnotationPresent(Initial.class);
                boolean isFinal = b.isAnnotationPresent(Final.class);
                Log.i("bla", String.valueOf(isInitial));
                Log.i("annos", Arrays.toString(a.getAnnotations()));
                if (isInitial) {
                    if (b.isAnnotationPresent(Initial.class)) {
                        return a.getSimpleName().compareTo(b.getSimpleName());
                    }
                    return -1;
                }
                if (b.isAnnotationPresent(Initial.class)) {
                    return 1;
                }
                if (isFinal) {
                    if (b.isAnnotationPresent(Final.class)) {
                        return a.getSimpleName().compareTo(b.getSimpleName());
                    }
                    return 2;
                }
                if (b.isAnnotationPresent(Final.class)) {
                    return -1;
                }

                return a.getSimpleName().compareTo(b.getSimpleName());
            }
        });

        Log.w("bla", Arrays.toString(classes));

        for (val cl : classes) {
            if (Transition.class.equals(cl.getSuperclass()))
                list.add(cl);
        }

        return list;
    }
}
