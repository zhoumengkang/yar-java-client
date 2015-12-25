package net.mengkang.yar.transport;

import net.mengkang.yar.client.YarClientOptions;
import net.mengkang.yar.protocol.YarRequest;
import net.mengkang.yar.protocol.YarResponse;

import java.io.IOException;

/**
 * Created by zhoumengkang on 12/12/15.
 */
public class SocketTransport implements YarTransport {
    @Override
    public void open(String url, YarClientOptions yarClientOptions) {

    }

    @Override
    public YarResponse exec(YarRequest yarRequest) throws IOException {
        return null;
    }
}
