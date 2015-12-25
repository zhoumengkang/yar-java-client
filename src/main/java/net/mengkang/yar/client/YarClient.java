package net.mengkang.yar.client;

import net.mengkang.yar.YarClientException;
import net.mengkang.yar.YarConstants;
import net.mengkang.yar.protocol.YarRequest;
import net.mengkang.yar.transport.YarTransport;
import net.mengkang.yar.YarConfig;
import net.mengkang.yar.protocol.YarResponse;
import net.mengkang.yar.transport.YarTransportFactory;

import java.io.*;
import java.lang.reflect.Proxy;

/**
 * Created by zhoumengkang on 2/12/15.
 */

public class YarClient {
    private String uri;
    private int protocol;
    private YarClientOptions options;

    public YarClient(String uri) {
        init(uri,null);
    }

    public YarClient(String uri,YarClientOptions yarClientOptions) {
        init(uri,yarClientOptions);
    }

    /**
     * YarClient 对象的初始化
     * @param uri
     * @param yarClientOptions
     */
    private void init(String uri,YarClientOptions yarClientOptions){

        if (uri.startsWith("http://") | uri.startsWith("https://")) {
            this.protocol = YarConstants.YAR_CLIENT_PROTOCOL_HTTP;
        } else if (uri.startsWith("tcp://")) {
            this.protocol = YarConstants.YAR_CLIENT_PROTOCOL_TCP;
        } else if (uri.startsWith("udp://")) {
            this.protocol = YarConstants.YAR_CLIENT_PROTOCOL_UDP;
        } else if (uri.startsWith("unix://")) {
            this.protocol = YarConstants.YAR_CLIENT_PROTOCOL_UNIX;
        } else {
            throw new YarClientException(String.format(YarClientException.unsupportedProtocolAddress,uri));
        }

        this.uri = uri;

        this.options = new YarClientOptions();
        // 设置各种属性
        if (yarClientOptions != null){
            this.options = yarClientOptions;
        }

        if (this.options.getConnect_timeout() <= 0) {
            this.options.setConnect_timeout(YarConfig.getInt("yar.connect.timeout"));
        }
        if (this.options.getPackager() == null) {
            this.options.setPackager(YarConfig.getString("yar.packager"));
        }
        if (this.options.getPersistent() <= 0) {
            this.options.setPersistent(YarConfig.getInt("yar.persistent"));
        }
        if (this.options.getTimeout() <= 0) {
            this.options.setTimeout(YarConfig.getInt("yar.timeout"));
        }

    }

    public final Object useService(Class type) {
        YarClientHandler handler = new YarClientHandler(this);
        if (type.isInterface()) {
            return Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, handler);
        } else {
            return Proxy.newProxyInstance(type.getClassLoader(), type.getInterfaces(), handler);
        }
    }

    public Object invoke(String method,Object[] args){

        YarResponse yarResponse = null;

        YarRequest yarRequest = new YarRequest();
        yarRequest.setId(123456789)
                .setMethod(method)
                .setParameters(args)
                .setPackagerName(this.options.getPackager());

        YarTransport yarTransport = YarTransportFactory.get(this.protocol);
        yarTransport.open(this.uri,this.options);

        try {
            yarResponse = yarTransport.exec(yarRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert yarResponse != null;
        return yarResponse.getRetVal();

    }

}
