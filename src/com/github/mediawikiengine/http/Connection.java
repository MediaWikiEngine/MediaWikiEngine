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

import com.github.mediawikiengine.http.exceptions.ReturnCodeException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

public class Connection
{

    private static ArrayList<Connection> instances = new ArrayList<>();

    private static Logger logger = Logger.getLogger("WebserviceCloud|HTTP");

    private URI apiPath;

    private String userAgent = "";

    private Cookies cookies = new Cookies();

    private Properties request = new Properties();

    private boolean compression = false;

    public Connection(URI apiPath)
    {

        this.apiPath = apiPath;
    }

    public static Connection getConnection(URI apipath)
    {

        for (Connection instance : instances)
        {
            if (instance.getApiPath().toString().equals(apipath.toString()))
            {
                logger.log(Level.INFO, "Returned an EXISTING connection to '" + apipath.toString() + "'.");
                return instance;
            }
        }

        Connection con = new Connection(apipath);
        instances.add(con);
        logger.log(Level.INFO, "Returned a NEW connection to '" + apipath.toString() + "'.");
        return con;
    }

    public synchronized String postRequest(Parameters parameters) throws ReturnCodeException, IOException
    {

        return request(parameters, "POST");
    }

    private synchronized String request(Parameters parameters, String requestMethod) throws ReturnCodeException,
            IOException
    {
        /**
         * Starting the connection
         */
        HttpURLConnection con = (HttpURLConnection) apiPath.toURL().openConnection();

        con.setRequestMethod(requestMethod);

        for (Object property : request.keySet())
        {
            con.setRequestProperty((String) property, request.getProperty((String) property));
        }

        con.setRequestProperty("User-Agent", userAgent);


        if (compression)
        {
            con.setRequestProperty("Accept-encoding", "gzip");
        }

        if (!cookies.toString().equals(""))
        {
            con.setRequestProperty("Cookie", cookies.toString());
        }

        con.setUseCaches(false);
        con.setAllowUserInteraction(false); // no user interact [like pop up]
        con.setDoOutput(true); // want to send
        con.setDoInput(true);

        /**
         * Write all parameters to stream
         */
        OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());

        writer.write(parameters.toString());
        writer.flush();
        con.connect();

        /**
         * Checking the response before doing anything else
         */
        if (con.getResponseCode() != 200)
        {
            throw new ReturnCodeException(con.getResponseCode() + ": " + con.getResponseMessage());
        }

        /**
         * Getting the cookies and store it for further use.
         */
        Map<String, List<String>> headers = con.getHeaderFields();
        List<String> values = headers.get("Set-Cookie");
        //cookies.empty();

        if (values != null)
        {
            for (String cookieLine : values)
            {
                cookies.addCookie(cookieLine);
            }
        }

        BufferedReader in = null;

        /**
         *  Getting the the input from a compressed stream to plaintext
         */
        if (compression)
        {
            in = new BufferedReader(new InputStreamReader(new GZIPInputStream(con.getInputStream()), "UTF-8"));
        }
        else
        {
            in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        }

        // get the text
        String line;
        StringBuilder text = new StringBuilder(100000);

        while ((line = in.readLine()) != null)
        {
            text.append(line);
            text.append("\n");
        }

        in.close();
        //con.disconnect();

        return text.toString();
    }

    public URI getApiPath()
    {

        return apiPath;
    }

    public String getUserAgent()
    {

        return userAgent;
    }

    public void setUserAgent(String userAgent)
    {

        this.userAgent = userAgent;
    }

    public void setRequestProperty(String key, String value)
    {

        request.setProperty(key, value);
    }

    public boolean isCompression()
    {

        return compression;
    }

    public void setCompression(boolean compression)
    {

        this.compression = compression;
    }
}