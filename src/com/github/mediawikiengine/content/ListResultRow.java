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

import java.util.HashMap;

public class ListResultRow
{

    private HashMap<String, String> rowContents = new HashMap<>();

    public ListResultRow()
    {

    }

    public String getData(String columnName)
    {

        return rowContents.get(columnName);
    }

    public void setData(String columnName, String data)
    {

        rowContents.put(columnName, data);
    }


}
