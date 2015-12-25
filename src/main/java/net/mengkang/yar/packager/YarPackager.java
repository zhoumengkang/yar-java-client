package net.mengkang.yar.packager;

import org.json.JSONObject;
import net.mengkang.yar.protocol.YarRequest;
import net.mengkang.yar.protocol.YarResponse;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by zhoumengkang on 5/12/15.
 */
abstract public class YarPackager {

    public static final String YAR_PACKAGER_PHP     = "PHP";
    public static final String YAR_PACKAGER_JSON    = "JSON";
    public static final String YAR_PACKAGER_MSGPACK = "MSGPACK";

    public static Map<String,YarPackager> yarPackagerMap;

    static{
        register();
    }

    public abstract byte[] pack(YarRequest yarRequest);

    public abstract YarResponse unpack(byte[] content);

    public static YarPackager get(String packagerName) throws Exception {
        YarPackager yarPackager = yarPackagerMap.get(packagerName.toLowerCase());
        if (yarPackager == null) {
            String exception  = String.format("unsupported packager %s", packagerName);
            throw new Exception(exception);
        } else {
            return yarPackager;
        }
    }

    public static void register(){
        yarPackagerMap = new HashMap<>();
        yarPackagerMap.put(YAR_PACKAGER_PHP.toLowerCase(),new PhpPackger());
        yarPackagerMap.put(YAR_PACKAGER_JSON.toLowerCase(),new JsonPackager());
        yarPackagerMap.put(YAR_PACKAGER_MSGPACK.toLowerCase(),new MsgpackPackager());
    }

    public static Map<String,Object> requestFormat(YarRequest yarRequest){
        Map<String, Object> request = new HashMap<String, Object>();
        request.put("i", yarRequest.getId());
        request.put("m", yarRequest.getMethod());
        request.put("p", yarRequest.getParameters());

        return request;
    }

    public static YarResponse responseFormat(String data) {
        JSONObject jsonObject = new JSONObject(data);
        YarResponse yarResponse = new YarResponse();
        yarResponse.setId(jsonObject.getLong("i"));
        yarResponse.setStatus(jsonObject.getInt("s"));
        yarResponse.setOut(jsonObject.getString("o"));
        yarResponse.setRetVal(jsonObject.get("r"));

        return yarResponse;
    }

}
