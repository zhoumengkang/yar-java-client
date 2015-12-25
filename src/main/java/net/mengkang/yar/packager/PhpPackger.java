package net.mengkang.yar.packager;

import net.mengkang.yar.protocol.YarRequest;
import net.mengkang.yar.protocol.YarResponse;

/**
 * Created by zhoumengkang on 11/12/15.
 */
public class PhpPackger extends YarPackager {
    @Override
    public byte[] pack(YarRequest yarRequest) {
        return new byte[0];
    }

    @Override
    public YarResponse unpack(byte[] content) {
        return null;
    }
}
