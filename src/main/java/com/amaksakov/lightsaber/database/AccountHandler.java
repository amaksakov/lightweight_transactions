package com.amaksakov.lightsaber.database;

import com.amaksakov.lightsaber.model.*;

public interface AccountHandler {
    AccountBalance getBalance(AccountId accountId);

    TransferResult moneyTransfer(TransferCommand transferCommand);

    AccountId createAccount(AccountRequest accountRequest);

    TransferResult chargeBalance(ChargeCommand chargeCommand);
}
