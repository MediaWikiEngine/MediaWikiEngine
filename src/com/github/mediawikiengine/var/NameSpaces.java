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

package com.github.mediawikiengine.var;

public enum NameSpaces
{
    Media(-2),
    Special(-1),
    Main(0),
    Talk(1),
    User(2),
    UserTalk(3),
    Wikipedia(4),
    WikipediaTalk(5),
    File(6),
    FileTalk(7),
    MediaWiki(8),
    MediaWikiTalk(9),
    Template(10),
    TemplateTalk(11),
    Help(12),
    HelpTalk(13),
    Category(14),
    CategoryTalk(15),
    Portal(100),
    PortalTalk(101),
    Book(108),
    BookTalk(109);

    private int id;

    NameSpaces(int id)
    {

        this.id = id;
    }

    public int getId()
    {

        return id;
    }
}
