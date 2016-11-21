package nl.ing.rebel.transitions;

import android.text.InputType;
import android.util.Pair;

import org.joda.money.Money;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by jorryt on 18/11/16.
 */

public class AccountTransitions extends Transitions {
    public static class Open extends Transition {
        public Integer id;
        public Money initialDeposit;
    }

    public static class Withdraw extends Transition {
        public Integer id;
        public Money amount;
    }


    public static class Deposit extends Transition {
        public Integer id;
        public Money amount;
    }

    public static class Close extends Transition {
        public Integer id;
    }
}
