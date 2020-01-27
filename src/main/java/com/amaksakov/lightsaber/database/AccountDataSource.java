package com.amaksakov.lightsaber.database;

import com.amaksakov.lightsaber.model.AccountId;

public interface AccountDataSource {
    Long getAccountBalance(AccountId accountId);

    Long updateAccountBalance(AccountId accountId, long value);

    void createAccount(AccountId accountId);
}
