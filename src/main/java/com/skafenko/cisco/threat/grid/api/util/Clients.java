package com.skafenko.cisco.threat.grid.api.util;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;
import lombok.experimental.UtilityClass;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

@UtilityClass
public class Clients {

    public static Client hostIgnoringClient() {
        ClientConfig config = new DefaultClientConfig();
        config.getClasses().add(JacksonJsonProvider.class);
        HostnameVerifier hostnameVerifier = (s, sslSession) -> true;
        HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
        config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES,
                new HTTPSProperties(hostnameVerifier, sslContext()));
        return Client.create(config);
    }

    private static SSLContext sslContext() {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};
        try {
            SSLContext context = SSLContext.getInstance("TLSv1.2");
            context.init(null, trustAllCerts, null);
            return context;
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException("couldn't initialize SSL: " + e.getMessage());
        }
    }
}
