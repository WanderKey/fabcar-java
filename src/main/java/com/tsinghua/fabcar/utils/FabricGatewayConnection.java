package com.tsinghua.fabcar.utils;

import io.grpc.ManagedChannel;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.client.CallOption;
import org.hyperledger.fabric.client.Contract;
import org.hyperledger.fabric.client.Gateway;
import org.hyperledger.fabric.client.Network;
import org.hyperledger.fabric.client.identity.Identities;
import org.hyperledger.fabric.client.identity.Signers;
import org.hyperledger.fabric.client.identity.X509Identity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

/**
 * Fabric Gateway 连接对象
 */


@Configuration
@AllArgsConstructor
@Slf4j
public class FabricGatewayConnection {

    final FabricProperties fabricProperties;

    @Bean
    public Gateway gateway() throws Exception {

        BufferedReader certificateReader = Files.newBufferedReader(Paths.get(fabricProperties.getCertificatePath()), StandardCharsets.UTF_8);

        X509Certificate certificate = Identities.readX509Certificate(certificateReader);

        BufferedReader privateKeyReader = Files.newBufferedReader(Paths.get(fabricProperties.getPrivateKeyPath()), StandardCharsets.UTF_8);

        PrivateKey privateKey = Identities.readPrivateKey(privateKeyReader);

        Gateway gateway = Gateway.newInstance()
                .identity(new X509Identity(fabricProperties.getMspId(), certificate))
                .signer(Signers.newPrivateKeySigner(privateKey))
                .connection(newGrpcConnection())
                .evaluateOptions(CallOption.deadlineAfter(5, TimeUnit.SECONDS))
                .endorseOptions(CallOption.deadlineAfter(15, TimeUnit.SECONDS))
                .submitOptions(CallOption.deadlineAfter(5, TimeUnit.SECONDS))
                .commitStatusOptions(CallOption.deadlineAfter(1, TimeUnit.MINUTES))
                .connect();

        log.info("=========================================== connected fabric gateway {} " , gateway);

        return gateway;
    }

    private ManagedChannel newGrpcConnection() throws IOException, CertificateException {

        Reader tlsCertReader = Files.newBufferedReader(Paths.get(fabricProperties.getTlsCertPath()), StandardCharsets.UTF_8);
        X509Certificate tlsCert = Identities.readX509Certificate(tlsCertReader);

        return NettyChannelBuilder.forTarget(fabricProperties.getPeerEndpoint())
                .sslContext(GrpcSslContexts.forClient().trustManager(tlsCert).build())
                .overrideAuthority(fabricProperties.getPeerOverrideAuth())
                .build();
    }

    @Bean
    public Network network(Gateway gateway) {
        return gateway.getNetwork(fabricProperties.getChannel());
    }

    @Bean
    public Contract contract(Network network) {
        return network.getContract(fabricProperties.getChaincodeName(), fabricProperties.getContractName());
    }

    @Bean
    public ChaincodeEventListener chaincodeEventListener(Network network) {
        return new ChaincodeEventListener(network, fabricProperties);
    }
}
