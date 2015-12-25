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
    static String uri = "http://mengkang.net/demo/yar-server/RewardScoreService.php";

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
            // 第一种调用方式
            YarConcurrentClient.call(new YarConcurrentTask(uri, "support", new Object[]{1, 2}, packagerName, new callback()));
            // 第二种调用方式 增加一些额外配置选项
            YarConcurrentClient.call(new YarConcurrentTask(uri, "support", new Object[]{1, 2}, packagerName, new callback(),yarClientOptions));
        }

        for (int i = 0; i < 10; i++) {
            // 第三种调用方式 有正确的回调和错误的回调
            YarConcurrentClient.call(new YarConcurrentTask(uri,"post",new Object[]{1,2},packagerName,new callback(),new errorCallback()));
            // 第四种调用方式 在第三种的基础上增加额外的配置选项
            YarConcurrentClient.call(new YarConcurrentTask(uri,"post",new Object[]{1,2},packagerName,new callback(),new errorCallback(),yarClientOptions));
        }

        YarConcurrentClient.loop(new callback());
        YarConcurrentClient.reset();
    }
}