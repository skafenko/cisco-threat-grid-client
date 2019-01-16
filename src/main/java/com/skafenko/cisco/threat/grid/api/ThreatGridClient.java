package com.skafenko.cisco.threat.grid.api;


import com.skafenko.cisco.threat.grid.api.exception.NoSuchAPIKeyException;
import com.skafenko.cisco.threat.grid.api.model.Playbook;
import com.skafenko.cisco.threat.grid.api.model.VirtualMachine;
import com.skafenko.cisco.threat.grid.api.model.json.FileScanMetaData;

import java.io.IOException;
import java.io.InputStream;


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

    public FileScanMetaData scanFile(InputStream in, String filename) throws IOException {
        return engine.scanFile(in, filename);
    }

    public FileScanMetaData scanFile(InputStream in, String filename, String... tags) throws IOException {
        return engine.scanFile(in, filename);
    }

    public FileScanMetaData getFileReport(String id) {
        return engine.getFileReport(id);
    }
}
