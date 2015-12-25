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
    static String RewardScoreServiceUri = "http://mengkang.net/demo/yar-server/RewardScoreService.php";

    public void testUserService(){
        YarClient yarClient  = new YarClient(RewardScoreServiceUri);
        RewardScoreService rewardScoreService = (RewardScoreService) yarClient.useService(RewardScoreService.class);
        for (int i = 0; i < 10; i++) {
            System.out.println(rewardScoreService.support(1, 2));
            System.out.println(rewardScoreService.post(1, 20));
        }
    }

}