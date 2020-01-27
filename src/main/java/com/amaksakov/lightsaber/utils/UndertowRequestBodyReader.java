package com.amaksakov.lightsaber.utils;

import io.undertow.server.HttpServerExchange;

import javax.xml.ws.spi.http.HttpExchange;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UndertowRequestBodyReader {
    public static String readBody(HttpServerExchange exchange) {
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder( );

        try {
            exchange.startBlocking( );
            reader = new BufferedReader( new InputStreamReader( exchange.getInputStream( ) ) );

            String line;
            while( ( line = reader.readLine( ) ) != null ) {
                builder.append( line );
            }
        } catch( IOException e ) {
            e.printStackTrace( );
        } finally {
            if( reader != null ) {
                try {
                    reader.close( );
                } catch( IOException e ) {
                    e.printStackTrace( );
                }
            }
        }

        String body = builder.toString( );
        return body;
    }
}
