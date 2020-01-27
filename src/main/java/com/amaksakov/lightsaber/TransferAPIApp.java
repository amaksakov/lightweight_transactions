package com.amaksakov.lightsaber;

import com.amaksakov.lightsaber.api.client.AccountsApi;
import com.amaksakov.lightsaber.api.server.AccountBalanceHandler;
import com.amaksakov.lightsaber.api.server.AccountCreationHandler;
import com.amaksakov.lightsaber.api.server.BalanceChargingHandler;
import com.amaksakov.lightsaber.api.server.MoneyTransferHandler;
import com.amaksakov.lightsaber.context.ContextProvider;
import com.amaksakov.lightsaber.context.ContextProviderInterface;
import com.networknt.server.HandlerProvider;
import com.sun.net.httpserver.HttpServer;

import io.swagger.handler.PathHandlerProvider;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import jdk.nashorn.internal.parser.Lexer;

public class TransferAPIApp {


    public static void main(String[] args)  {

        ContextProviderInterface contextProvider = ContextProvider.getInstance();

        AccountBalanceHandler accountBalanceHandler = new AccountBalanceHandler(contextProvider);

        BalanceChargingHandler balanceChargingHandler = new BalanceChargingHandler(contextProvider);

        AccountCreationHandler accountCreationHandler = new AccountCreationHandler(contextProvider);

        MoneyTransferHandler moneyTransferHandler = new MoneyTransferHandler(contextProvider);

        HttpHandler sampleProvider = new HttpHandler() {
            @Override
            public void handleRequest(HttpServerExchange exchange)
                    throws Exception {
                String path = exchange.getRequestPath();
                HttpString method = exchange.getRequestMethod();

                if (path.equals("/v1/account/get_balance") && method.equals(HttpString.tryFromString("GET"))) {
                    accountBalanceHandler.handleRequest(exchange);
                } else if (path.equals("/v1/account/money_transfer") && method.equals(HttpString.tryFromString("GET"))) {
                    moneyTransferHandler.handleRequest(exchange);
                } else if (path.equals("/v1/account/charge_balance") && method.equals(HttpString.tryFromString("GET"))) {
                    balanceChargingHandler.handleRequest(exchange);
                } else if (path.equals("/v1/account/create") && method.equals(HttpString.tryFromString("GET"))) {
                    accountCreationHandler.handleRequest(exchange);
                }
                else{
                    exchange.getResponseHeaders().put(
                            Headers.CONTENT_TYPE, "text/plain");
                    exchange.getResponseSender().send("Hello Alex");
                }
            }
        };

        PathHandlerProvider handlerProvider = new PathHandlerProvider();
        Undertow server = Undertow.builder().addHttpListener(8080, "localhost")
                .setHandler(sampleProvider).build();
        server.start();
    }


}
