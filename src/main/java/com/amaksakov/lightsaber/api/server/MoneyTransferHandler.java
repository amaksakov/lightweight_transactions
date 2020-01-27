package com.amaksakov.lightsaber.api.server;

import com.amaksakov.lightsaber.context.ContextProviderInterface;
import com.amaksakov.lightsaber.database.AccountHandler;
import com.amaksakov.lightsaber.database.SynchronizedAccountHandler;
import com.amaksakov.lightsaber.database.validation.DebitAccountValidator;
import com.amaksakov.lightsaber.database.validation.Validator;
import com.amaksakov.lightsaber.model.*;
import com.amaksakov.lightsaber.utils.ObjectMapperConfiguration;
import com.amaksakov.lightsaber.utils.UndertowRequestBodyReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public class MoneyTransferHandler implements HttpHandler {

    private final ContextProviderInterface contextProvider;
    AccountHandler accountHandler;

    public MoneyTransferHandler (ContextProviderInterface contextProvider) {
        this.contextProvider = contextProvider;
        accountHandler = contextProvider.getAccountHandler();
    }

    ObjectMapper objectMapper = ObjectMapperConfiguration.getObjectMapper();
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        TransferResult result;
        if (exchange.getRequestMethod().equalToString("GET")) {
            String fromUserId = exchange.getQueryParameters().get("fromUserId").getFirst();
            String fromAccountIdParameter = exchange.getQueryParameters().get("fromAccountId").getFirst();
            AccountId fromAccount = new AccountId().userId(fromUserId).accountId(Long.valueOf(fromAccountIdParameter));

            String toUserId = exchange.getQueryParameters().get("toUserId").getFirst();
            String toAccountIdParameter = exchange.getQueryParameters().get("toAccountId").getFirst();
            AccountId toAccount = new AccountId().userId(toUserId).accountId(Long.valueOf(toAccountIdParameter));

            String amount = exchange.getQueryParameters().get("amount").getFirst();

            TransferCommand transferCommand = new TransferCommand().from(fromAccount).to(toAccount).amount(Long.valueOf(amount));

            if (transferCommand.getAmount() > 0) {
                result = handleLogic(transferCommand);
            } else {
                exchange.getResponseHeaders().put(
                        Headers.CONTENT_TYPE, "application/json");
                String payload = objectMapper.writeValueAsString(new TransferError().reason(TransferError.ReasonEnum.INCORRECT_AMOUNT));
                exchange.getResponseSender().send(payload);
                return;
            }
            exchange.getResponseHeaders().put(
                    Headers.CONTENT_TYPE, "application/json");
            String payload = objectMapper.writeValueAsString(result);
            exchange.getResponseSender().send(payload);

        }  else if (exchange.getRequestMethod().equalToString("POST")) {
            String requestBody = UndertowRequestBodyReader.readBody(exchange);

            TransferCommand transferCommand = objectMapper.readValue(requestBody, TransferCommand.class);

            if (transferCommand.getAmount() > 0) {
                result = handleLogic(transferCommand);
            } else {
                exchange.getResponseHeaders().put(
                        Headers.CONTENT_TYPE, "application/json");
                String payload = objectMapper.writeValueAsString(new TransferError().reason(TransferError.ReasonEnum.INCORRECT_AMOUNT));
                exchange.getResponseSender().send(payload);
                return;
            }
            exchange.getResponseHeaders().put(
                    Headers.CONTENT_TYPE, "application/json");
            String payload = objectMapper.writeValueAsString(result);
            exchange.getResponseSender().send(payload);

        }


    }

    public TransferResult handleLogic(TransferCommand transferCommand) {
        return accountHandler.moneyTransfer(transferCommand);
    }


}
