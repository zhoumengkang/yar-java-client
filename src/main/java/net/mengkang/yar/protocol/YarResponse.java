package net.mengkang.yar.protocol;

/**
 * typedef struct _yar_response {
 *      long id;
 *      int  status;
 *      zend_string *out;
 *      zval err;
 *      zval retval;
 * } yar_response_t;
 */
public class YarResponse {
    private String packagerName;
    private long id;
    private int status;
    private String out;
    private String err;
    private Object retVal;

    public String getPackagerName() {
        return packagerName;
    }

    public void setPackagerName(String packagerName) {
        this.packagerName = packagerName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOut() {
        return out;
    }

    public void setOut(String out) {
        this.out = out;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    public Object getRetVal() {
        return retVal;
    }

    public void setRetVal(Object retVal) {
        this.retVal = retVal;
    }

    @Override
    public String toString() {
        return "YarResponse{" +
                "id=" + id +
                ", status=" + status +
                ", out='" + out + '\'' +
                ", err='" + err + '\'' +
                ", retVal=" + retVal +
                '}';
    }
}
