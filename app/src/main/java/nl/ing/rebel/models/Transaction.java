package nl.ing.rebel.models;

import lombok.Data;

/**
 * Created by jorryt on 08/11/16.
 */

@Data
public class Transaction {
    private Integer id;
    private String ibanFrom;
    private String ibanTo;
    private String amount;
}
