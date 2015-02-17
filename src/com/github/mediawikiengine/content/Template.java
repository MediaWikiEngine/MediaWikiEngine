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

package com.github.mediawikiengine.content;

import com.github.mediawikiengine.util.StringUtil;

import java.util.LinkedHashMap;

public class Template extends WikiContent
{

    private String templateName;

    private LinkedHashMap<String, String> parameters = new LinkedHashMap<String, String>();

    public Template(String templateName)
    {

        this.templateName = templateName;
    }

    public static Template getTemplateFromContent(String content)
    {

        String templateName = content.split("\n")[0].replace("{{", "");
        Template template = new Template(templateName);

        for (String line : content.split("\n"))
        {
            if (line.contains("="))
            {
                int firstIs = line.indexOf("=");

                String key = line.substring(0, firstIs).replace("|", "").trim();
                String value = "";

                try
                {
                    value = line.substring(firstIs + 1).trim();
                    value = value.replace("<br>", "<br />");
                }
                catch (ArrayIndexOutOfBoundsException ignored)
                {
                }

                template.setParameter(key, value);
            }
            else if (!line.contains("|") && !line.contains("<br />") && !line.contains("{{") && !line.contains("}}"))
            {
                String key = template.lastParameter();
                String value = template.getParameter(key) + "<br />" + line;

                template.setParameter(key, value);
            }
        }

        return template;
    }

    public String getTemplateName()
    {

        return templateName;
    }

    private int getSizeLongestParameterKey()
    {

        int maxLength = 0;
        for (String key : parameters.keySet())
        {
            int length = key.length();
            if (length > maxLength)
            {
                maxLength = length;
            }
        }

        return maxLength;
    }

    public boolean hasParameter(String parameter)
    {

        return parameters.containsKey(parameter);
    }

    public boolean isParameterEmpty(String parameter)
    {

        if (parameters.containsKey(parameter))
        {
            return parameters.get(parameter).equals("");
        }
        else
        {
            return false;
        }
    }

    public String getParameter(String parameter)
    {

        return parameters.get(parameter);
    }

    public void setParameter(String parameter, String value)
    {

        parameters.put(parameter, value);
    }


    public void addParameter(String parameter)
    {

        if (!hasParameter(parameter))
        {
            parameters.put(parameter, "");
        }
    }

    public String lastParameter()
    {

        String lastKey = "";

        for (String key : parameters.keySet())
        {
            lastKey = key;
        }

        return lastKey;
    }

    public void sort(String... sortParams)
    {

        LinkedHashMap<String, String> newParameters = new LinkedHashMap<>();
        for (String sortParam : sortParams)
        {
            if (!hasParameter(sortParam))
            {
                newParameters.put(sortParam, "");
            }
            else
            {
                newParameters.put(sortParam, getParameter(sortParam));
            }
        }

        for (String key : parameters.keySet())
        {
            if (!newParameters.containsKey(key) && !isParameterEmpty(key))
            {
                newParameters.put(key, parameters.get(key));
            }
        }

        parameters = newParameters;
    }

    @Override
    public String toWikiText()
    {

        String returner = "";

        int maxSpaces = 2 + getSizeLongestParameterKey() + 4;
        returner += "{{" + templateName + "\n";

        for (String key : parameters.keySet())
        {
            int spaces = 2 + (maxSpaces - key.length());
            returner += "| " + key + StringUtil.repeat(" ", spaces) + " = " + parameters.get(key) + "\n";
        }

        returner += "}}";
        return returner;
    }
}
