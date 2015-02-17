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

package com.github.mediawikiengine.http;

import java.util.Collection;
import java.util.HashMap;

public class Cookies
{

    private HashMap<String, String> hm = new HashMap<>();

    public Cookies()
    {

    }

    public Cookies(String cookieString)
    {

        addCookie(cookieString);
    }

    public void addCookie(String cookieString)
    {

        cookieString = cookieString.trim();

        if (!cookieString.trim().equals(""))
        {
            hm.put(cookieString.substring(0, cookieString.indexOf("=")), cookieString);
        }
    }

    public void removeCookie(String cookieName)
    {

        cookieName = cookieName.trim();

        hm.remove(cookieName);
    }

    public boolean doesCookieExist(String cookieName)
    {

        return hm.containsKey(cookieName);
    }

    public void empty()
    {

        hm.clear();
    }

    public Collection<String> toCookieStringCollection()
    {

        return hm.values();
    }

    public String toString()
    {

        StringBuilder sb = new StringBuilder();

        for (String key : hm.keySet())
        {
            sb.append(hm.get(key));
            sb.append(";");
        }

        if (sb.length() == 0)
        {
            return "";
        }
        else
        {
            return sb.toString();
        }
    }

    public boolean isEmpty()
    {

        return hm.isEmpty();
    }
}