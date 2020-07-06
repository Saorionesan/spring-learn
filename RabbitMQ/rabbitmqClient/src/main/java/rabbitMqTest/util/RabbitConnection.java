package rabbitMqTest.util;


import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * rabbitMq 工具类
 */
public class RabbitConnection {

    private static final  String host="47.103.2.6";
    private static final  int port=5672;
    private static final String username="ysk";
    private static final String password="19971001";
    //设置虚拟主机
    /**
     * mq 为了使用多租户，设置了一个vhost的概念 类似于myqsl中的数据库的概念
     */
    private static final String vhost="/";
    //获取rabbitmq连接
    public static Connection getConnection() throws IOException, TimeoutException {
        //定义连接工厂
        ConnectionFactory connectionFactory=new ConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(vhost);
        return connectionFactory.newConnection();
    }
}
