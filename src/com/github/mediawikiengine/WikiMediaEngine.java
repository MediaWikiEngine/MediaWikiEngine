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

package com.github.mediawikiengine;

import com.github.mediawikiengine.actions.basic.*;
import com.github.mediawikiengine.actions.basic.tokens.GetEditToken;
import com.github.mediawikiengine.actions.exceptions.ActionFailedException;
import com.github.mediawikiengine.actions.lists.RecentChanges;
import com.github.mediawikiengine.actions.metainfo.GetLanguageLinks;
import com.github.mediawikiengine.actions.metainfo.GetSiteInfo;
import com.github.mediawikiengine.actions.metainfo.GetUserInfo;
import com.github.mediawikiengine.content.LanguageLink;
import com.github.mediawikiengine.content.ListResult;
import com.github.mediawikiengine.content.Page;
import com.github.mediawikiengine.exceptions.EngineInitFailed;
import com.github.mediawikiengine.http.Connection;
import com.github.mediawikiengine.util.Credentials;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class WikiMediaEngine extends Engine
{

    protected static Locale currentLocale = Locale.getDefault();

    private static Logger logger = Logger.getLogger("WebserviceCloud|WikiMedia");

    private static ResourceBundle defaultConfig = ResourceBundle.getBundle("net.webservicecloud.interfaces.default",
            currentLocale);

    private Connection con;

    private Credentials loginCredentials;

    private URI apiFullLocation;

    private boolean botFlag;

    private String editToken = null;

    private Properties siteinfo = new Properties();

    private Properties userinfo = new Properties();

    public WikiMediaEngine(Credentials loginCredentials, URI apiFullLocation) throws EngineInitFailed
    {

        logger.log(Level.INFO, "Created new WikiMediaEngine.");

        this.loginCredentials = loginCredentials;
        this.apiFullLocation = apiFullLocation;

        if (this.apiFullLocation == null)
        {
            logger.log(Level.INFO, "API Location not valid or present. Using the one from configuration file.");
            StringBuilder builder = new StringBuilder();
            builder.append(getDefaultConfig().getString("wikimedia.api.protocol")).append("://");
            builder.append(getDefaultConfig().getString("wikimedia.api.domain"));
            builder.append("/");
            builder.append(getDefaultConfig().getString("wikimedia.api.path"));
            builder.append("/");
            builder.append(getDefaultConfig().getString("wikimedia.api.file"));

            try
            {
                this.apiFullLocation = new URI(builder.toString());
            }
            catch (URISyntaxException e)
            {
                throw new EngineInitFailed("API Path misformed", Level.SEVERE);

            }
        }

        con = Connection.getConnection(this.apiFullLocation);
        con.setUserAgent(getDefaultConfig().getString("api.name") + "/" + getDefaultConfig().getString("api.version")
                + "(User: " + loginCredentials.getUsername() + ")");
        con.setCompression(true);
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        botFlag = Boolean.getBoolean("wikimedia.botpolicy.botflag");

        logger.log(Level.INFO, "Edit token = '" + editToken + "'");

        Timer timer = new Timer();
        timer.schedule(new TimerTask()
        {

            @Override()
            public void run()
            {

                login();
                logger.log(Level.INFO, "Login... Stayin' Alive");
            }
        }, 0, 15 * 60 * 1000);


        GetSiteInfo si = new GetSiteInfo();
        GetUserInfo ui = new GetUserInfo();

        try
        {
            si.execute(con);
            ui.execute(con);
        }
        catch (ActionFailedException actionFailed)
        {
        }

        siteinfo = si.getResult();
        userinfo = ui.getResult();
    }

    public WikiMediaEngine(Credentials loginCredentials) throws EngineInitFailed
    {

        this(loginCredentials, null);
    }

    public static ResourceBundle getDefaultConfig()
    {

        return defaultConfig;
    }

    public String toString()
    {

        return defaultConfig.getString("api.name") + " " + getFrameWorkVersion() + "\n\n" + defaultConfig.getString
                ("api.about");
    }

    public String getFrameWorkVersion()
    {

        return defaultConfig.getString("api.version");
    }

    private synchronized String getEditToken()
    {

        GetEditToken token = new GetEditToken();

        try
        {
            token.execute(con);
        }
        catch (ActionFailedException actionFailed)
        {
            return null;
        }

        return token.getResult();
    }

    private synchronized void login()
    {

        LogIn lg = new LogIn(loginCredentials);

        try
        {
            lg.execute(con);
        }
        catch (ActionFailedException actionFailed)
        {
            System.exit(-1);
        }
    }

    public synchronized void logout()
    {

        try
        {
            LogOut lgo = new LogOut();
            lgo.execute(con);
        }
        catch (ActionFailedException actionFailed)
        {
            actionFailed.printStackTrace();
        }
    }

    public Page getPage(String pageTitle)
    {

        GetPage gp = new GetPage(pageTitle, siteinfo);

        try
        {
            gp.execute(con);
        }
        catch (ActionFailedException e)
        {
            e.printStackTrace();
        }

        if (gp.getResult() == null)
        {
            return Page.getPage(pageTitle, siteinfo);
        }
        else
        {
            return gp.getResult();
        }
    }

    public void setPage(Page page, String summary, boolean minor)
    {

        CreateEditPage cep = new CreateEditPage(page, summary, minor, botFlag, getEditToken());

        try
        {
            cep.execute(con);
        }
        catch (ActionFailedException e)
        {
            e.printStackTrace();
        }
    }

    public void setPage(Page page, String summary)
    {

        setPage(page, summary, false);
    }

    public void setPage(Page page)
    {

        setPage(page, "", false);
    }

    public boolean doesPageExist(Page page)
    {

        return doesPageExist(page.getPageTitle());
    }

    public boolean doesPageExist(String pageName)
    {

        GetPage gp = new GetPage(pageName, siteinfo);

        try
        {
            gp.execute(con);
        }
        catch (ActionFailedException e)
        {
            e.printStackTrace();
        }

        return gp.getResult() != null;
    }

    public boolean doesCategoryExist(String catName)
    {

        String fullName = getSiteInfo("namespace_category") + ":" + catName;
        return doesPageExist(fullName);
    }

    public ArrayList<LanguageLink> getLanguageLinks(int pageID, boolean includeOwn)
    {

        GetLanguageLinks giw = new GetLanguageLinks(pageID, includeOwn);

        try
        {
            giw.execute(con);
        }
        catch (ActionFailedException e)
        {
            e.printStackTrace();
        }

        return giw.getResult();
    }

    public ArrayList<LanguageLink> getLanguageLinks(String pageName, boolean includeOwn)
    {

        GetLanguageLinks giw = new GetLanguageLinks(pageName, includeOwn);

        try
        {
            giw.execute(con);
        }
        catch (ActionFailedException e)
        {
            e.printStackTrace();
        }

        return giw.getResult();
    }

    public ArrayList<LanguageLink> getLanguageLinks(int pageID)
    {

        return getLanguageLinks(pageID, false);
    }

    public ArrayList<LanguageLink> getLanguageLinks(String pageName)
    {

        return getLanguageLinks(pageName, false);
    }

    public LanguageLink getLanguageLink(int pageID, String cc)
    {

        ArrayList<LanguageLink> ll = getLanguageLinks(pageID, false);

        for (LanguageLink l : ll)
        {
            if (l.getLanguage().equals(cc))
            {
                return l;
            }
        }

        return null;
    }

    public LanguageLink getLanguageLink(String pageName, String cc)
    {

        ArrayList<LanguageLink> ll = getLanguageLinks(pageName, false);

        for (LanguageLink l : ll)
        {
            if (l.getLanguage().equals(cc))
            {
                return l;
            }
        }

        return null;
    }

    private ListResult getRecentChanges(String rcshow, int limit)
    {

        RecentChanges rc = new RecentChanges(rcshow, limit);

        try
        {
            rc.execute(con);
        }
        catch (ActionFailedException e)
        {
            e.printStackTrace();
        }

        return rc.getResult();
    }

    public ListResult getRecentChangesFromBots(int limit)
    {

        return getRecentChanges("bot", limit);
    }

    public ListResult getRecentChangesFromAnonymous(int limit)
    {

        return getRecentChanges("anon", limit);
    }

    public ListResult getRecentChangesFromRegisteredUsers(int limit)
    {

        return getRecentChanges("!anon", limit);
    }

    public void setNeedsBotFlag(boolean botFlag)
    {

        this.botFlag = botFlag;
    }

    public String getSiteInfo(String key)
    {

        return siteinfo.getProperty(key);
    }

    public String getUserInfo(String key)
    {

        return userinfo.getProperty(key);
    }

    public ArrayList<String> getCategoryMembers(String title)
    {

        GetCategoryMembers giw = new GetCategoryMembers(title);

        try
        {
            giw.execute(con);
        }
        catch (ActionFailedException e)
        {
            e.printStackTrace();
        }

        return giw.getResult();
    }

    @Override
    protected void finalize() throws Throwable
    {

        logout();
    }
}
