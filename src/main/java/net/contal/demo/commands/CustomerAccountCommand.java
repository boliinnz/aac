package net.contal.demo.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomerAccountCommand {

    private String firstName;
    private String lastName;
    private double accountBalance;

    private int accountNumber;
    private double amount;
}
