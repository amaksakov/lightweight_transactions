package com.amaksakov.lightsaber.api.server;

import com.amaksakov.lightsaber.context.ContextProviderInterface;
import com.amaksakov.lightsaber.database.AccountHandler;
import com.amaksakov.lightsaber.database.SynchronizedAccountHandler;
import com.amaksakov.lightsaber.model.AccountBalance;
import com.amaksakov.lightsaber.model.AccountId;
import com.amaksakov.lightsaber.model.AccountRequest;
import com.amaksakov.lightsaber.utils.ObjectMapperConfiguration;
import com.amaksakov.lightsaber.utils.UndertowRequestBodyReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public class AccountCreationHandler implements HttpHandler {
    private final ContextProviderInterface contextProvider;
    AccountHandler accountHandler;

    public AccountCreationHandler (ContextProviderInterface contextProvider) {
        this.contextProvider = contextProvider;
        accountHandler = contextProvider.getAccountHandler();
    }

    ObjectMapper objectMapper = ObjectMapperConfiguration.getObjectMapper();
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        AccountId account;
        if (exchange.getRequestMethod().equalToString("GET")) {
            String userId = exchange.getQueryParameters().get("userId").getFirst();
            String accountOrdinal = exchange.getQueryParameters().get("accountOrdinal").getFirst();
            AccountRequest accountRequest = new AccountRequest().userId(userId).accountOrdinal(Integer.valueOf(accountOrdinal));
            account = handleLogic(accountRequest);
            exchange.getResponseHeaders().put(
                    Headers.CONTENT_TYPE, "application/json");
            String payload = objectMapper.writeValueAsString(account);
            exchange.getResponseSender().send(payload);

        } else if (exchange.getRequestMethod().equalToString("POST")) {
            String requestBody = UndertowRequestBodyReader.readBody(exchange);
            AccountRequest accountRequest = objectMapper.readValue(requestBody, AccountRequest.class);
            account = handleLogic(accountRequest);
            exchange.getResponseHeaders().put(
                    Headers.CONTENT_TYPE, "application/json");
            String payload = objectMapper.writeValueAsString(account);
            exchange.getResponseSender().send(payload);

        }


    }

    public AccountId handleLogic(AccountRequest accountRequest) {
        return accountHandler.createAccount(accountRequest);
    }
}
