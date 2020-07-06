package rabbitMqTest.txTest;

import com.rabbitmq.client.*;
import rabbitMqTest.util.RabbitConnection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class TxConsume {
    private static final String Queue_Name="tx_test_queue";
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection= RabbitConnection.getConnection();
        Channel channel=connection.createChannel();

        //消费消息 监听哪一个队列进行消息消费
        channel.basicConsume(Queue_Name,true,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("收到了消息:"+new String(body,"utf-8"));
            }
        });
    }
}
