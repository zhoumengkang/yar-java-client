package net.mengkang.yar.protocol;


/**
 * Created by zhoumengkang on 3/12/15.
 */


public class YarRequest {

    private String packagerName;
    private long id;
    private String method;
    private Object[] parameters;

    public String getPackagerName() {
        return packagerName;
    }

    public YarRequest setPackagerName(String packagerName) {
        this.packagerName = packagerName;
        return this;
    }

    public long getId() {
        return id;
    }

    public YarRequest setId(long id) {
        this.id = id;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public YarRequest setMethod(String method) {
        this.method = method;
        return this;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public YarRequest setParameters(Object[] parameters) {
        this.parameters = parameters;
        return this;
    }
}
