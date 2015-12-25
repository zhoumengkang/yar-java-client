package net.mengkang.yar.client;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by zhoumengkang on 3/12/15.
 */
final class YarClientHandler implements InvocationHandler {

    private YarClient yarClient;

    YarClientHandler(YarClient yarClient) {
        this.yarClient = yarClient;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return yarClient.invoke(method.getName(), args);
    }
}
