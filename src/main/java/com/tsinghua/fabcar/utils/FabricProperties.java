package com.tsinghua.fabcar.utils;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Fabric-Gateway配置属性
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "fabric")
public class FabricProperties {

    private String mspId;
    private String networkConnectionConfigPath;
    private String certificatePath;
    private String privateKeyPath;
    private String tlsCertPath;
    private String channel;
    private String chaincodeName;
    private String contractName;
    private String peerEndpoint;
    private String peerOverrideAuth;
}
