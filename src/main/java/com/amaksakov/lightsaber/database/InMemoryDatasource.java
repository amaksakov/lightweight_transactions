package com.amaksakov.lightsaber.database;

import com.amaksakov.lightsaber.database.validation.Validator;
import com.amaksakov.lightsaber.model.AccountId;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryDatasource implements AccountDataSource {
    private final static Map<AccountId, AtomicLong> accounts = new HashMap<>();

    private static volatile InMemoryDatasource instance = null;

    public static InMemoryDatasource getInstance() {
        if (instance == null) {
            synchronized (InMemoryDatasource.class) {
                if (instance == null) {
                    instance = new InMemoryDatasource();
                }
            }
        }
        return instance;
    }

    private InMemoryDatasource() {

    }

    @Override
    public Long getAccountBalance(AccountId accountId) {
        AtomicLong balanceValue = accounts.get(accountId);
        return balanceValue != null ? balanceValue.get() : null;
    }

    @Override
    public Long updateAccountBalance(AccountId accountId, long value) {
        AtomicLong balanceValue = accounts.get(accountId);

        if (balanceValue == null) {
            return null;
        } else {
            return balanceValue.addAndGet(value);
        }
    }

    @Override
    public void createAccount(AccountId accountId) {
        if (!accounts.containsKey(accountId)) {
            accounts.put(accountId, new AtomicLong(0));
        }
    }
}
