package rabbitMqTest.txTest;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import rabbitMqTest.util.RabbitConnection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 生产者消息确认机制
 * 使用事务机制进行确认
 */
public class TxSend {

    private static final String Queue_Name="tx_test_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection= RabbitConnection.getConnection();
        Channel channel=connection.createChannel();

        //声明队列
        channel.queueDeclare(Queue_Name,false,false,false,null);
        String msg="hello world";
        try{
            //开启事务模式
            channel.txSelect();
            //发送数据到队列中
            channel.basicPublish("",Queue_Name,null,msg.getBytes());
            //提交事务
            channel.txCommit();
        }catch (Exception e){
            //回滚
            channel.txRollback();
            System.out.println(e.getMessage());
        }finally {
            channel.close();
            connection.close();
        }
    }
}
