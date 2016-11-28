package nl.ing.rebel.transitions;

import org.joda.money.Money;

import nl.ing.rebel.annotations.Initial;
import nl.ing.rebel.annotations.RestEndPoint;

/**
 * Created by jorryt on 21/11/16.
 */

@RestEndPoint(url = "http://10.0.2.2:8080/Transaction/{id}/")
public class TransactionTransitions extends Transitions {
    @Initial
    public static class Start extends Transition {
        public String id;
        public String from; // debitAccount;
        public String to; // creditAccount;
        public Money amount;
    }

    public static class Book extends Transition {
        public String id;
    }
}
