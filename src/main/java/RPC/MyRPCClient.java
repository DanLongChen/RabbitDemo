package RPC;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

/**
 * Created by Paser on 2019/2/15.
 */
public class MyRPCClient {
    private static final String rpc_queue_name = "rpc_queue";
    public String reply_queue_name;
    public Connection connection;

    public static void main(String[] args) {
        MyRPCClient client=new MyRPCClient();
        try {
            for(int i=0;i<10;i++){
                System.out.println(client.call(String.valueOf(i)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            if(client.connection!=null){
                try {
                    client.connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String call(String message) throws IOException, TimeoutException, InterruptedException {

        connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        reply_queue_name = channel.queueDeclare().getQueue();
        String uuid = UUID.randomUUID().toString();
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().correlationId(uuid).replyTo(reply_queue_name).build();
        channel.basicPublish("", rpc_queue_name, properties, message.getBytes());
        BlockingQueue<String> blockingQueue=new ArrayBlockingQueue<String>(1);
        channel.basicConsume(reply_queue_name, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                super.handleDelivery(consumerTag, envelope, properties, body);
                if (properties.getCorrelationId().equals(uuid)) {
                    blockingQueue.offer(new String(body,"UTF-8"));
                }
            }
        });
        return blockingQueue.take();
    }
}
