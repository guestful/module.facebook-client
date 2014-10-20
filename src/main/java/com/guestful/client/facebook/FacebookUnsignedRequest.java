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

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.json.Json;
import javax.json.JsonObject;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class FacebookUnsignedRequest {

    private static final String HASH_ALGO = "HMACSHA256";

    private final boolean verified;
    private final Date creationDate;
    private final Date expirationDate;
    private final String signedRequest;
    private final String code;
    private final String algorithm;
    private final String userId;
    private final String oauthToken;
    private final String appData;
    private final JsonObject user;
    private final JsonObject page;

    FacebookUnsignedRequest(String signedRequest, String appSecret) {
        this.signedRequest = signedRequest;

        SecretKey hmacKey = new SecretKeySpec(appSecret.getBytes(), HASH_ALGO);
        String[] split = signedRequest.split("\\.", 2);
        String encoded_sig = split[0];
        String encoded_envelope = split[1];

        try {
            Mac mac = Mac.getInstance(HASH_ALGO);
            mac.init(hmacKey);
            byte[] digest = mac.doFinal(encoded_envelope.getBytes());
            this.verified = Arrays.equals(Base64.getUrlDecoder().decode(encoded_sig), digest);
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }

        JsonObject body = Json.createReader(new StringReader(new String(Base64.getUrlDecoder().decode(encoded_envelope), StandardCharsets.UTF_8))).readObject();
        this.creationDate = new Date(1000L * body.getJsonNumber("issued_at").longValue());
        this.algorithm = body.getString("algorithm");
        this.code = body.getString("code");
        this.expirationDate = body.containsKey("expires") ? new Date(1000L * body.getJsonNumber("expires").longValue()) : null;
        this.oauthToken = body.getString("oauth_token", null);
        this.userId = body.getString("user_id", null);
        this.appData = body.getString("app_data", null);
        this.user = body.getJsonObject("user");
        this.page = body.getJsonObject("page");
    }

    public boolean isVerified() {
        return verified;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public String getSignedRequest() {
        return signedRequest;
    }

    public String getCode() {
        return code;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public String getUserId() {
        return userId;
    }

    public String getOauthToken() {
        return oauthToken;
    }

    public String getAppData() {
        return appData;
    }

    public JsonObject getUser() {
        return user;
    }

    public JsonObject getPage() {
        return page;
    }

    @Override
    public String toString() {
        return getSignedRequest();
    }

}
