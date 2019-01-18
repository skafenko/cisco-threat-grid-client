
package com.skafenko.cisco.threat.grid.api.model.json;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class StateResponse {
    @JsonProperty("api_version")
    private int apiVersion;
    private int id;
    private List<SamplesState> data;
}
