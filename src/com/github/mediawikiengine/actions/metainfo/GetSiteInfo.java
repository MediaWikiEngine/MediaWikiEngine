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
import org.w3c.dom.NodeList;

import java.util.Properties;

public class GetSiteInfo extends WikimediaApiAction implements ReturnableApiAction
{

    public GetSiteInfo()
    {

        addParameter("meta", "siteinfo");
        addParameter("siprop", "general|namespaces|statistics|dbrepllag|interwikimap|namespacealiases");
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

        NamedNodeMap general = getXmlDocument().getElementsByTagName("general").item(0).getAttributes();
        NamedNodeMap statistics = getXmlDocument().getElementsByTagName("statistics").item(0).getAttributes();
        NodeList ns = getXmlDocument().getElementsByTagName("ns");

        Properties returner = new Properties();

        for (int i = 0; i < general.getLength(); i++)
        {
            String name = general.item(i).getNodeName();
            String value = general.item(i).getNodeValue();

            returner.setProperty("general_" + name, value);
        }

        for (int i = 0; i < statistics.getLength(); i++)
        {
            String name = statistics.item(i).getNodeName();
            String value = statistics.item(i).getNodeValue();

            returner.setProperty("statistics_" + name, value);
        }

        for (int i = 0; i < ns.getLength(); i++)
        {
            String name;
            try
            {
                name = ns.item(i).getAttributes().getNamedItem("canonical").getTextContent();
            }
            catch (NullPointerException e)
            {
                continue;
            }

            name = "namespace_" + name.toLowerCase().replace(" ", "");
            String value = ns.item(i).getTextContent();

            returner.setProperty(name, value);
        }

        return returner;
    }
}
