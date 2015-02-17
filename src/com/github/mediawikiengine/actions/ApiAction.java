/*
 * Copyright $year - MediaWikiEngine
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.github.mediawikiengine.actions;

import com.github.mediawikiengine.actions.exceptions.ActionFailedException;
import com.github.mediawikiengine.http.Connection;
import com.github.mediawikiengine.http.Parameters;
import com.github.mediawikiengine.http.exceptions.ReturnCodeException;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class ApiAction
{

    protected static Logger logger = Logger.getLogger("WebserviceCloud");

    private static Locale currentLocale = Locale.getDefault();

    protected static ResourceBundle defaultConfig = ResourceBundle.getBundle("net.webservicecloud.interfaces" +
            ".default", currentLocale);

    protected Parameters parameters = new Parameters();

    private String rawOutput = null;

    private boolean executed = false;

    protected void addParameter(String key, String value)
    {

        parameters.set(key, value);
    }

    protected void addParameter(String key)
    {

        parameters.set(key);
    }

    public boolean isExecuted()
    {

        return executed;
    }

    public String getRawOutput()
    {

        return rawOutput;
    }

    public void execute(Connection con) throws ActionFailedException
    {

        if (executed)
        {
            logger.log(Level.WARNING, "Executed action '" + getActionName() + "' more then once");
        }
        else
        {
            logger.log(Level.INFO, "Executing action: " + getActionName() + " | params:" + parameters);
        }

        try
        {
            rawOutput = con.postRequest(parameters);
        }
        catch (ReturnCodeException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        executed = true;
    }

    public abstract String getActionName();

    @Override
    public String toString()
    {

        return getRawOutput();
    }
}
