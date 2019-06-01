package Cluster;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Paser on 2019/2/16.
 */
//集群感知客户端，首先连接到负载均衡器，然后再连接到集群
public class Client {
    public static void main(String[] args) {
        Connection connection = null;
        Channel channel = null;
        while (true) {//如果消费者消费消息没有出错则会一直消费消息，如果出错则会跳到异常处理，然后再跳到外循环
            try {
                connection = ConnectionUtil.getConnection();
                channel = connection.createChannel();
                channel.queueDeclare("queue", false, false, false, null);
                channel.exchangeDeclare("cluster_test", "direct", false, false, null);
                channel.queueBind("queue", "cluster_test", "cluster_test");
                channel.basicConsume("queue", true, "client", new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                        super.handleDelivery(consumerTag, envelope, properties, body);
                        System.out.println("client receive message from server " + new String(body, "UTF-8"));
//                        channel.basicAck(envelope.getDeliveryTag(), false);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (channel != null) {
                    try {
                        channel.abort();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
        }
    }
}
