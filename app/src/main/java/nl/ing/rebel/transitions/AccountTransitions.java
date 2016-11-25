package nl.ing.rebel.transitions;

import android.text.InputType;
import android.util.Pair;

import org.joda.money.Money;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import nl.ing.rebel.annotations.Final;
import nl.ing.rebel.annotations.Initial;
import nl.ing.rebel.annotations.RestEndPoint;

@RestEndPoint(url = "http://10.0.2.2:8080/Account/{id}/")
public class AccountTransitions extends Transitions {
    @Initial
    public static class OpenAccount extends Transition {
        public String id;
        //public String IBAN;
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

    @Final
    public static class Close extends Transition {
        public Integer id;
    }
}
