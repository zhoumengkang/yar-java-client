package net.mengkang.yar.concurrent.client;

import net.mengkang.yar.YarClientException;
import net.mengkang.yar.YarConfig;
import net.mengkang.yar.YarConstants;
import net.mengkang.yar.client.YarClientOptions;

/**
 * Created by zhoumengkang on 16/12/15.
 */
public class YarConcurrentTask {
    private int id;
    private String uri;
    private String method;
    private Object[] params;
    private int protocol;
    private String packagerName;
    private YarConcurrentCallback callback;
    private YarConcurrentErrorCallback errorCallback;
    private YarClientOptions yarClientOptions;

    public YarConcurrentTask(String uri, String method, Object[] params, String packagerName, YarConcurrentCallback callback) {
        this.uri = uri;
        this.method = method;
        this.params = params;
        this.packagerName = packagerName;
        this.callback = callback;
        init(uri,null);
    }

    public YarConcurrentTask(String uri, String method, Object[] params, String packagerName, YarConcurrentCallback callback, YarClientOptions yarClientOptions) {
        this.uri = uri;
        this.method = method;
        this.params = params;
        this.packagerName = packagerName;
        this.callback = callback;
        init(uri,yarClientOptions);
    }

    public YarConcurrentTask(String uri, String method, Object[] params, String packagerName, YarConcurrentCallback callback, YarConcurrentErrorCallback errorCallback) {
        this.uri = uri;
        this.method = method;
        this.params = params;
        this.packagerName = packagerName;
        this.callback = callback;
        this.errorCallback = errorCallback;
        init(uri,null);
    }

    public YarConcurrentTask(String uri, String method, Object[] params, String packagerName, YarConcurrentCallback callback, YarConcurrentErrorCallback errorCallback,YarClientOptions yarClientOptions) {
        this.uri = uri;
        this.method = method;
        this.params = params;
        this.packagerName = packagerName;
        this.callback = callback;
        this.errorCallback = errorCallback;
        init(uri,yarClientOptions);
    }

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

        this.yarClientOptions = new YarClientOptions();

        if (yarClientOptions != null) {
            this.yarClientOptions = yarClientOptions;
        }

        if (this.yarClientOptions.getConnect_timeout() <= 0) {
            this.yarClientOptions.setConnect_timeout(YarConfig.getInt("yar.connect.timeout"));
        }
        if (this.yarClientOptions.getPackager() == null) {
            this.yarClientOptions.setPackager(YarConfig.getString("yar.packager"));
        }
        if (this.yarClientOptions.getPersistent() <= 0) {
            this.yarClientOptions.setPersistent(YarConfig.getInt("yar.persistent"));
        }
        if (this.yarClientOptions.getTimeout() <= 0) {
            this.yarClientOptions.setTimeout(YarConfig.getInt("yar.timeout"));
        }
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getProtocol() {
        return protocol;
    }

    public void setProtocol(int protocol) {
        this.protocol = protocol;
    }

    public String getPackagerName() {
        return packagerName;
    }

    public void setPackagerName(String packagerName) {
        this.packagerName = packagerName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Object[] getParams() {
        return params;
    }

    // debug
    public String getParamsString(){
        StringBuffer sb = new StringBuffer("[");
        int paramsLength = params.length;
        for (int i = 0; i < paramsLength; i++) {
            sb.append(params[i].toString());
            if (i < paramsLength - 1){
                sb.append(",");
            }else{
                sb.append("]");
            }
        }
        return sb.toString();
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public YarConcurrentCallback getCallback() {
        return callback;
    }

    public void setCallback(YarConcurrentCallback callback) {
        this.callback = callback;
    }

    public YarClientOptions getYarClientOptions() {
        return yarClientOptions;
    }

    public void setYarClientOptions(YarClientOptions yarClientOptions) {
        this.yarClientOptions = yarClientOptions;
    }

    public YarConcurrentErrorCallback getErrorCallback() {
        return errorCallback;
    }

    public void setErrorCallback(YarConcurrentErrorCallback errorCallback) {
        this.errorCallback = errorCallback;
    }
}
