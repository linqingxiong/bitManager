package com.xiong.bitmanager.pojo.dto.bm.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName UpdateProxyReqDto
 * @Description TODO
 * @Author admin
 * @Date 2025/3/20 15:29
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UpdateProxyReqDto {
    @JsonProperty("ids")
    private List<String> ids = new ArrayList<>();

    @JsonProperty("ipCheckService")
    private String ipCheckService;

    @JsonProperty("proxyMethod")
    private Integer proxyMethod;

    @JsonProperty("proxyType")
    private String proxyType;

    @JsonProperty("host")
    private String host;

    @JsonProperty("port")
    private String port;

    @JsonProperty("proxyUserName")
    private String proxyUserName;

    @JsonProperty("proxyPassword")
    private String proxyPassword;

    @JsonProperty("ip")
    private String ip;

    @JsonProperty("city")
    private String city;

    @JsonProperty("province")
    private String province;

    @JsonProperty("country")
    private String country;

    @JsonProperty("isIpNoChange")
    private Boolean isIpNoChange;

    @JsonProperty("dynamicIpUrl")
    private String dynamicIpUrl;

    @JsonProperty("dynamicIpChannel")
    private String dynamicIpChannel;

    @JsonProperty("isDynamicIpChangeIp")
    private Boolean isDynamicIpChangeIp;

    @JsonProperty("isGlobalProxyInfo")
    private Boolean isGlobalProxyInfo;

    @JsonProperty("isIpv6")
    private Boolean isIpv6;
}
