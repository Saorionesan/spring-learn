package rabbitMqTest.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import rabbitMqTest.util.RabbitConnection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * confirm 模式生产者
 * 注意 如果当前队列已被设置为事务模式 那么不可再进行设置
 */
public class ConSend {

    private static final String Queue_Name="con_test_queue";
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection= RabbitConnection.getConnection();
        Channel channel=connection.createChannel();

        //声明队列
        channel.queueDeclare(Queue_Name,false,false,false,null);
        String msg="hello world";

        //将channel设置为confirm模式
        channel.confirmSelect();
        //发送消息
        channel.basicPublish("",Queue_Name,null,msg.getBytes());

        //查看confirm模式的返回结果
        //注意在该模式下 发送消息为串行，即同步。每发一条消息都要等待rabbitmq的确认返回
        if(!channel.waitForConfirms()){
            System.out.println("消息发送失败");
        }else {
            System.out.println("消息发送成功");
        }
        channel.close();
        connection.close();
    }
}
