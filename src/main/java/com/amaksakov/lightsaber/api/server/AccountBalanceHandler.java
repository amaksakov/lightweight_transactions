package com.amaksakov.lightsaber.api.server;

import com.amaksakov.lightsaber.context.ContextProvider;
import com.amaksakov.lightsaber.context.ContextProviderInterface;
import com.amaksakov.lightsaber.database.AccountHandler;
import com.amaksakov.lightsaber.database.SynchronizedAccountHandler;
import com.amaksakov.lightsaber.database.validation.DebitAccountValidator;
import com.amaksakov.lightsaber.database.validation.Validator;
import com.amaksakov.lightsaber.model.AccountBalance;
import com.amaksakov.lightsaber.model.AccountId;
import com.amaksakov.lightsaber.model.TransferCommand;
import com.amaksakov.lightsaber.model.TransferResult;
import com.amaksakov.lightsaber.utils.ObjectMapperConfiguration;
import com.amaksakov.lightsaber.utils.UndertowRequestBodyReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.util.ObjectMapperFactory;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public class AccountBalanceHandler implements HttpHandler {

    private final ContextProviderInterface contextProvider;
    AccountHandler accountHandler;

    public AccountBalanceHandler (ContextProviderInterface contextProvider) {
        this.contextProvider = contextProvider;
        accountHandler = contextProvider.getAccountHandler();
    }


    ObjectMapper objectMapper = ObjectMapperConfiguration.getObjectMapper();
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        AccountBalance balance;
        if (exchange.getRequestMethod().equalToString("GET")) {
            String userId = exchange.getQueryParameters().get("userId").getFirst();
            String accountIdParameter = exchange.getQueryParameters().get("accountId").getFirst();
            AccountId accountId = new AccountId().userId(userId).accountId(Long.valueOf(accountIdParameter));
            balance = handleLogic(accountId);
            exchange.getResponseHeaders().put(
                    Headers.CONTENT_TYPE, "application/json");
            String payload = objectMapper.writeValueAsString(balance);
            exchange.getResponseSender().send(payload);

        } else if (exchange.getRequestMethod().equalToString("POST")) {
            String requestBody = UndertowRequestBodyReader.readBody(exchange);
            AccountId accountId = objectMapper.readValue(requestBody, AccountId.class);
            balance = handleLogic(accountId);
            exchange.getResponseHeaders().put(
                    Headers.CONTENT_TYPE, "application/json");
            String payload = objectMapper.writeValueAsString(balance);
            exchange.getResponseSender().send(payload);
        }


    }

    public AccountBalance handleLogic(AccountId accountId) {
        return accountHandler.getBalance(accountId);
    }
}
