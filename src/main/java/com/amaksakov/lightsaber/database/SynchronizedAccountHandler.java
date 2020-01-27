package com.amaksakov.lightsaber.database;

import com.amaksakov.lightsaber.context.ContextProvider;
import com.amaksakov.lightsaber.context.ContextProviderInterface;
import com.amaksakov.lightsaber.database.validation.Validator;
import com.amaksakov.lightsaber.model.*;

import javax.naming.Context;

public class SynchronizedAccountHandler implements AccountHandler {


    private static volatile SynchronizedAccountHandler instance = null;

    private Validator validator = null;

    private AccountDataSource accountDataSource = InMemoryDatasource.getInstance();

    public static SynchronizedAccountHandler getInstance(ContextProviderInterface contextProvider) {
        Validator validator = contextProvider.getValidator();
        if (instance == null) {
            synchronized (SynchronizedAccountHandler.class) {
                if (instance == null) {
                    instance = new SynchronizedAccountHandler(validator);
                }
            }
        }
        return instance;
    }

    private SynchronizedAccountHandler(Validator validator) {
        this.validator = validator;
    }

    public Validator getValidator() {
        return validator;
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    @Override
    public synchronized AccountBalance getBalance(AccountId accountId) {
         Long balance = accountDataSource.getAccountBalance(accountId);
         if (balance != null) {
             AccountBalance accountBalance = new AccountBalance().account(accountId).balance(balance);
             return accountBalance;
         } else {
             return null;
         }
    }

    @Override
    public synchronized TransferResult moneyTransfer(TransferCommand transferCommand) {
        long amount = transferCommand.getAmount().longValue();
        if (amount > 0) {
            Long balance = accountDataSource.getAccountBalance(transferCommand.getFrom());
            if (validator.validate(transferCommand, balance)) {
                accountDataSource.updateAccountBalance(transferCommand.getFrom(), -amount);
                accountDataSource.updateAccountBalance(transferCommand.getTo(), amount);
                Long newBalance = accountDataSource.getAccountBalance(transferCommand.getFrom());

                TransferResult transferResult = new TransferResult().newBalance(new AccountBalance().balance(newBalance).account(transferCommand.getFrom()));
                return transferResult;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public synchronized AccountId createAccount(AccountRequest accountRequest) {
        AccountId accountId = new AccountId().userId(accountRequest.getUserId()).accountId((long)accountRequest.getAccountOrdinal());
        if (accountDataSource.getAccountBalance(accountId) == null) {
            //no account yet exists
            accountDataSource.createAccount(accountId);
        }
        return  accountId;

    }

    @Override
    public synchronized TransferResult chargeBalance(ChargeCommand chargeCommand) {
        long amount = chargeCommand.getAmount().longValue();
        if (amount > 0) {
            Long balance = accountDataSource.getAccountBalance(chargeCommand.getAccount());
            if (validator.validate(chargeCommand, balance)) {
                accountDataSource.updateAccountBalance(chargeCommand.getAccount(),amount);
                Long newBalance = accountDataSource.getAccountBalance(chargeCommand.getAccount());

                TransferResult transferResult = new TransferResult().newBalance(new AccountBalance().balance(newBalance).account(chargeCommand.getAccount()));
                return transferResult;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
