package Cluster;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Paser on 2019/2/16.
 */
public class ConnectionUtil {
    public static Connection getConnection() throws IOException, TimeoutException {
        ConnectionFactory factory=new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5671);
        factory.setVirtualHost("/");
        factory.setUsername("guest");
        factory.setPassword("guest");
        return factory.newConnection();
    }
}
