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

package com.github.mediawikiengine.actions.basic;

import com.github.mediawikiengine.actions.ReturnableApiAction;
import com.github.mediawikiengine.actions.WikimediaApiAction;
import com.github.mediawikiengine.content.Page;
import org.w3c.dom.NodeList;

import java.util.Properties;

public class GetPage extends WikimediaApiAction implements ReturnableApiAction
{

    private Properties siteinfo;

    private GetPage()
    {

        addParameter("prop", "revisions");
        addParameter("rvprop", "ids|comment|content");
        addParameter("rvlimit", "1");
    }

    public GetPage(String pageTitle, Properties siteinfo)
    {

        this();
        this.siteinfo = siteinfo;
        addParameter("titles", pageTitle);
    }

    public GetPage(int pageID, Properties siteinfo)
    {

        this();
        this.siteinfo = siteinfo;
        addParameter("pageids", Integer.toString(pageID));
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

    public Page getResult()
    {

        NodeList revisionList = getXmlDocument().getElementsByTagName("rev");

        long revID, pageID;

        try
        {
            revID = Long.parseLong(revisionList.item(0).getAttributes().getNamedItem("revid").getNodeValue());
        }
        catch (NullPointerException fail)
        {
            return null;
        }


        String comment = "";
        try
        {
            comment = revisionList.item(0).getAttributes().getNamedItem("comment").getNodeValue();
        }
        catch (NullPointerException ignored)
        {

        }

        String content = revisionList.item(0).getTextContent();
        String pageName = getXmlDocument().getElementsByTagName("page").item(0).getAttributes().getNamedItem("title")
                .getNodeValue();
        pageID = Long.parseLong(getXmlDocument().getElementsByTagName("page").item(0).getAttributes().getNamedItem
                ("pageid").getNodeValue());

        Page page = Page.getPage(pageName, siteinfo);
        page.setComment(comment);
        page.setRevID(revID);
        page.setPageContent(content);
        page.setPageID(pageID);

        return page;
    }
}
