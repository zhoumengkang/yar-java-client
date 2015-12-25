package net.mengkang.yar.client;

import junit.framework.TestCase;

/**
 * Created by zhoumengkang on 25/12/15.
 */
public class YarClientTest extends TestCase {
    /**
     * 定义 rpc 接口
     */
    public interface RewardScoreService{
        String support(int uid,int fid);
        String post(int uid,int fid);
    }

    /**
     * rpc api 地址
     */
    static String uri = "http://mengkang.net/demo/yar-server/RewardScoreService.php";

    public void testUserService(){
        // 第一种调用方式
        YarClient yarClient  = new YarClient(uri);
        RewardScoreService rewardScoreService = (RewardScoreService) yarClient.useService(RewardScoreService.class);
        for (int i = 0; i < 10; i++) {
            System.out.println(rewardScoreService.support(1, 2));
        }
        // 第二种调用方式
        YarClientOptions yarClientOptions = new YarClientOptions();
        yarClientOptions.setConnect_timeout(2000);
        YarClient yarClient2  = new YarClient(uri,yarClientOptions);
        RewardScoreService rewardScoreService2 = (RewardScoreService) yarClient2.useService(RewardScoreService.class);
        for (int i = 0; i < 10; i++) {
            System.out.println(rewardScoreService2.post(1, 20));
        }
    }

}