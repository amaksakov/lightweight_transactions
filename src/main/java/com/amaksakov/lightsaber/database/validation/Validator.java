package com.amaksakov.lightsaber.database.validation;

import com.amaksakov.lightsaber.model.ChargeCommand;
import com.amaksakov.lightsaber.model.TransferCommand;

public interface Validator {
    boolean validate(TransferCommand transferOrder, long accountBalance);

    boolean validate(ChargeCommand chargeCommand, long accountBalance);
}
