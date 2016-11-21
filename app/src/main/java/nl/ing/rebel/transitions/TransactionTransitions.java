package nl.ing.rebel.transitions;

import nl.ing.rebel.annotations.Initial;

/**
 * Created by jorryt on 21/11/16.
 */

public class TransactionTransitions extends Transitions {
    @Initial
    public static class New extends Transition {
        public String id;
        public String iban1;
        public String iban2;
    }
}
