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
import com.github.mediawikiengine.content.LanguageLink;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class GetLanguageLinks extends WikimediaApiAction implements ReturnableApiAction
{

    public boolean includeOwn;

    public GetLanguageLinks(String pageName, boolean includeOwn)
    {

        this(includeOwn);
        addParameter("titles", pageName);
    }

    public GetLanguageLinks(int pageID, boolean includeOwn)
    {

        this(includeOwn);
        addParameter("pageids", Integer.toString(pageID));
    }

    private GetLanguageLinks(boolean includeOwn)
    {

        this.includeOwn = includeOwn;
        if (this.includeOwn)
        {
            addParameter("meta", "siteinfo");
        }

        addParameter("prop", "langlinks");
        addParameter("redirects", "");
        addParameter("lllimit", "500");
    }

    public ArrayList<LanguageLink> getResult()
    {

        ArrayList<LanguageLink> ll = new ArrayList<LanguageLink>();
        NodeList langList = getXmlDocument().getElementsByTagName("ll");

        for (int i = 0; i < langList.getLength(); i++)
        {
            Node node = langList.item(i);
            String lang = node.getAttributes().getNamedItem("lang").getNodeValue();
            String link = node.getTextContent();
            ll.add(new LanguageLink(lang, link));
        }

        if (includeOwn)
        {
            String lang = getXmlDocument().getElementsByTagName("general").item(0).getAttributes().getNamedItem
                    ("lang").getNodeValue();
            String link = getXmlDocument().getElementsByTagName("page").item(0).getAttributes().getNamedItem("title")
                    .getNodeValue();

            ll.add(new LanguageLink(lang, link));
        }

        return ll;
    }

    @Override
    public boolean getRequiresReadPermission()
    {

        return true;
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
}
