package com.amaksakov.lightsaber.api.server;

import com.amaksakov.lightsaber.context.ContextProviderInterface;
import com.amaksakov.lightsaber.database.AccountHandler;
import com.amaksakov.lightsaber.database.SynchronizedAccountHandler;
import com.amaksakov.lightsaber.model.*;
import com.amaksakov.lightsaber.utils.ObjectMapperConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public class BalanceChargingHandler implements HttpHandler {
    private final ContextProviderInterface contextProvider;
    AccountHandler accountHandler;

    public BalanceChargingHandler (ContextProviderInterface contextProvider) {
        this.contextProvider = contextProvider;
        accountHandler = contextProvider.getAccountHandler();
    }

    ObjectMapper objectMapper = ObjectMapperConfiguration.getObjectMapper();
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        TransferResult result;
        if (exchange.getRequestMethod().equalToString("GET")) {
            String userId = exchange.getQueryParameters().get("userId").getFirst();
            String accountIdParameter = exchange.getQueryParameters().get("accountId").getFirst();
            AccountId account = new AccountId().userId(userId).accountId(Long.valueOf(accountIdParameter));

            String amount = exchange.getQueryParameters().get("amount").getFirst();

            ChargeCommand chargeCommand = new ChargeCommand().account(account).amount(Long.valueOf(amount));

            result = handleLogic(chargeCommand);

            exchange.getResponseHeaders().put(
                    Headers.CONTENT_TYPE, "application/json");
            String payload = objectMapper.writeValueAsString(result);
            exchange.getResponseSender().send(payload);

        }
    }

    public TransferResult handleLogic(ChargeCommand chargeCommand) {
        return accountHandler.chargeBalance(chargeCommand);
    }

}
