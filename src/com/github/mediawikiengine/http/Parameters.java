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

package com.github.mediawikiengine.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

public class Parameters
{

    private HashMap<String, String> hm = new HashMap<String, String>();

    public void set(String key)
    {

        hm.put(key, "");
    }

    public void set(String key, String value)
    {

        hm.put(key, value);
    }

    public String get(String key)
    {

        return hm.get(key);
    }

    public String getEncoded(String key)
    {

        try
        {
            return URLEncoder.encode(hm.get(key), "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            return hm.get(key);
        }
    }

    public String toString()
    {

        StringBuilder sb = new StringBuilder();

        for (String key : hm.keySet())
        {
            sb.append(key);
            if (!hm.get(key).equals(""))
            {
                sb.append("=");

                try
                {
                    sb.append(URLEncoder.encode(hm.get(key), "UTF-8"));
                }
                catch (UnsupportedEncodingException e)
                {
                    sb.append(hm.get(key));
                }
            }
            sb.append("&");
        }

        if (sb.length() == 0)
        {
            return "";
        }
        else
        {
            return sb.substring(0, sb.length() - 1).toString();
        }
    }
}
