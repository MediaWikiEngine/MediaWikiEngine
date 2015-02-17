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

package com.github.mediawikiengine.exceptions;

import java.util.logging.Level;
import java.util.logging.Logger;

public class WebserviceCloudException extends Exception
{

    private static Logger logger = Logger.getLogger("WebserviceCloud");

    private WebserviceCloudException()
    {

        super();
    }

    public WebserviceCloudException(String message, Level level)
    {

        super(message);
        logger.log(level, message);
    }

    private WebserviceCloudException(String message, Throwable cause)
    {

        super(message, cause);
    }

    private WebserviceCloudException(Throwable cause)
    {

        super(cause);
    }
}
