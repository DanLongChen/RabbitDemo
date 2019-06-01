package RPC;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

/**
 * Created by Paser on 2019/2/15.
 */
public class MyRPCServer {
    private static final String rpc_queue_name="rpc_queue";
    public static int fib(int num){
        if(num==0){
            return 0;
        }
        if(num==1){
            return 1;
        }
        return fib(num-1)+fib(num-2);
    }

    public static void main(String[] args) throws IOException {
        Connection connection= null;
        try {
            connection = ConnectionUtil.getConnection();
            Channel channel=connection.createChannel();
            HashMap<String,Object> map=new HashMap<>();
            map.put("x-ha-policy","all");
            channel.queueDeclare(rpc_queue_name,false,false,false,map);
            channel.basicQos(1);
            System.out.println("waiting Client Request");
            Consumer consumer=new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    super.handleDelivery(consumerTag, envelope, properties, body);
                    AMQP.BasicProperties basicProperties = new AMQP.BasicProperties().builder().correlationId(properties.getCorrelationId()).build();
                    String response = "";
                    try {
                        String message = new String(body, "UTF-8");
                        System.out.println("Server get " + message + " netx to compute Fib(" + message + ")");
                        int request = Integer.parseInt(message);
                        response += fib(request);
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    } finally {
                        channel.basicPublish("", properties.getReplyTo(), basicProperties, ("The anwser is:" + response).getBytes());
                        channel.basicAck(envelope.getDeliveryTag(), false);
                        synchronized(this){
                            this.notify();
                        }
                    }
                }
            };
            channel.basicConsume(rpc_queue_name,false,consumer);
            while(true){
                synchronized (consumer){
                    try {
                        consumer.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally {

                    }
                }
            }
        } catch (TimeoutException e) {
            e.printStackTrace();
        }finally {
            if(connection!=null){
                connection.close();
            }
        }

    }
}
