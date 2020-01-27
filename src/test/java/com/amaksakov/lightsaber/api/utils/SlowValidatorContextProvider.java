package com.amaksakov.lightsaber.api.utils;

import com.amaksakov.lightsaber.context.ContextProviderInterface;
import com.amaksakov.lightsaber.database.AccountHandler;
import com.amaksakov.lightsaber.database.SynchronizedAccountHandler;
import com.amaksakov.lightsaber.database.validation.DebitAccountValidator;
import com.amaksakov.lightsaber.database.validation.Validator;


public class SlowValidatorContextProvider implements ContextProviderInterface {
    private static SlowValidatorContextProvider instance = null;

    public static SlowValidatorContextProvider getInstance() {
        if (instance == null) {
            synchronized (SynchronizedAccountHandler.class) {
                if (instance == null) {
                    instance = new SlowValidatorContextProvider();
                }
            }
        }
        return instance;
    }

    @Override
    public Validator getValidator() {
        return new SlowAccountValidator();
    }

    @Override
    public AccountHandler getAccountHandler()
    {
        return SynchronizedAccountHandler.getInstance(this);
    }
}
