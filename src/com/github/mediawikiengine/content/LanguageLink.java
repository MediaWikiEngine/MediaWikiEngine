/**
 * Copyright 2012 - WebserviceCloud.net
 *
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
 * <p/>
 * Created on: 24-9-12 23:39
 */

package com.github.mediawikiengine.content;

public class LanguageLink extends WikiContent
{

    private String language;

    private String link;

    public LanguageLink(String language, String link)
    {

        this.language = language;
        this.link = link;
    }

    public String getLanguage()
    {

        return language;
    }

    public String getLink()
    {

        return link;
    }

    @Override
    public String toWikiText()
    {

        return "[[" + language + ":" + link + "]]";
    }
}
