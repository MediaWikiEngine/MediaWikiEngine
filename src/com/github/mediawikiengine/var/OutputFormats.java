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

package com.github.mediawikiengine.var;

public enum OutputFormats
{
    Json("json"),
    JsonFm("jsonfm"),
    Php("php"),
    PhpFm("phpfm"),
    Wddx("wddx"),
    WddxFm("wddxfm"),
    Xml("xml"),
    XmlFm("xmlfm"),
    Yaml("yaml"),
    YamlFm("yamlfm"),
    RawFm("rawfm"),
    Txt("txt"),
    TxtFm("txtfm"),
    Dbg("dbg"),
    DbgFm("dbgfm");

    private String format;

    OutputFormats(String format)
    {

        this.format = format;
    }

    public static OutputFormats getDefault()
    {

        return OutputFormats.Xml;
    }

    public String toString()
    {

        return format;
    }
}