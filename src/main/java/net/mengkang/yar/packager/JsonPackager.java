package net.mengkang.yar.packager;

import net.mengkang.yar.protocol.YarRequest;
import net.mengkang.yar.protocol.YarResponse;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by zhoumengkang on 4/12/15.
 */
public class JsonPackager extends YarPackager {
    @Override
    public byte[] pack(YarRequest yarRequest) {
        Map<String,Object> request = requestFormat(yarRequest);
        JSONObject jsonObject = new JSONObject(request);
        String string = jsonObject.toString();
        return string.getBytes();
    }

    @Override
    public YarResponse unpack(byte[] content) {
        return responseFormat(new String(content));
    }
}
