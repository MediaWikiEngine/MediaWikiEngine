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

import com.github.mediawikiengine.actions.ReturnableApiAction;
import com.github.mediawikiengine.actions.WikimediaApiAction;
import org.w3c.dom.NodeList;

import java.util.ArrayList;


public class GetCategoryMembers extends WikimediaApiAction implements ReturnableApiAction
{

    public GetCategoryMembers(String title)
    {

        addParameter("list", "categorymembers");
        addParameter("cmtitle", title);
        addParameter("cmlimit", "500");
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

    public ArrayList<String> getResult()
    {

        NodeList cm = getXmlDocument().getElementsByTagName("cm");

        ArrayList<String> returner = new ArrayList<>();

        for (int i = 0; i < cm.getLength(); i++)
        {
            returner.add(cm.item(i).getAttributes().getNamedItem("title").getTextContent());
        }

        return returner;
    }
}
