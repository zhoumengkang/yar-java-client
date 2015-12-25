package net.mengkang.yar.concurrent.client;

import net.mengkang.yar.protocol.YarRequest;
import net.mengkang.yar.transport.YarTransport;
import net.mengkang.yar.transport.YarTransportFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.mengkang.yar.protocol.YarResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by zhoumengkang on 2/12/15.
 */
public class YarConcurrentClient {

    protected final static Logger logger = LoggerFactory.getLogger(YarConcurrentClient.class);

    private static ExecutorService executorService;
    private static List<YarConcurrentTask> yarConcurrentCallStack;
    private static int YAR_PROTOCOL_PERSISTENT = 0;

    static{
        init();
    }

    private static void init(){
        yarConcurrentCallStack = new ArrayList<>();
        executorService = Executors.newCachedThreadPool();
    }

    public static void call(YarConcurrentTask yarConcurrentTask) {
        yarConcurrentTask.setId(yarConcurrentCallStack.size() + 1);
        yarConcurrentCallStack.add(yarConcurrentTask);
    }

    public static boolean loop(YarConcurrentCallback callback) {

        List<Future<Object>> result =new ArrayList<>();

        try{
            for (YarConcurrentTask callStack : yarConcurrentCallStack){
                Future<Object> future = executorService.submit(new Handle(callStack));
                result.add(future);
            }

            try {
                callback.call();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }catch(Exception e){
            e.printStackTrace();
            return false;
        }


        for(Future<Object> future:result){
            try {
                if (future.get() != null){
                    logger.info(future.get().toString());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            } catch (ExecutionException e) {
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }

    public static void reset(){
        yarConcurrentCallStack = null;
        yarConcurrentCallStack = new ArrayList<>();
    }

    public static class Handle implements Callable<Object> {

        private YarConcurrentTask yarConcurrentTask;

        public Handle(YarConcurrentTask yarConcurrentTask) {
            this.yarConcurrentTask = yarConcurrentTask;
        }

        public Object call() throws Exception {

            logger.debug(String.format("%d: call api '%s' at (%c)'%s' with '%s' parameters",
                    yarConcurrentTask.getId(), yarConcurrentTask.getMethod(), (YarConcurrentClient.YAR_PROTOCOL_PERSISTENT > 0) ? 'p' : 'r', yarConcurrentTask.getUri(), yarConcurrentTask.getParamsString()));

            YarResponse yarResponse = null;

            YarRequest yarRequest = new YarRequest();
            yarRequest.setId(yarConcurrentTask.getId())
                    .setMethod(yarConcurrentTask.getMethod())
                    .setParameters(yarConcurrentTask.getParams())
                    .setPackagerName(yarConcurrentTask.getPackagerName());

            YarTransport yarTransport = YarTransportFactory.get(yarConcurrentTask.getProtocol());
            yarTransport.open(yarConcurrentTask.getUri(),yarConcurrentTask.getYarClientOptions());

            try {
                yarResponse = yarTransport.exec(yarRequest);
            } catch (IOException e) {
                if (yarConcurrentTask.getErrorCallback() != null) {
                    yarConcurrentTask.getErrorCallback().error();
                }
                e.printStackTrace();
            }

            if (yarConcurrentTask.getCallback() != null){
                assert yarResponse != null;
                return yarConcurrentTask.getCallback().setRetValue(yarResponse.getRetVal()).call();
            }
            return null;
        }
    }

}
