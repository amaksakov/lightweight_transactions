package com.amaksakov.lightsaber.database.validation;

import com.amaksakov.lightsaber.model.ChargeCommand;
import com.amaksakov.lightsaber.model.TransferCommand;

public class DebitAccountValidator implements Validator {

    @Override
    public boolean validate(TransferCommand transferOrder, long accountBalance) {
        return (accountBalance - transferOrder.getAmount() >= 0);
    }

    @Override
    public boolean validate(ChargeCommand chargeCommand, long accountBalance) {
        return (accountBalance - chargeCommand.getAmount() >= 0);
    }
}
