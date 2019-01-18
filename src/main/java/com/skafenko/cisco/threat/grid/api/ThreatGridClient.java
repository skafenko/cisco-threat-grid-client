package com.skafenko.cisco.threat.grid.api;


import com.skafenko.cisco.threat.grid.api.exception.NoSuchAPIKeyException;
import com.skafenko.cisco.threat.grid.api.model.Playbook;
import com.skafenko.cisco.threat.grid.api.model.VirtualMachine;
import com.skafenko.cisco.threat.grid.api.model.json.FileScanReport;
import com.skafenko.cisco.threat.grid.api.model.json.SamplesState;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


class ThreatGridClient {

    private final Engine engine;

    private ThreatGridClient(String apikey) {
        engine = Engine.configure(apikey);
    }

    static ThreatGridClient configure(String apikey) {
        if (apikey != null && !apikey.isEmpty()) {
            return new ThreatGridClient(apikey);
        } else {
            throw new NoSuchAPIKeyException("Valid API key is required");
        }
    }

    public ThreatGridClient setVM(VirtualMachine vm) {
        engine.setVm(vm);
        return this;
    }

    public ThreatGridClient setEmailNotification(boolean value) {
        engine.setEmailNotification(value);
        return this;
    }

    public ThreatGridClient setPrivate(boolean value) {
        engine.setPrivate(value);
        return this;
    }

    public ThreatGridClient setPlaybook(Playbook playbook) {
        engine.setPlaybook(playbook);
        return this;
    }

    public ThreatGridClient setCallbackUrl(String callbackUrl) {
        engine.setCallbackUrl(callbackUrl);
        return this;
    }

    public FileScanReport scanFile(InputStream in, String filename, String... tags) throws IOException {
        return engine.scanFile(in, filename, tags)
                .getData();
    }

    public FileScanReport scanFile(InputStream in, String filename, Playbook playbook, String... tags) throws IOException {
        return engine.scanFile(in, filename, playbook, tags)
                .getData();
    }

    public FileScanReport scanUrl(String url, String sampleName, Playbook playbook, String... tags) throws IOException {
        return engine.scanUrl(url, sampleName, playbook, tags)
                .getData();
    }

    public FileScanReport scanFile(File file, String filename, String... tags) throws IOException {
        return engine.scanFile(new FileInputStream(file), filename, tags)
                .getData();
    }

    public List<SamplesState> getFilesReport(Collection<String> ids) {
        if (ids != null && !ids.isEmpty()) {
            return engine.getFilesReport(ids).getData();
        }
        return new ArrayList<>();
    }

    public FileScanReport getFileReport(String id) {
        return engine.getFileReport(id)
                .getData();
    }
}
