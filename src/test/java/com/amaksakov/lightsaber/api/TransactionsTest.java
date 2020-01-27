package com.amaksakov.lightsaber.api;

import com.amaksakov.lightsaber.api.server.AccountBalanceHandler;
import com.amaksakov.lightsaber.api.server.AccountCreationHandler;
import com.amaksakov.lightsaber.api.server.BalanceChargingHandler;
import com.amaksakov.lightsaber.api.server.MoneyTransferHandler;
import com.amaksakov.lightsaber.api.utils.SlowValidatorContextProvider;
import com.amaksakov.lightsaber.context.ContextProvider;
import com.amaksakov.lightsaber.context.ContextProviderInterface;
import com.amaksakov.lightsaber.model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TransactionsTest {
    ContextProviderInterface contextProvider;

    AccountBalanceHandler accountBalanceHandler;

    BalanceChargingHandler balanceChargingHandler;

    AccountCreationHandler accountCreationHandler;

    MoneyTransferHandler moneyTransferHandler;

    AccountId account1;

    AccountId account2;

    AccountId account3;
    @Before
    public void init() {
        contextProvider = SlowValidatorContextProvider.getInstance();

        accountBalanceHandler = new AccountBalanceHandler(contextProvider);

        balanceChargingHandler = new BalanceChargingHandler(contextProvider);

        accountCreationHandler = new AccountCreationHandler(contextProvider);

        moneyTransferHandler = new MoneyTransferHandler(contextProvider);

        String user1 = "user1";
        String user2 = "user2";
        String user3 = "user3";
        int accountNo = 1;
        AccountRequest accountRequest = new AccountRequest().userId(user1).accountOrdinal(accountNo);
        account1 = accountCreationHandler.handleLogic(accountRequest);

        accountRequest = new AccountRequest().userId(user2).accountOrdinal(accountNo);
        account2 = accountCreationHandler.handleLogic(accountRequest);

        accountRequest = new AccountRequest().userId(user3).accountOrdinal(accountNo);
        account3 = accountCreationHandler.handleLogic(accountRequest);
    }


    @Test
    public void testSimultaneousTransferConsistency() throws InterruptedException {
        // 100 euro
        long chargeAmount = 10000L;

        //30 euro
        long transferAmount = 3000L;

        //30 euro
        long toAccount3TransferAmount = 3000L;

        long expectedRemainder = 4000L;

        long startTime = System.currentTimeMillis();

        balanceChargingHandler.handleLogic(new ChargeCommand().account(account1).amount(chargeAmount));



        AccountBalance accountBalance = accountBalanceHandler.handleLogic(account1);
        Assert.assertEquals(chargeAmount, accountBalance.getBalance().longValue());

        Thread parallelTransaction = new Thread(new Runnable() {
            @Override
            public void run() {
                TransferCommand parallelTransferCommand = new TransferCommand().from(account1).to(account3).amount(toAccount3TransferAmount);
                TransferResult transferResult = moneyTransferHandler.handleLogic(parallelTransferCommand);
            }
        });
        parallelTransaction.start();

        TransferCommand transferCommand = new TransferCommand().from(account1).to(account2).amount(transferAmount);
        TransferResult transferResult = moneyTransferHandler.handleLogic(transferCommand);

        AccountBalance newAccountBalance2 = accountBalanceHandler.handleLogic(account2);
        Assert.assertEquals(transferAmount, newAccountBalance2.getBalance().longValue());

        parallelTransaction.join();

        AccountBalance newAccountBalance1 = accountBalanceHandler.handleLogic(account1);
        Assert.assertEquals(expectedRemainder, newAccountBalance1.getBalance().longValue());

        AccountBalance newAccount3Balance = accountBalanceHandler.handleLogic(account3);
        Assert.assertEquals(toAccount3TransferAmount, newAccount3Balance.getBalance().longValue());


        long finishTime = System.currentTimeMillis();
        Assert.assertTrue("There should be a delay of 1 second or more because of slow validator", (finishTime - startTime) >= 1000);

    }
}
