package com.amaksakov.lightsaber.context;

import com.amaksakov.lightsaber.database.AccountHandler;
import com.amaksakov.lightsaber.database.SynchronizedAccountHandler;
import com.amaksakov.lightsaber.database.validation.DebitAccountValidator;
import com.amaksakov.lightsaber.database.validation.Validator;

public interface ContextProviderInterface {
    Validator getValidator();

    AccountHandler getAccountHandler();
}
