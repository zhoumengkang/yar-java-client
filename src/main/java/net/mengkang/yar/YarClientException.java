package net.mengkang.yar;

/**
 * Created by zhoumengkang on 2/12/15.
 */
public class YarClientException extends RuntimeException {

    public static String unsupportedProtocolAddress = "unsupported protocol address %s";
    public static String validUriExpected = "first parameter is expected to be a valid rpc server uri %s";
    public static String onlySupportHttp = "only http protocol is supported in concurrent client for now";
    public static String clientStarted = "concurrent client has already started";

    public YarClientException(String message) {
        super(message);
    }
}
