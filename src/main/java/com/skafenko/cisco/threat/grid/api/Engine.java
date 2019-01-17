package com.skafenko.cisco.threat.grid.api;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.skafenko.cisco.threat.grid.api.model.Playbook;
import com.skafenko.cisco.threat.grid.api.model.VirtualMachine;
import com.skafenko.cisco.threat.grid.api.model.json.FileScanMetaData;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.MultiPart;
import com.sun.jersey.multipart.file.StreamDataBodyPart;
import lombok.Setter;

import javax.net.ssl.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

@Setter
class Engine {
    private static final String SAMPLES_URL = "https://panacea.threatgrid.eu/api/v2/samples";
    private static final String API_KEY = "api_key";

    private final String apikey;
    private final Client client;

    private VirtualMachine vm = VirtualMachine.NONE;
    private Playbook playbook = Playbook.NONE;
    private boolean isPrivate;
    private String callbackUrl;
    private boolean emailNotification;

    private Engine(String apikey) {
        this.apikey = apikey;
        this.client = getClient();
    }

    static Engine configure(String apikey) {
        return new Engine(apikey);
    }

    FileScanMetaData scanFile(InputStream in, String filename, String... tags) throws IOException {
        return scanFile(in, filename, playbook, tags);
    }

    public FileScanMetaData getFileReport(String id) {
        return client.resource(SAMPLES_URL + "/" + id)
                .queryParam(API_KEY, apikey).get(FileScanMetaData.class);
    }

    FileScanMetaData scanFile(InputStream in, String filename, Playbook playbook, String... tags) throws IOException {
        try (MultiPart multiPart = new FormDataMultiPart()
                .field(API_KEY, apikey)
                .field("vm", vm.getValue())
                .field("private", String.valueOf(isPrivate))
                .field("tags", String.join(",", tags))
                .field("playbook", playbook.name().toLowerCase())
                .field("callback_url", callbackUrl)
                .field("email_notification", String.valueOf(emailNotification))
//                .field("sample_password", "password")
                .bodyPart(new StreamDataBodyPart("sample", in, filename, MediaType.APPLICATION_OCTET_STREAM_TYPE))) {
            multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);
            WebResource resource = client.resource(SAMPLES_URL);
            return resource.type(MediaType.MULTIPART_FORM_DATA)
                    .post(FileScanMetaData.class, multiPart);
        } catch (Exception e) {
            throw e;
        }
    }

    public FileScanMetaData scanUrl(String url, String sampleName, Playbook playbook, String[] tags) throws IOException {
        try (MultiPart multiPart = new FormDataMultiPart()
                .field(API_KEY, apikey)
                .field("url", url)
                .field("sample_filename", sampleName)
                .field("vm", vm.getValue())
                .field("private", String.valueOf(isPrivate))
                .field("tags", String.join(",", tags))
                .field("playbook", playbook.name().toLowerCase())
                .field("callback_url", callbackUrl)
                .field("email_notification", String.valueOf(emailNotification))) {
            multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);
            WebResource resource = client.resource(SAMPLES_URL);
            return resource.type(MediaType.MULTIPART_FORM_DATA)
                    .post(FileScanMetaData.class, multiPart);
        } catch (Exception e) {
            throw e;
        }
    }

    private Client getClient() {
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
