/*
 * Copyright 2015 - MediaWikiEngine
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

package com.github.mediawikiengine.actions.basic;

import com.github.mediawikiengine.actions.WikimediaApiAction;
import com.github.mediawikiengine.actions.exceptions.ActionFailedException;
import com.github.mediawikiengine.http.Connection;
import com.github.mediawikiengine.util.Credentials;
import org.w3c.dom.NodeList;

import java.util.logging.Level;

public class LogIn extends WikimediaApiAction
{

    public LogIn(Credentials cred)
    {

        addParameter("lgname", cred.getUsername());
        addParameter("lgpassword", cred.getPassword());
    }

    public LogIn(Credentials cred, String domain)
    {

        this(cred);
        addParameter("domain", domain);
    }

    @Override
    public synchronized void execute(Connection con) throws ActionFailedException
    {

        super.execute(con);

        NodeList loginList = getXmlDocument().getElementsByTagName("login");
        String result = loginList.item(0).getAttributes().getNamedItem("result").getNodeValue();

        if (result.equals("NeedToken"))
        {
            String token = loginList.item(0).getAttributes().getNamedItem("token").getNodeValue();
            logger.log(Level.INFO, "We need a token. So now we include a token and try it again. TOKEN: " + token);
            addParameter("lgtoken", token);
            execute(con);
        }
        else if (result.equals("NoName") || result.equals("Illegal") || result.equals("NotExists") || result.equals
                ("EmptyPass") || result.equals("WrongPass") || result.equals("WrongPluginPass"))
        {
            throw new ActionFailedException("Username and/or password error", Level.SEVERE);
        }
        else if (result.equals("Throttled"))
        {
            long wait = Long.parseLong(loginList.item(0).getAttributes().getNamedItem("token").getNodeValue());
            logger.log(Level.WARNING, "Need to wait for " + wait + " seconds (Throttled Login)");

            try
            {
                Thread.sleep(wait * 1000l);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            execute(con);
        }
        else if (result.equals("CreateBlocked") || result.equals("Blocked"))
        {
            throw new ActionFailedException("User blocked", Level.SEVERE);
        }
        else if (result.equals("Success"))
        {
            logger.log(Level.INFO, "Logged in successfully");
        }
        else
        {
            throw new ActionFailedException("Unknown Error", Level.SEVERE);
        }
    }

    @Override
    public boolean getRequiresReadPermission()
    {

        return false;
    }

    @Override
    public boolean getRequiresWritePermission()
    {

        return false;
    }

    @Override
    public String getActionName()
    {

        return "login";
    }
}
