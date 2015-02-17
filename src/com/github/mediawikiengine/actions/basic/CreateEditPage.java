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
