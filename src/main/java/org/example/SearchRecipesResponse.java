package org.example;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder( {
        "results",
        "offset",
        "number",
        "totalResults"
})
@Data
public class SearchRecipesResponse {

    @JsonProperty("offset")
    private Integer offset;
    @JsonProperty("number")
    private Integer number;
    @JsonProperty("totalResults")
    private Integer totalResults;
    @JsonProperty("results")
    private List<Result> results = new ArrayList<Result>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
                    "id",
                    "title",
                    "nutrition"
               })
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {
        @JsonProperty("id")
        private Integer id;
        @JsonProperty("title")
        private String title;
        @JsonProperty("nutrition")
        private Nutrition nutrition;
        @JsonIgnore
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "nutrients"
    })
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Nutrition {
        @JsonProperty("nutrients")
        private List<Nutrients> nutrients = new ArrayList<Nutrients>();
        @JsonIgnore
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "name",
            "amount",
            "unit"
    })
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Nutrients {
        @JsonProperty("name")
        private String name;
        @JsonProperty("amount")
        private Double amount;
        @JsonProperty("unit")
        private String unit;
        @JsonIgnore
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    }
}
