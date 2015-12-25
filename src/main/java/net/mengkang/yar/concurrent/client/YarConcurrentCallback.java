package net.mengkang.yar.concurrent.client;

/**
 * 所有的回调必须继承该类
 * 在 loop 执行发出之后即刻执行 async 方法
 * 当 请求结果返回之后则执行 success 方法
 */
public abstract class YarConcurrentCallback {

    protected Object retValue;

    public Object call() throws Exception {
        if (retValue == null){
            async();
            return null;
        }else{
            return success();
        }
    }

    public YarConcurrentCallback setRetValue(Object retValue) {
        this.retValue = retValue;
        return this;
    }

    public abstract void async();

    public abstract Object success();

}
