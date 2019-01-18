package com.skafenko.cisco.threat.grid.api;

import com.skafenko.cisco.threat.grid.api.model.Playbook;
import com.skafenko.cisco.threat.grid.api.model.VirtualMachine;
import com.skafenko.cisco.threat.grid.api.model.json.FileScanMetaData;
import com.skafenko.cisco.threat.grid.api.model.json.StateResponse;
import com.skafenko.cisco.threat.grid.api.util.Clients;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.MultiPart;
import com.sun.jersey.multipart.file.StreamDataBodyPart;
import lombok.Setter;
import org.codehaus.jettison.json.JSONArray;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

@Setter
class Engine {
    private static final String SAMPLES_URL = "https://panacea.threatgrid.eu/api/v2/samples";
    private static final String SAMPLES_STATE_URL = "https://panacea.threatgrid.eu/api/v2/samples/state";
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
        this.client = Clients.hostIgnoringClient();
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

    public StateResponse getFilesReport(Collection<String> ids) {
        return client.resource(SAMPLES_STATE_URL)
                .queryParam(API_KEY, apikey)
                .queryParam("ids", new JSONArray(ids).toString())
                .get(StateResponse.class);
    }
}
