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

import org.glassfish.jersey.jsonp.JsonProcessingFeature;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.lang.reflect.Method;
import java.util.logging.LogManager;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(JUnit4.class)
public class FacebookClientTest {

    static {
        LogManager.getLogManager().reset();
        SLF4JBridgeHandler.install();
        LoggerFactory.getILoggerFactory();
    }

    @Test
    public void test() throws Exception {

        Client restClient = ClientBuilder.newBuilder().build();
        restClient.register(JsonProcessingFeature.class);

        FacebookConfig config = new FacebookConfig();
        config.setAppID(System.getenv("GUESTFUL_FACEBOOK_APP_ID"));
        config.setAppSecret(System.getenv("GUESTFUL_FACEBOOK_APP_SECRET"));

        FacebookClient client = new FacebookClient(restClient, config);

        FacebookUnsignedRequest request = client.unsignRequest("eeTf3Qlp-sF30IYeJmi5tNx6gK15-rcpQD57liRgOcI.eyJhbGdvcml0aG0iOiJITUFDLVNIQTI1NiIsImNvZGUiOiJBUUQ4Mk5za0JxSk1XV2QwZTE4M0NGNzc2S1BfeVBodGY4YmxFVHRfaER5cnFQMzFXSWVVeG5jdnp3dVdZVmRudVJDSHltVkI5ZmdKZDFTSzQtME1vdHlySnlzbHg5WVA0SDZsQndDVkQ5bzZwRTl2T2xXYjdiYXNwZUNFT3FUQk5wQ25JR1VtZHZCQ2d1NGdoR0wzTUlTUDg2dUF1ekY5SS1fZXh3aVJqRTZsNmcxRjhxS3l1bnQ2dnU5Si11NVVSVmtxRm1scnRFQTVmanpyR2FCZ2dVOTFBSDZ3Q2NSTnd3U0F1RHl3RUQ2MVVyUGhpc1drM3Z0VFI5ODc3TFZIQU9fRkoxMlhvVzl3U0ZwTlNZYnRjR1pCNTJpYm05OXRDak43dEdubDFhR0JYTUJZSEVmbXY4RlBZcmY3aXdXUnpJM29OUEJXOTd5eHV4UG5DZ1M4NnM1YyIsImlzc3VlZF9hdCI6MTQwNjIyMjE4OCwidXNlcl9pZCI6IjEwMjAyOTg5NzgxNTYwMTAxIn0");

        for (Method method : request.getClass().getMethods()) {
            if(method.getParameterCount() == 0 && method.getReturnType() != void.class && method.getReturnType() != Void.class) {
                System.out.println(method.getName() + ": " + method.invoke(request));
            }
        }

        /*String token = "CAAVhfm0pd3wBAGMkWJldIYRUeWblZCJQrnuIWZA5lLtWb5YHGcBZCpLmNMSvS6pV1ZA047wcPJKF6wNA9SZBromjoZCQ3RM7j82pFpzIn0NneECuSgGbmrvzTPeiizEO7RkBF8CEJFXq2trJ5YR2fAGXB3KiKRs8dDuyweEaAJM0Rtg84Xw7zKNHsmA5TnMhmS8fpgCmk0RgUfNQXpaJka";
        System.out.println(client.getMe(new FacebookAccessToken(token)));
        System.out.println(client.getFriends(new FacebookAccessToken(token)));*/

    }

}
