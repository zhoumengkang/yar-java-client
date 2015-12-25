package net.mengkang.yar.protocol;

import net.mengkang.yar.YarConfig;
import net.mengkang.yar.packager.YarPackager;

import java.io.*;
import java.util.Arrays;

/**
 * Created by zhoumengkang on 5/12/15.
 */
public class YarProtocol {

    public static final int YAR_PROTOCOL_MAGIC_NUM = 0x80DFEC60;
    public static final int YAR_HEADER_LENGTH = 82;
    public static final int YAR_PACKAGER_NAME_LENGTH = 8;

    public static YarHeader render(YarRequest yarRequest,int length){
        YarHeader yarHeader = new YarHeader();
        yarHeader.setId((int) yarRequest.getId());
        yarHeader.setMagicNum(YAR_PROTOCOL_MAGIC_NUM);
        yarHeader.setBodyLen(length);
        yarHeader.setProvider(YarConfig.getString("yar.provider"));
        yarHeader.setToken(YarConfig.getString("yar.token"));
        return yarHeader;
    }

    public static YarHeader parse(byte[] content){
        YarHeader yarHeader = new YarHeader();

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(content));
        try {
            yarHeader.setId(in.readInt());
            yarHeader.setVersion(in.readShort());
            if (in.readInt() != YAR_PROTOCOL_MAGIC_NUM) {
                return null;
            }
            yarHeader.setReserved(in.readInt());

            byte[] provider = new byte[32];
            in.read(provider,0,32);
            yarHeader.setProvider(new String(provider));

            byte[] token = new byte[32];
            in.read(token,0,32);
            yarHeader.setToken(new String(token));

            yarHeader.setBodyLen(in.readInt());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return yarHeader;
    }

    public static YarResponse responseFetch(byte[] responseByte) throws IOException {

        YarResponse yarResponse = new YarResponse();

        byte[] header = new byte[YAR_HEADER_LENGTH];
        for (int i = 0; i < YAR_HEADER_LENGTH; i++) {
            header[i] = responseByte[i];
        }

        YarHeader yarHeader = YarProtocol.parse(header);
        if(yarHeader == null){
            // TODO
            throw new IOException("malformed response header");
        }

        byte[] packager = new byte[YAR_PACKAGER_NAME_LENGTH];
        int packagerLength = 0;
        for (int i = 0; i < YAR_PACKAGER_NAME_LENGTH; i++) {
            packager[i] = responseByte[YAR_HEADER_LENGTH + i];
            // 在这个8字节中，当是 php 或者是 json 的时候，后面的三个或者四个字节可能之前已经被占用，需要截取下
            if (packager[i] == 0){
                packagerLength = i;
                break;
            }
        }

        String packagerName = new String(packager);
        yarResponse.setPackagerName(packagerName.substring(0, packagerLength));

        int off = YAR_HEADER_LENGTH + YAR_PACKAGER_NAME_LENGTH;
        int len = responseByte.length;

        byte[] yarResponseBody = new byte[len];
        for (int i = off; i < len; i++) {
            yarResponseBody[i - off] = responseByte[i];
        }

        try {
            return YarPackager.get(yarResponse.getPackagerName()).unpack(yarResponseBody);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] requestCreate(YarRequest yarRequest) throws IOException {

        byte[] body;
        ByteArrayOutputStream bodyOut = new ByteArrayOutputStream();
        try {
            bodyOut.write(Arrays.copyOf(yarRequest.getPackagerName().toUpperCase().getBytes(), 8));
            bodyOut.write(YarPackager.get(yarRequest.getPackagerName()).pack(yarRequest));

            body = bodyOut.toByteArray();
        } catch (Exception e) {
            throw new IOException(e);
        } finally {
            bodyOut.close();
        }

        YarHeader yarHeader = render(yarRequest,body.length);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byteArrayOutputStream);

        try {
            out.writeInt(yarHeader.getId());
            out.writeShort(yarHeader.getVersion());
            out.writeInt(yarHeader.getMagicNum());
            out.writeInt(yarHeader.getReserved());
            out.write(Arrays.copyOf(yarHeader.getProvider().getBytes(),32));
            out.write(Arrays.copyOf(yarHeader.getToken().getBytes(),32));
            out.writeInt(yarHeader.getBodyLen());

            out.write(body);

            return byteArrayOutputStream.toByteArray();
        } finally {
            byteArrayOutputStream.close();
            out.close();
        }

    }

}
