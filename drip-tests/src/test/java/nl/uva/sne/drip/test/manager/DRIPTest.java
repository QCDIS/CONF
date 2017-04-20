/*
 * Copyright 2017 S. Koulouzis, Wang Junchao, Huan Zhou, Yang Hu 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.uva.sne.drip.test.manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.apache.http.*;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author S. Koulouzis
 */
public class DRIPTest {

    private static List<String> PROPERTIES_FILE_PATHS = new ArrayList<>();
    public static List<Properties> propertiesList = new ArrayList<>();

    public static final String ACCESS_KEY_ID_PROPPERTY_NAME = "access.key.id";
    public static final String CLOUD_PROPVIDER_PROPPERTY_NAME = "cloud.provider.name";
    public static final String SECRET_KEY_PROPERTY_NAME = "secret.key";
    public static final String CLOUD_PRIVATE_KEY_PATHS_PROPERTY_NAME = "cloud.private.key.paths";
    public static final String DRIP_HOST_PROPERTY_NAME = "drip.host";
    public static final String DRIP_TEST_PROPERTY_FILES_PROPERTY_NAME = "test.property.files";

    @BeforeClass
    public static void setUpClass() {
        try (InputStream is = ClassLoader.getSystemResourceAsStream("test.properties")) {
            Properties mainPropertyFile = new Properties();
            mainPropertyFile.load(is);
            PROPERTIES_FILE_PATHS = Arrays.asList(mainPropertyFile.getProperty(DRIP_TEST_PROPERTY_FILES_PROPERTY_NAME).split(","));

            for (String propFile : PROPERTIES_FILE_PATHS) {
                try (InputStream inStream = ClassLoader.getSystemResourceAsStream(propFile)) {
                    Properties prop = new Properties();
                    prop.load(inStream);
                    propertiesList.add(prop);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(TestCloudCredentialsController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(TestCloudCredentialsController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(DRIPTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
              

    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    private Client create(String host) {
//        SSLContext sc;
//        ClientBuilder builder = ClientBuilder.newBuilder();
//
//        try {
//            builder.hostnameVerifier(new javax.net.ssl.HostnameVerifier() {
//                public boolean verify(String hostname, javax.net.ssl.SSLSession sslSession) {
//                    if (hostname.equals(host)) {
//                        return true;
//                    }
//                    return false;
//                }
//            });
//            builder = builder.sslContext(sc);
//            if (basicAutorization != null) {
//                builder.register(basicAutorization);
//            }
//        } catch (NullPointerException npe) {
//            LOGGER.warn("Null SSL context, skipping client SSL configuration", npe);
//        }
//        return builder.build();
        return null;
    }

}