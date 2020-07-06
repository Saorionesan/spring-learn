package rabbitMqTest.send;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import rabbitMqTest.util.RabbitConnection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 生产者
 */
public class RabbitSend {
    private static final String Queue_Name="test_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection= RabbitConnection.getConnection();

        /**
         * 从Tcp 连接中获取一个通道,
         *如果每一次访问RabbitMQ都建立一个Connection，在消息量大的时候建立TCP Connection的开销将是巨大的，效率也较低。Channel是在connection内部建立的逻辑连接，如果应用程序支持多线程，通常每个thread创建单独的channel进行通讯，AMQP method包含了channel id帮助客户端和message broker识别channel，所以channel之间是完全隔离的。
         * Channel作为轻量级的Connection极大减少了操作系统建立TCP connection的开销。
         */
        Channel channel=connection.createChannel();

        //是否开启消息持久化功能
        boolean durable=false;
        //声明队列
        channel.queueDeclare(Queue_Name,durable,false,false,null);

        String msg="hello world";

        /**
         * 每个消费者 发送确认消息之前，消息队列不发送下一个消息到消费者,一次只处理一个消息
         * 限制发送给同一个消费者不得超过一条
         */
        channel.basicQos(1);
        //发送消息
        channel.basicPublish("",Queue_Name, MessageProperties.PERSISTENT_TEXT_PLAIN,msg.getBytes());
        //关闭管道
       channel.close();
       connection.close();
    }

}
