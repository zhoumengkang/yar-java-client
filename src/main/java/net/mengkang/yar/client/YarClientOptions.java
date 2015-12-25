package net.mengkang.yar.client;

/**
 * Created by zhoumengkang on 22/12/15.
 */
public class YarClientOptions {
    private String packager;
    private int persistent;
    private int timeout;
    private int connect_timeout;

    public String getPackager() {
        return packager;
    }

    public void setPackager(String packager) {
        this.packager = packager;
    }

    public int getPersistent() {
        return persistent;
    }

    public void setPersistent(int persistent) {
        this.persistent = persistent;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getConnect_timeout() {
        return connect_timeout;
    }

    public void setConnect_timeout(int connect_timeout) {
        this.connect_timeout = connect_timeout;
    }
}
