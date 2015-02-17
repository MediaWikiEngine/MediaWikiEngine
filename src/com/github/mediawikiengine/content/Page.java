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

package com.github.mediawikiengine.content;

import com.github.mediawikiengine.util.StringUtil;

import java.util.Properties;

public class Page extends WikiContent
{

    private String pageTitle;

    private StringBuilder pageContent = new StringBuilder();

    private boolean minorChange = false;

    private String comment = "";

    private long revID = -1;

    private long pageID = -1;

    private Properties siteProps;

    private String cat, catTalk, talk, template, templateTalk, project, projectTalk, help, helpTalk;

    private Page(Properties siteProps)
    {

        this.siteProps = siteProps;
        cat = siteProps.getProperty("namespace_category");
        catTalk = siteProps.getProperty("namespace_categorytalk");
        talk = siteProps.getProperty("namespace_talk");
        template = siteProps.getProperty("namespace_template");
        templateTalk = siteProps.getProperty("namespace_templatetalk");
        project = siteProps.getProperty("namespace_project");
        projectTalk = siteProps.getProperty("namespace_projecttalk");
        help = siteProps.getProperty("namespace_help");
        helpTalk = siteProps.getProperty("namespace_helptalk");
    }

    private Page(String pageTitle, Properties siteProps)
    {

        this(siteProps);
        this.pageTitle = pageTitle;
    }

    public static Page getPage(String pageTitle, Properties siteProps)
    {

        return new Page(pageTitle, siteProps);
    }

    public String getPageTitle()
    {

        return pageTitle;
    }

    public void setPageTitle(String pageTitle)
    {

        this.pageTitle = pageTitle;
    }

    public String toWikiText()
    {

        return pageContent.toString();
    }

    public void setPageContent(String content)
    {

        pageContent = new StringBuilder(content);
    }

    public boolean isMinorChange()
    {

        return minorChange;
    }

    public void setMinorChange(boolean minorChange)
    {

        this.minorChange = minorChange;
    }

    public String getComment()
    {

        return comment;
    }

    public void setComment(String comment)
    {

        this.comment = comment;
    }

    public long getRevID()
    {

        return revID;
    }

    public void setRevID(long revID)
    {

        this.revID = revID;
    }

    public long getPageID()
    {

        return pageID;
    }

    public void setPageID(long pageID)
    {

        this.pageID = pageID;
    }

    public Template getTemplate(String templateName)
    {

        if (!doesTemplateExist(templateName))
        {
            return null;
        }

        int beginPosTemplate = toWikiText().indexOf("{{" + templateName);
        int cursor = beginPosTemplate + 2;
        int somethingFound = 1;

        while (true)
        {
            int beginPosFound = toWikiText().indexOf("{{", cursor);
            int enderPosFound = toWikiText().indexOf("}}", cursor);

            if (beginPosFound == -1)
            {
                cursor = enderPosFound;
                somethingFound--;
            }
            else if (beginPosFound < enderPosFound)
            {
                cursor = beginPosFound;
                somethingFound++;
            }
            else
            {
                cursor = enderPosFound;
                somethingFound--;
            }

            cursor += 2;

            if (somethingFound == 0)
            {
                break;
            }

        }

        String template = toWikiText().substring(beginPosTemplate, cursor);
        return Template.getTemplateFromContent(template);
    }

    public void replaceTemplate(Template template)
    {

        int beginPosTemplate = toWikiText().indexOf("{{" + template.getTemplateName());
        int cursor = beginPosTemplate + 2;
        int somethingFound = 1;

        while (true)
        {
            int beginPosFound = toWikiText().indexOf("{{", cursor);
            int enderPosFound = toWikiText().indexOf("}}", cursor);

            if (beginPosFound == -1)
            {
                cursor = enderPosFound;
                somethingFound--;
            }
            else if (beginPosFound < enderPosFound)
            {
                cursor = beginPosFound;
                somethingFound++;
            }
            else
            {
                cursor = enderPosFound;
                somethingFound--;
            }

            cursor += 2;

            if (somethingFound == 0)
            {
                break;
            }
        }

        String getBefore = toWikiText().substring(0, beginPosTemplate);
        String getEnd = toWikiText().substring(cursor);

        pageContent = new StringBuilder();
        pageContent.append(getBefore);
        pageContent.append(template.toWikiText());
        pageContent.append(getEnd);
    }

