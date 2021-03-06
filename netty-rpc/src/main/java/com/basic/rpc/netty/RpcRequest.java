package com.basic.rpc.netty;

import lombok.Data;

/**
 * RPC Request
 */
@Data
public class RpcRequest {
    private String requestId;
    private String className;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameters;
}