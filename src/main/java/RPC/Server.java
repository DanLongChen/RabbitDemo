package RPC;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Paser on 2019/2/15.
 */
public class Server {
    private static final String RPC_QUEUE_NAME = "rpc_queue";

    //具体处理方（斐波那契额计算函数）
    private static int fib(int n) {
        if (n == 0)
            return 0;
        if (n == 1)
            return 1;
        return fib(n - 1) + fib(n - 2);
    }

    public static void main(String[] argv) {
        //建立连接、通道，并声明队列
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setVirtualHost("/");
        factory.setUsername("rpc");
        factory.setPassword("rpc");

        Connection connection = null;
        try {
            connection = factory.newConnection();
            //创建通道
            final Channel channel = connection.createChannel();
            //定义队列
            channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);
            //会告诉RabbitMQ不要同时给一个消费者推送多于N个消息，即一旦有N个消息还没有ack，则该consumer将block掉，直到有消息ack
            channel.basicQos(1);

            System.out.println(" [x] Awaiting RPC requests");

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                           byte[] body) throws IOException {
                    AMQP.BasicProperties replyProps = new AMQP.BasicProperties.Builder()
                            .correlationId(properties.getCorrelationId()).build();

                    String response = "";

                    try {
                        String message = new String(body, "UTF-8");
                        int n = Integer.parseInt(message);

                        System.out.println(" [.] fib(" + message + ")");
                        response += fib(n);
                    } catch (RuntimeException e) {
                        System.out.println(" [.] " + e.toString());
                    } finally {
                        // 返回处理结果队列
                        channel.basicPublish("", properties.getReplyTo(), replyProps, response.getBytes("UTF-8"));
                        //  确认消息，已经收到后面参数 multiple：是否批量.true:将一次性确认所有小于envelope.getDeliveryTag()的消息。
                        channel.basicAck(envelope.getDeliveryTag(), false);
                        // RabbitMq consumer worker thread notifies the RPC
                        // server owner thread
                        synchronized (this) {
                            this.notify();
                        }
                    }
                }
            };
            //取消自动确认
            boolean autoAck = false ;
            channel.basicConsume(RPC_QUEUE_NAME, autoAck, consumer);
            // Wait and be prepared to consume the message from RPC client.
            while (true) {
                synchronized (consumer) {
                    try {
                        consumer.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                try {
                    connection.close();
                } catch (IOException _ignore) {
                }
        }
    }
}
