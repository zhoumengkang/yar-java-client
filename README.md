# 简介
Yar 是一个轻量级, 高效的 RPC 框架, 它提供了一种简单方法来让 PHP 项目之间可以互相远程调用对方的本地方法. 并且 Yar 也提供了并行调用的能力. 可以支持同时调用多个远程服务的方法.

Yar 鸟哥博客介绍 http://www.laruence.com/2012/09/15/2779.html

Yar 鸟哥原始项目 https://github.com/laruence/yar

Yar Java Client 则实现了跨语言的远程调用。使得 Java 客户端能够调用 Yar PHP 服务器端本地的方法。

# 特性

1. 执行速度快，依旧保持鸟哥初衷，框架轻，使用简单

2. 支持并行的 RPC 调用

3. 方法的使用和参数的和 PHP 版本保持一致

# 范例

PHP服务器端
---
提供了两个 rpc api ，模拟的业务场景是点赞赠送金币和发布帖子赠送金币。

```php
<?php
 
class RewardScoreService {
    /**
     * $uid 给 $fid 点赞
     * @param $fid  interge
     * @param $uid  interge
     * @return void
     */
    public function support($uid,$fid){
        return "support:uid:$uid:fid:$fid";
    }
 
    /**
     * $uid 发布了帖子 $fid 
     * @param $fid  interge
     * @param $uid  interge
     * @return void
     */
    public function post($uid,$fid){
        return "post:uid:$uid:fid:$fid";
    }
}
 
$yar_server = new Yar_server(new RewardScoreService());
$yar_server->handle();
```

Java客户端同步调用这两个服务
---
```java
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
        System.out.println(rewardScoreService.support(1, 2));
        
        // 第二种调用方式
        YarClientOptions yarClientOptions = new YarClientOptions();
        yarClientOptions.setConnect_timeout(2000);
        
        YarClient yarClient2  = new YarClient(uri,yarClientOptions);
        RewardScoreService rewardScoreService2 = (RewardScoreService) yarClient2.useService(RewardScoreService.class);
        System.out.println(rewardScoreService2.post(1, 20));
    }

}
```

Java客户端并行调用这两个服务
---

这里的方法的命令皆以 Yar 原版为准则。

`YarConcurrentClient.call`方法注册，

`YarConcurrentClient.loop`并行调用，

`YarConcurrentClient.reset`清空任务。

回调函数需要继承实现`YarConcurrentCallback`里面定义了两个方法：`async`是针对并行调用发出之后立即执行的任务，而`success`则是每个请求之后返回的结果。
```java
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

        // 第一种调用方式
        YarConcurrentClient.call(new YarConcurrentTask(uri, "support", new Object[]{1, 2}, packagerName, new callback()));
        
        // 第二种调用方式 增加一些额外配置选项
        YarConcurrentClient.call(new YarConcurrentTask(uri, "support", new Object[]{1, 2}, packagerName, new callback(),yarClientOptions));

        // 第三种调用方式 有正确的回调和错误的回调
        YarConcurrentClient.call(new YarConcurrentTask(uri,"post",new Object[]{1,2},packagerName,new callback(),new errorCallback()));
        
        // 第四种调用方式 在第三种的基础上增加额外的配置选项
        YarConcurrentClient.call(new YarConcurrentTask(uri,"post",new Object[]{1,2},packagerName,new callback(),new errorCallback(),yarClientOptions));

        YarConcurrentClient.loop(new callback());
        YarConcurrentClient.reset();
    }
}
```
# 详细说明
更多详细的说明，请查看 [wiki](https://github.com/zhoumengkang/yar-java-client/wiki)
有任何使用的问题和技术交流，欢迎使用 [issues](https://github.com/zhoumengkang/yar-java-client/issues)
