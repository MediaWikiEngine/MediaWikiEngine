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

package com.github.mediawikiengine.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileUtils
{

    public static String getFileFromWorkingDirectory(String locationFile) throws IOException
    {

        BufferedReader reader = new BufferedReader(new FileReader(locationFile));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");
        while ((line = reader.readLine()) != null)
        {
            stringBuilder.append(line);
            stringBuilder.append(ls);
        }
        return stringBuilder.toString();
    }

    public static String getURLSource(String url) throws MalformedURLException
    {

        return getURLSource(new URL(url));
    }

    public static String getURLSource(URL url)
    {

        BufferedReader in = null;
        try
        {
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            String output;
            StringBuffer sourceReturn = new StringBuffer();
            while ((output = in.readLine()) != null)
            {
                sourceReturn.append(output);
            }
            in.close();

            return sourceReturn.toString();
        }
        catch (IOException ex)
        {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                assert in != null;
                in.close();
            }
            catch (IOException ex)
            {
                Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
}
