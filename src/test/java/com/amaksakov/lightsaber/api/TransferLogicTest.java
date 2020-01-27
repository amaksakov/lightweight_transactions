package com.amaksakov.lightsaber.api;

import com.amaksakov.lightsaber.api.server.AccountBalanceHandler;
import com.amaksakov.lightsaber.api.server.AccountCreationHandler;
import com.amaksakov.lightsaber.api.server.BalanceChargingHandler;
import com.amaksakov.lightsaber.api.server.MoneyTransferHandler;
import com.amaksakov.lightsaber.context.ContextProvider;
import com.amaksakov.lightsaber.context.ContextProviderInterface;
import com.amaksakov.lightsaber.model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TransferLogicTest {


    ContextProviderInterface contextProvider;

    AccountBalanceHandler accountBalanceHandler;

    BalanceChargingHandler balanceChargingHandler;

    AccountCreationHandler accountCreationHandler;

    MoneyTransferHandler moneyTransferHandler;

    AccountId account1;

    AccountId account2;
    @Before
    public void init() {
        contextProvider = ContextProvider.getInstance();

        accountBalanceHandler = new AccountBalanceHandler(contextProvider);

        balanceChargingHandler = new BalanceChargingHandler(contextProvider);

        accountCreationHandler = new AccountCreationHandler(contextProvider);

        moneyTransferHandler = new MoneyTransferHandler(contextProvider);

        String user1 = "user1";
        String user2 = "user2";
        int accountNo = 1;
        AccountRequest accountRequest = new AccountRequest().userId(user1).accountOrdinal(accountNo);
        account1 = accountCreationHandler.handleLogic(accountRequest);

        accountRequest = new AccountRequest().userId(user2).accountOrdinal(accountNo);
        account2 = accountCreationHandler.handleLogic(accountRequest);
    }

    @Test
    public void testSimpleScenario() {
        // 100 euro
        long chargeAmount = 10000L;

        //70 euro
        long transferAmount = 7000L;

        long expectedRemainder = 3000L;

        balanceChargingHandler.handleLogic(new ChargeCommand().account(account1).amount(chargeAmount));

        AccountBalance accountBalance = accountBalanceHandler.handleLogic(account1);
        Assert.assertEquals(chargeAmount, accountBalance.getBalance().longValue());

        TransferCommand transferCommand = new TransferCommand().from(account1).to(account2).amount(transferAmount);
        TransferResult transferResult = moneyTransferHandler.handleLogic(transferCommand);

        Assert.assertEquals("new balance should match remainder", expectedRemainder, transferResult.getNewBalance().getBalance().longValue());

        AccountBalance newAccountBalance1 = accountBalanceHandler.handleLogic(account1);
        Assert.assertEquals(expectedRemainder, newAccountBalance1.getBalance().longValue());

        AccountBalance newAccountBalance2 = accountBalanceHandler.handleLogic(account2);
        Assert.assertEquals(transferAmount, newAccountBalance2.getBalance().longValue());


    }
}
