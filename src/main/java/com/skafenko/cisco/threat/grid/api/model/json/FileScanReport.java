package com.skafenko.cisco.threat.grid.api.model.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.skafenko.cisco.threat.grid.api.model.VirtualMachine;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@EqualsAndHashCode
public class FileScanReport {
    private List<String> tags = new ArrayList<>();
    private String md5;
    private VirtualMachine vm;
    @JsonProperty("submission_id")
    private int submissionId;
    private String state;
    private String login;
    private String sha1;
    private String filename;
    private String status;
    @JsonProperty("submitted_at")
    private String submittedAt;
    private String id;
    private String sha256;
    private String os;

    public boolean isSuccess() {
        return "succ".equalsIgnoreCase(state);
    }

    public boolean isFileTypeNotSupported() {
        return "filetype_not_supported".equalsIgnoreCase(status);
    }
}