    public boolean doesTemplateExist(String templateName)
    {

        return toWikiText().contains("{{" + templateName);
    }

    public void addCategory(String catName)
    {

        if (!doesCategoryExist(catName))
        {
            int posBeginner;
            int posEnder = 0;

            while (true)
            {
                posBeginner = toWikiText().indexOf("[[" + cat + ":", posEnder);
                if (posBeginner == -1)
                {
                    break;
                }
                posEnder = toWikiText().indexOf("]]", posBeginner) + 2;
            }
            int absBeginner = toWikiText().indexOf("[[" + cat + ":");

            String catsText = toWikiText().substring(absBeginner, posEnder);
            String newCatsText = catsText + "\n" + "[[" + cat + ":" + catName + "]]";

            setPageContent(toWikiText().replace(catsText, newCatsText));
        }
    }

    public boolean doesCategoryExist(String catName)
    {

        return toWikiText().contains(cat + ":" + catName);
    }

    public void renameSection(String oldSectionName, String newSectionName)
    {

        for (int i = 1; i < 6; i++)
        {
            String repeatSectionSign = StringUtil.repeat("=", i);
            String oldSection = repeatSectionSign + oldSectionName + repeatSectionSign + "\n";
            String newSection = repeatSectionSign + newSectionName + repeatSectionSign + "\n";

            if (toWikiText().contains(oldSection))
            {
                setPageContent(toWikiText().replace(oldSection, newSection));
                return;
            }

            oldSection = repeatSectionSign + " " + oldSectionName + " " + repeatSectionSign + "\n";
            newSection = repeatSectionSign + " " + newSectionName + " " + repeatSectionSign + "\n";

            if (toWikiText().contains(oldSection))
            {
                setPageContent(toWikiText().replace(oldSection, newSection));
                return;
            }
        }
    }

    public boolean hasSection(String sectionName)
    {

        for (int i = 1; i < 6; i++)
        {
            String repeatSectionSign = StringUtil.repeat("=", i);
            String oldSection = repeatSectionSign + sectionName + repeatSectionSign + "\n";

            if (toWikiText().contains(oldSection))
            {
                return true;
            }

            oldSection = repeatSectionSign + " " + sectionName + " " + repeatSectionSign + "\n";

            if (toWikiText().contains(oldSection))
            {
                return true;
            }
        }
        return false;
    }

    public String getSection(String sectionName)
    {

        String[] lines = toWikiText().split("\n");
        String linesToReturn = "";
        boolean wasItFound = false;

        for (String line : lines)
        {
            if (!wasItFound)
            {
                for (int i = 1; i < 6; i++)
                {
                    String repeatSectionSign = StringUtil.repeat("=", i);
                    if (line.startsWith(repeatSectionSign + " " + sectionName) || line.startsWith(repeatSectionSign +
                            sectionName))
                    {
                        wasItFound = true;
                    }
                }

                if (wasItFound)
                {
                    continue;
                }
            }

            if (wasItFound)
            {
                for (int i = 1; i < 6; i++)
                {
                    String repeatSectionSign = StringUtil.repeat("=", i);
                    if (line.startsWith(repeatSectionSign))
                    {
                        return linesToReturn;
                    }
                }

                linesToReturn += line + "\n";
            }
        }

        return linesToReturn;
    }

    public synchronized void replaceSection(String sectionName, String section)
    {

        setPageContent(toWikiText().replace(getSection(sectionName), section));
    }

    public int getNumberOfLines()
    {

        return toWikiText().split("\n").length;
    }
}
