package com.basic.rpc.netty;

import lombok.Data;

/**
 * RPC Response
 */

@Data
public class RpcResponse {
    private String requestId;
    private String error;
    private Object result;
}
