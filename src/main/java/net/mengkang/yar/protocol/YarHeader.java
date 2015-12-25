package net.mengkang.yar.protocol;


/**
 * typedef struct _yar_header {
 *      unsigned int   id;
 *      unsigned short version;
 *      unsigned int   magic_num;
 *      unsigned int   reserved;
 *      unsigned char  provider[32];
 *      unsigned char  token[32];
 *      unsigned int   body_len;
 *      }
 */

public class YarHeader {

    private int id;
    private short version;
    private int magicNum;
    private int reserved;
    private String provider;
    private String token;
    private int bodyLen;

    public YarHeader() {
        this.magicNum = YarProtocol.YAR_PROTOCOL_MAGIC_NUM;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public short getVersion() {
        return version;
    }

    public void setVersion(short version) {
        this.version = version;
    }

    public int getMagicNum() {
        return magicNum;
    }

    public void setMagicNum(int magicNum) {
        this.magicNum = magicNum;
    }

    public int getReserved() {
        return reserved;
    }

    public void setReserved(int reserved) {
        this.reserved = reserved;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getBodyLen() {
        return bodyLen;
    }

    public void setBodyLen(int bodyLen) {
        this.bodyLen = bodyLen;
    }

}
