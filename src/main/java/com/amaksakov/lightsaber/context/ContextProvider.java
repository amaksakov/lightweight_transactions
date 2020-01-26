package com.amaksakov.lightsaber.context;

import com.amaksakov.lightsaber.database.SynchronizedAccountHandler;
import com.amaksakov.lightsaber.database.validation.DebitAccountValidator;
import com.amaksakov.lightsaber.database.validation.Validator;

public class ContextProvider {
    private static ContextProvider instance = null;

    public static ContextProvider getInstance() {
        if (instance == null) {
            synchronized (SynchronizedAccountHandler.class) {
                if (instance == null) {
                    instance = new ContextProvider();
                }
            }
        }
        return instance;
    }

    public Validator getValidator() {
        return new DebitAccountValidator();
    }
}
