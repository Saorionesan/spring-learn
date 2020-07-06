package rabbitMqTest.ps;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import rabbitMqTest.util.RabbitConnection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class PSSend {

    public static  final String exchangeName="test_exchange_fanout";


    public static void main(String[] args) throws IOException, TimeoutException {

        //获取链接
        Connection connection= RabbitConnection.getConnection();

        //获取channel
        Channel channel=connection.createChannel();

        //声明交换机 交换机类型为fanout，该类型会将消息发送到所有与该交换机绑定的queue上
        channel.exchangeDeclare(exchangeName,"fanout");

        //声明队列
        



    }
}
