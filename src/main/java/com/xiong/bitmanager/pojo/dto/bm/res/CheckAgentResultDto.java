package com.xiong.bitmanager.pojo.dto.bm.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * InlineResponse2002Data
 */

@Data
public class CheckAgentResultDto {
  @JsonProperty("success")
  private Boolean success;

  @JsonProperty("data")
  private CheckResult data;

  @Data
  public static class CheckResult {
    @JsonProperty("ip")
    private String ip;

    @JsonProperty("countryName")
    private String countryName;

    @JsonProperty("stateProv")
    private String stateProv;

    @JsonProperty("countryCode")
    private String countryCode;

    @JsonProperty("region")
    private String region;

    @JsonProperty("city")
    private String city;

    @JsonProperty("languages")
    private String languages;

    @JsonProperty("timeZone")
    private String timeZone;

    @JsonProperty("offset")
    private String offset;

    @JsonProperty("longitude")
    private String longitude;

    @JsonProperty("latitude")
    private String latitude;

    @JsonProperty("zip")
    private String zip;

    @JsonProperty("status")
    private Integer status;

    @JsonProperty("used")
    private Boolean used;

    @JsonProperty("usedTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime usedTime;
  }
}

