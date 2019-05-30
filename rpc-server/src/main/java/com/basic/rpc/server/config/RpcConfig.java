package com.basic.rpc.server.config;

import com.basic.rpc.netty.RpcServer;
import com.basic.rpc.zk.ServiceRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RpcConfig {

    @Value("${registry.address}")
    private String registryAddress;

    @Value("${rpc.server.host}")
    private String serverHost;

    @Value("${rpc.server.port}")
    private int serverPort;

    @Bean
    public ServiceRegistry serverRegistry(){
        return new ServiceRegistry(registryAddress);
    }

    @Bean
    public RpcServer rpcServer(){
        return new RpcServer(serverHost,serverPort,serverRegistry());
    }
}
