package com.baisc.rpc.client.config;


import com.basic.rpc.netty.RpcProxy;
import com.basic.rpc.zk.ServiceDiscovery;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RpcConfig {

    @Value("${registry.address}")
    private String registryAddress;

    @Bean
    public ServiceDiscovery serviceDiscovery(){
        return new ServiceDiscovery(registryAddress);
    }

    @Bean
    public RpcProxy rpcProxy(){
        return new RpcProxy(serviceDiscovery());
    }
}
