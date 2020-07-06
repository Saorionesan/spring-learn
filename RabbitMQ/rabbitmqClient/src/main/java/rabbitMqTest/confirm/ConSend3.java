package rabbitMqTest.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import rabbitMqTest.util.RabbitConnection;

import java.io.IOException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeoutException;

public class ConSend3 {
    private static final String Queue_Name="con_test_queue3";
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection= RabbitConnection.getConnection();
        Channel channel=connection.createChannel();

        //声明队列
        channel.queueDeclare(Queue_Name,false,false,false,null);
        String msg="hello world";

        //将channel设置为confirm模式
        channel.confirmSelect();

        //存放未确认的消息的ID号(标识)
         final SortedSet<Long> confirmSet= Collections.synchronizedSortedSet(new TreeSet<Long>());

         //增加监听 在生产者成功发送消息时则调用handleAck 失败时调用handleNack
         channel.addConfirmListener(new ConfirmListener() {
             // rabbitmq正确的收到消息 返回ack
             /**
              *
              * @param l 为deliveryTag，当前Chanel发出的消息序号
              * @param b 代表multiple，如果为true则从unconfirm集合（即上面的confirmSet集合）删除多条记录
              * multiple为true 说明返回了多条消息序号
              * @throws IOException
              *在发送消息时需要往confirmSet中增加序号,在rabbitmq成功接收到消息时将confirmSet中的未确认的消息ID号remove掉
              */
             public void handleAck(long l, boolean b) throws IOException {
             if(b){
                 System.out.println("multiple为true");
                 confirmSet.headSet(l+1).clear();
             }else {
                 //不一次性删除多条记录,只移除当前消息序号
                 System.out.println("multiple为false");
                 confirmSet.remove(l);
             }
             }
             //rabbitmq 未正确收到消息 返回nack
             //发送消息失败时将调用该方法，此时可以在此处进行处理
             public void handleNack(long l, boolean b) throws IOException {
                 System.out.println("未正确发送消息");
             }
         });
         //获取发送的消息序号
        while(true){
            long seqNo=channel.getNextPublishSeqNo();
            channel.basicPublish("",Queue_Name,null,msg.getBytes());
            //将集合中未确认的消息数加1
            confirmSet.add(seqNo);
        }
    }
}
