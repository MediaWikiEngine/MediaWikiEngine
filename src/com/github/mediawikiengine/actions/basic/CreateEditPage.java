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

import com.github.mediawikiengine.actions.WikimediaApiAction;
import com.github.mediawikiengine.content.Page;

public class CreateEditPage extends WikimediaApiAction
{

    public CreateEditPage(Page page, String summary, boolean minorChange, boolean botFlag, String editToken)
    {

        if (minorChange)
        {
            addParameter("minor", "true");
        }

        if (botFlag)
        {
            addParameter("bot");
        }

        addParameter("text", page.toWikiText());
        addParameter("title", page.getPageTitle());
        addParameter("token", editToken);

        if (!summary.equals(""))
        {
            addParameter("summary", summary);
        }
    }

    @Override
    public boolean getRequiresReadPermission()
    {

        return true;
    }

    @Override
    public boolean getRequiresWritePermission()
    {

        return true;
    }

    @Override
    public String getActionName()
    {

        return "edit";
    }
}
