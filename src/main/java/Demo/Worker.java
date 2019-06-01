package Demo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;

/**
 * Created by Paser on 2019/2/15.
 */
public class Worker implements Runnable{

    @Override
    public void run() {
        try {
            Connection connection=ConnectionUtil.getConnection();
            Channel channel=connection.createChannel();
            channel.basicConsume("test_queue1",true,new Consumer1());
            channel.basicConsume("test_queue2",true,new Consumer2());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
