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

package com.github.mediawikiengine.actions.metainfo;

import com.github.mediawikiengine.actions.ReturnableApiAction;
import com.github.mediawikiengine.actions.WikimediaApiAction;
import org.w3c.dom.NamedNodeMap;

import java.util.Properties;

public class GetUserInfo extends WikimediaApiAction implements ReturnableApiAction
{

    public GetUserInfo()
    {

        addParameter("meta", "userinfo");
        addParameter("uiprop", "rights|hasmsg|blockinfo|groups|changeablegroups|options|editcount|ratelimits|realname" +
                "|email");
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

        return "query";
    }


    public Properties getResult()
    {

        NamedNodeMap user = getXmlDocument().getElementsByTagName("userinfo").item(0).getAttributes();
        NamedNodeMap options = getXmlDocument().getElementsByTagName("options").item(0).getAttributes();

        Properties returner = new Properties();

        for (int i = 0; i < user.getLength(); i++)
        {
            returner.setProperty("userinfo_" + user.item(i).getNodeName(), user.item(i).getNodeValue());
        }

        for (int i = 0; i < options.getLength(); i++)
        {
            returner.setProperty("options_" + options.item(i).getNodeName(), options.item(i).getNodeValue());
        }

        return returner;
    }
}
