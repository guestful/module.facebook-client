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

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class FacebookAccessToken {

    private final String value;
    private final int expiresIn;

    public FacebookAccessToken(String value, int expiresIn) {
        this.value = value;
        this.expiresIn = expiresIn;
    }

    public FacebookAccessToken(String value) {
        this.value = value;
        this.expiresIn = -1;
    }

    public String getValue() {
        return value;
    }

    public int getExpiresIn() {
        return expiresIn;
    }
}
