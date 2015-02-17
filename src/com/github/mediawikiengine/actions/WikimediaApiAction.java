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

package com.github.mediawikiengine.actions;

import com.github.mediawikiengine.actions.exceptions.ActionFailedException;
import com.github.mediawikiengine.http.Connection;
import com.github.mediawikiengine.var.OutputFormats;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

public abstract class WikimediaApiAction extends ApiAction
{

    protected DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    protected DocumentBuilder builder;

    private Document outputDoc = null;

    protected WikimediaApiAction()
    {

        addParameter("action", getActionName());
        addParameter("format", OutputFormats.Xml.toString());

        factory.setNamespaceAware(true);

        try
        {
            builder = factory.newDocumentBuilder();
        }
        catch (ParserConfigurationException e)
        {
            e.printStackTrace();
        }
    }

    public synchronized void execute(Connection con) throws ActionFailedException
    {

        long beginTime = System.currentTimeMillis();

        super.execute(con);

        try
        {
            outputDoc = builder.parse(new InputSource(new StringReader(getRawOutput())));
        }
        catch (SAXException | IOException e)
        {
            e.printStackTrace();
        }


        long endTime = System.currentTimeMillis();
        long timeTaken = endTime - beginTime;

        if (getRequiresReadPermission())
        {
            long timeToWait;

            if (getRequiresWritePermission())
            {
                timeToWait = (Integer.parseInt(defaultConfig.getString("wikimedia.timebetweenreads")) * 1000) -
                        timeTaken;
            }
            else
            {
                timeToWait = (Integer.parseInt(defaultConfig.getString("wikimedia.timebetweenwrites")) * 1000) -
                        timeTaken;
            }

            if (timeToWait > 0)
            {
                try
                {
                    Thread.sleep(timeToWait);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public abstract boolean getRequiresReadPermission();

    public abstract boolean getRequiresWritePermission();

    public String toString()
    {

        if (outputDoc == null)
        {
            return "";
        }
        else
        {
            Transformer transformer = null;
            try
            {
                transformer = TransformerFactory.newInstance().newTransformer();
            }
            catch (TransformerConfigurationException e)
            {
                return "";
            }

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            //initialize StreamResult with File object to save to file
            StreamResult result = new StreamResult(new StringWriter());
            DOMSource domSource = new DOMSource(outputDoc);

            try
            {
                transformer.transform(domSource, result);
            }
            catch (TransformerException e)
            {
                return "";
            }

            return result.getWriter().toString();
        }
    }

    public Document getXmlDocument()
    {

        return outputDoc;
    }
}
