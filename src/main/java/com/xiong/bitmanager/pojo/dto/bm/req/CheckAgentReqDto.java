package com.xiong.bitmanager.pojo.dto.bm.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xiong.bitmanager.pojo.po.ProxyConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName CheckAgentReqDto
 * @Description TODO
 * @Author admin
 * @Date 2025/3/20 15:11
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckAgentReqDto {
    @JsonProperty("host")
    private String host;

    @JsonProperty("port")
    private Integer port;

    @JsonProperty("proxyType")
    private String proxyType;

    @JsonProperty("proxyUserName")
    private String proxyUserName;

    @JsonProperty("proxyPassword")
    private String proxyPassword;

    @JsonProperty("id")
    private String id;

    public CheckAgentReqDto(ProxyConfig proxyConfig) {
        this.host = proxyConfig.getHost();
        this.port = proxyConfig.getPort();
        this.proxyType = proxyConfig.getProxyType();
        this.proxyUserName = proxyConfig.getProxyUserName();
        this.proxyPassword = proxyConfig.getProxyPassword();
        this.id = proxyConfig.getId().toString();
    }
}
