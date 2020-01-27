package com.amaksakov.lightsaber.api.utils;

import com.amaksakov.lightsaber.database.validation.Validator;
import com.amaksakov.lightsaber.model.ChargeCommand;
import com.amaksakov.lightsaber.model.TransferCommand;

public class SlowAccountValidator implements Validator {

    @Override
    public boolean validate(TransferCommand transferOrder, long accountBalance) {
        try {
            Thread.currentThread().sleep(1000);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        return (accountBalance - transferOrder.getAmount() >= 0);
    }

    @Override
    public boolean validate(ChargeCommand chargeCommand, long accountBalance) {
        try {
            Thread.currentThread().sleep(1000);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        return (accountBalance + chargeCommand.getAmount() >= 0);
    }
}