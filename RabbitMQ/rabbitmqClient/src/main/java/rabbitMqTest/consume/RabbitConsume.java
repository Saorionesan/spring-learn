package rabbitMqTest.consume;

import com.rabbitmq.client.*;
import rabbitMqTest.util.RabbitConnection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消费者
 */
public class RabbitConsume {

    private static final String Queue_Name="test_queue";

    //获取消息
    public static void main(String[] args) throws IOException, TimeoutException {
        //获取连接
        Connection connection= RabbitConnection.getConnection();

        //创建管道
        final Channel channel=connection.createChannel();

        /**
         * 设置客户端最多接收未被 ack 的消息的个数
         */
        channel.basicQos(1);

        //定义队列消费者 匿名内部类
        /**
         * 一旦有消息发送 其就会进行接收
         */
        DefaultConsumer consumer= new DefaultConsumer(channel){
            //一旦监听到消息会调用该方法进行接收
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg=new String(body,"utf-8");
                System.out.println("获取到消息:"+msg);
                //进行手动回复消息
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        };
        /**
         * 消费者端的自动应答改成false
         */
       //消费者绑定队列
        //绑定该队列时 其会一直阻塞在这 有消息就会调用handleDelivery方法进行处理
        //类似于ServerSocket 中的accept()方法 如果未获取到连接其会一直阻塞
        //关于线程状态此处 https://www.cnblogs.com/aspirant/p/8900276.html
        //自动确认改为手动确认
        // ack 为消息应答
        channel.basicConsume(Queue_Name,false,consumer);
    }
}
