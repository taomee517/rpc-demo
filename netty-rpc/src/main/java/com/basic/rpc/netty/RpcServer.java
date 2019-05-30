package com.basic.rpc.netty;

import com.basic.rpc.annotation.RpcService;
import com.basic.rpc.zk.ServiceRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class RpcServer implements ApplicationContextAware,InitializingBean{

    private String serverHost;
    private int serverPort;
    private ServiceRegistry serviceRegistry;
    // 用于存放接口名与服务对象之间的映射关系
    private Map<String,Object> rpcServices = new HashMap<>();

    private EventLoopGroup boss;
    private EventLoopGroup worker;

    public RpcServer(String serverHost, int serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    public RpcServer(String serverHost, int serverPort, ServiceRegistry serviceRegistry) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String,Object> serviceMap = applicationContext.getBeansWithAnnotation(RpcService.class);
        if (serviceMap !=null && serviceMap.keySet() != null && serviceMap.keySet().size() != 0) {
            for (Object service : serviceMap.values()){
                String iFaceName = service.getClass().getAnnotation(RpcService.class).value().getName();
                log.info("Loading service: {}", iFaceName);
                rpcServices.put(iFaceName,service);
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        start();
    }

    public void stop() {
        if (boss != null) {
            boss.shutdownGracefully();
        }
        if (worker != null) {
            worker.shutdownGracefully();
        }
    }

    public void start() throws Exception {
        // 配置服务端的NIO线程组
        // boss仅配置一个线程，worker配置默认值 可用核心数*2
        boss = new NioEventLoopGroup(1);
        worker = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boss,worker).channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 100)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0))
                                .addLast(new RpcDecoder(RpcRequest.class))
                                .addLast(new RpcEncoder(RpcResponse.class))
                                .addLast(new RpcHandler(rpcServices));
                    }
                });

        log.info("=====netty server init start on port: {} =====", serverPort);
        // 绑定端口，同步等待成功
        ChannelFuture task = bootstrap.bind(serverPort);
        // 将服务注册到zookeeper
        if (serviceRegistry != null) {
            serviceRegistry.register(serverHost + ":" + serverPort);
        }
       // 等待服务端监听端口关闭
        task.channel().closeFuture();
    }


}
