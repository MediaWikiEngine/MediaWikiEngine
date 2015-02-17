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

package com.github.mediawikiengine.actions.lists;

import com.github.mediawikiengine.actions.ReturnableApiAction;
import com.github.mediawikiengine.actions.WikimediaApiAction;
import com.github.mediawikiengine.content.ListResult;
import org.w3c.dom.NodeList;

public class RecentChanges extends WikimediaApiAction implements ReturnableApiAction
{

    public RecentChanges()
    {

        addParameter("list", "recentchanges");
        addParameter("rcdir", "older");
        addParameter("rcprop", "user|comment|timestamp|title|ids|sizes|redirect|patrolled");
    }

    public RecentChanges(String rcshow, int limit)
    {

        this();

        addParameter("rcshow", rcshow);
        addParameter("rclimit", "" + limit);
    }

    public ListResult getResult()
    {

        NodeList rc = getXmlDocument().getElementsByTagName("rc");
        return new ListResult(rc);
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
