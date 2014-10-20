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

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonStructure;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class FacebookClient {

    private static final Logger LOGGER = Logger.getLogger(FacebookClient.class.getName());

    private final Client client;
    private final WebTarget target;
    private final FacebookConfig config;
    private boolean enabled = true;

    public FacebookClient(Client restClient) {
        this(restClient, new FacebookConfig());
    }

    public FacebookClient(FacebookConfig config) {
        this(ClientBuilder.newClient(), config);
    }

    public FacebookClient(Client restClient, FacebookConfig config) {
        this.client = restClient;
        this.target = buildWebTarget();
        this.config = config;
    }

    public Client getClient() {
        return client;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public FacebookConfig getConfig() {
        return config;
    }

    protected WebTarget buildWebTarget() {
        return getClient().target("https://graph.facebook.com/v2.0");
    }

    public FacebookUnsignedRequest unsignRequest(String signedRequest) {
        return new FacebookUnsignedRequest(signedRequest, getConfig().getAppSecret());
    }

    public JsonObject getMe(FacebookAccessToken token) throws FacebookClientException {
        MultivaluedMap<String, Object> query = new MultivaluedHashMap<>();
        query.putSingle("access_token", token.getValue());
        return request(HttpMethod.GET, "/me", query, JsonObject.class).getBody();
    }

    public JsonArray getFriends(FacebookAccessToken token) throws FacebookClientException {
        MultivaluedMap<String, Object> query = new MultivaluedHashMap<>();
        query.putSingle("access_token", token.getValue());
        return request(HttpMethod.GET, "/me/friends", query, JsonArray.class).getBody();
    }

    <T extends JsonStructure> FacebookResponse<T> request(String method, String path, MultivaluedMap<String, Object> query, Class<T> type) {
        return request(method, path, null, query, type);
    }

    <T extends JsonStructure> FacebookResponse<T> request(String method, String path, JsonObject message, MultivaluedMap<String, Object> query, Class<T> type) {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest(method + " " + path + (message == null ? "" : " : " + message));
        }
        if (!isEnabled()) {
            return new FacebookResponse<T>(
                Response.ok().build(),
                type.cast(type == JsonObject.class ? Json.createObjectBuilder().build() : Json.createArrayBuilder().build()));
        }
        WebTarget webTarget = target.path(path);
        for (String param : query.keySet()) {
            webTarget = webTarget.queryParam(param, query.getFirst(param));
        }
        Invocation.Builder builder = webTarget.request(MediaType.APPLICATION_JSON_TYPE);
        Response response;
        if (message == null) {
            response = builder.method(method);
        } else {
            response = builder.method(method, Entity.json(message));
        }
        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
            throw new FacebookClientException(response);
        }
        JsonObject body = response.readEntity(JsonObject.class);
        if (body.containsKey("error")) {
            throw new FacebookClientException(response.getStatusInfo(), body.getJsonObject("error"));
        }
        JsonStructure s = body;
        if (body.containsKey("data")) {
            s = (JsonStructure) body.get("data");
        }
        return new FacebookResponse<T>(response, type.cast(s));
    }

}
