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

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;

public class ListResult
{

    private ArrayList<Row> rows = new ArrayList<>();

    public ListResult()
    {

    }

    public ListResult(NodeList nl)
    {

        for (int i = 0; i < nl.getLength(); i++)
        {
            NamedNodeMap nnm = nl.item(i).getAttributes();
            Row row = new Row();

            for (int j = 0; j < nnm.getLength(); j++)
            {
                Node node = nnm.item(j);

                String columnName = node.getLocalName();
                String value = node.getTextContent();

                row.set(columnName, value);
            }

            addRow(row);
        }
    }

    public ArrayList<Row> getRows()
    {

        return rows;
    }

    public void addRow(Row row)
    {

        rows.add(row);
    }

    public ArrayList<String> getColumnNames()
    {

        return rows.get(0).getColumnNames();
    }

    public class Row
    {

        HashMap<String, String> rowFacade = new HashMap<>();

        public Row()
        {

        }

        public String get(String columnName)
        {

            return rowFacade.get(columnName);
        }

        public void set(String columnName, String value)
        {

            rowFacade.put(columnName, value);
        }

        public void delete(String columnName)
        {

            rowFacade.remove(columnName);
        }

        public void clear()
        {

            rowFacade.clear();
        }

        ArrayList<String> getColumnNames()
        {

            ArrayList<String> al = new ArrayList<String>();
            for (String column : rowFacade.keySet())
            {
                al.add(column);
            }

            return al;
        }
    }


}
