package rabbitMqTest.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import rabbitMqTest.util.RabbitConnection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消费者确认 批量发送。发送完之后再一次性进行确认
 */
public class ConSend2 {

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
        //批量发送  发送完之后再进行确认
        for (int i=0;i<10;i++){
            channel.basicPublish("",Queue_Name,null,msg.getBytes());
        }
        //查看confirm模式的返回结果
        //在该模式下 发送消息仍然为串行，即同步
        if(!channel.waitForConfirms()){
            System.out.println("消息发送失败");
        }else {
            System.out.println("消息发送成功");
        }
        channel.close();
        connection.close();
    }
}
