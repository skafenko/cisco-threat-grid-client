
package com.skafenko.cisco.threat.grid.api.model.json;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class FileScanMetaData {
    @JsonProperty("api_version")
    private int apiVersion;
    private int id;
    private FileScanReport data;
}
