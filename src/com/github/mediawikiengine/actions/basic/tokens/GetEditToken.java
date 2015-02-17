package com.github.mediawikiengine.actions.basic.tokens;

import com.github.mediawikiengine.actions.ReturnableApiAction;
import com.github.mediawikiengine.actions.WikimediaApiAction;
import org.w3c.dom.NodeList;

public class GetEditToken extends WikimediaApiAction implements ReturnableApiAction
{

    public GetEditToken()
    {

        addParameter("prop", "info");
        addParameter("titles", "GetToken");
        addParameter("intoken", "edit");
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

        return "query";
    }

    public String getResult()
    {

        NodeList pageList = getXmlDocument().getElementsByTagName("page");
        String editToken = pageList.item(0).getAttributes().getNamedItem("edittoken").getNodeValue();

        return editToken;
    }

}
