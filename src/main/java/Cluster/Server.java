package Cluster;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Paser on 2019/2/16.
 */
public class Server {
    public static void main(String[] args) {
        try {
            Connection connection=ConnectionUtil.getConnection();
            Channel channel=connection.createChannel();
            channel.basicPublish("cluster_test","cluster_test",null,"message from server 9".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return;
    }
}
