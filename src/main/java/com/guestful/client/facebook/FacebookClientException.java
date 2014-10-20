/**
 * Copyright (C) 2013 Guestful (info@guestful.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.guestful.client.facebook;

import javax.json.JsonObject;
import javax.ws.rs.core.Response;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class FacebookClientException extends FacebookException {

    private final Response.StatusType status;
    private final int code;
    private final int subcode;
    private final String type;
    private final String errorMessage;

    FacebookClientException(Response response) {
        this(response.getStatusInfo(), response.readEntity(JsonObject.class).getJsonObject("error"));
    }

    FacebookClientException(Response.StatusType status, JsonObject error) {
        super(error.toString());
        this.status = status;
        this.code = error.getInt("code", -1);
        this.subcode = error.getInt("error_subcode", -1);
        this.type = error.getString("type", "");
        this.errorMessage = error.getString("message", "");
    }

    public Response.StatusType getStatus() {
        return status;
    }

    public int getSubcode() {
        return subcode;
    }

    public int getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
