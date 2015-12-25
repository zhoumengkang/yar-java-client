package net.mengkang.yar.concurrent.client;

import junit.framework.TestCase;
import net.mengkang.yar.YarConfig;
import net.mengkang.yar.client.YarClientOptions;

/**
 * Created by zhoumengkang on 16/12/15.
 */
public class YarConcurrentClientTest extends TestCase {

    /**
     * rpc api 地址
     */
    static String RewardScoreServiceUri = "http://mengkang.net/demo/yar-server/RewardScoreService.php";

    public class callback extends YarConcurrentCallback {

        public void async() {
            System.out.println("现在, 所有的请求都发出去了, 还没有任何请求返回");
        }

        public Object success() {
            return retValue;
        }

    }

    public class errorCallback extends YarConcurrentErrorCallback {
        @Override
        void error() {
            System.out.println("出错了");
        }
    }

    public void testLoop() throws Exception {

        String packagerName = YarConfig.getString("yar.packager");
        YarClientOptions yarClientOptions = new YarClientOptions();
        yarClientOptions.setConnect_timeout(2000);

        for (int i = 0; i < 10; i++) {
            YarConcurrentClient.call(new YarConcurrentTask(RewardScoreServiceUri, "support", new Object[]{1, 2}, packagerName, new callback()));

            YarConcurrentClient.call(new YarConcurrentTask(RewardScoreServiceUri, "support", new Object[]{1, 2}, packagerName, new callback(),yarClientOptions));
        }

        for (int i = 0; i < 10; i++) {
            YarConcurrentClient.call(new YarConcurrentTask(RewardScoreServiceUri,"post",new Object[]{1,2},packagerName,new callback(),new errorCallback()));
            YarConcurrentClient.call(new YarConcurrentTask(RewardScoreServiceUri,"post",new Object[]{1,2},packagerName,new callback(),new errorCallback(),yarClientOptions));
        }

        YarConcurrentClient.loop(new callback());
        YarConcurrentClient.reset();
    }
}