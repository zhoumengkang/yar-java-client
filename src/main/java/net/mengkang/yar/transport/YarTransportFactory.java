package net.mengkang.yar.transport;

import net.mengkang.yar.YarConstants;

import java.util.HashSet;

/**
 * Created by zhoumengkang on 13/12/15.
 */
public class YarTransportFactory {

    public static HashSet<Integer> yarTransportSet;

    static{
        register();
    }

    public static void register(){
        yarTransportSet = new HashSet<>();
        yarTransportSet.add(YarConstants.YAR_CLIENT_PROTOCOL_HTTP);
        yarTransportSet.add(YarConstants.YAR_CLIENT_PROTOCOL_TCP);
        yarTransportSet.add(YarConstants.YAR_CLIENT_PROTOCOL_UDP);
        yarTransportSet.add(YarConstants.YAR_CLIENT_PROTOCOL_UNIX);
    }

    public static YarTransport get(int yarTransportType) {
        if (!yarTransportSet.contains(yarTransportType)) {
            String exception  = String.format("unsupported protocol %d", yarTransportType);
            throw new IllegalArgumentException(exception);
        }

        if (yarTransportType == YarConstants.YAR_CLIENT_PROTOCOL_HTTP) {
            return new HttpTransport();
        }else{
            return new SocketTransport();
        }
    }
}
