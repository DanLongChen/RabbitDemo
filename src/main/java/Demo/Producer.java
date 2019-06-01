package Demo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Paser on 2019/2/14.
 */
public class Producer {
    private static final String queue_name1 = "test_queue1";
    private static final String queue_name2 = "test_queue2";

    public static void main(String[] args) throws IOException, TimeoutException {
        //获取连接（相当于一个TCP连接）
        Connection connection=ConnectionUtil.getConnection();
        //从连接中创建通道
        Channel channel=connection.createChannel();
        channel.exchangeDeclare("MyExchange","fanout",false,false,null);
        //队列声明（name，durable，exclusive，autoDelete，其他参数）
        channel.queueDeclare(queue_name1,false,false,false,null);
        channel.queueDeclare(queue_name2,false,false,false,null);
        //绑定队列到交换机
        channel.queueBind(queue_name1,"MyExchange","test_queue");
        channel.queueBind(queue_name2,"MyExchange","test_queue");
        //发布消息（交换器名称，路由键，其他参数，消息）
        for(int i=0;i<10;i++){
            //在fanout交换器的情况下，消息会被广播到所有绑定的队列
            channel.basicPublish("MyExchange","",null,("Hello World "+i).getBytes());
        }
        System.out.println("Producer send message ");

        channel.close();
        connection.close();


    }

}
