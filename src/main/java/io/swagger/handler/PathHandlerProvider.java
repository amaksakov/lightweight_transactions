package io.swagger.handler;

import com.networknt.config.Config;
import com.networknt.server.HandlerProvider;
import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Methods;

public class PathHandlerProvider implements HandlerProvider {

    public HttpHandler getHandler() {
        HttpHandler handler = Handlers.routing()


            .add(Methods.POST, "/v1/account/charge_balance", new HttpHandler() {
                        public void handleRequest(HttpServerExchange exchange) throws Exception {
                            exchange.getResponseSender().send("createAccount");
                        }
                    })


            .add(Methods.GET, "/v1/account/charge_balance", new HttpHandler() {
                        public void handleRequest(HttpServerExchange exchange) throws Exception {
                            exchange.getResponseSender().send("accountCharge");
                        }
                    })


            .add(Methods.POST, "/v1/account/get_balance", new HttpHandler() {
                        public void handleRequest(HttpServerExchange exchange) throws Exception {
                            exchange.getResponseSender().send("getAccountBalance");
                        }
                    })


            .add(Methods.GET, "/v1/account/money_transfer", new HttpHandler() {
                        public void handleRequest(HttpServerExchange exchange) throws Exception {
                            exchange.getResponseSender().send("moneyTransfer");
                        }
                    })
                .add(Methods.GET, "/", new HttpHandler() {
                    public void handleRequest(HttpServerExchange exchange) throws Exception {
                        exchange.getResponseSender().send("hello");
                    }
                })
                .add(Methods.POST, "//get_balance", new HttpHandler() {
                    public void handleRequest(HttpServerExchange exchange) throws Exception {
                        exchange.getResponseSender().send("getAccountBalance");
                    }
                })


                ;
        return handler;
    }
}

