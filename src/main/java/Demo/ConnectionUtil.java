package Demo;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Paser on 2019/2/14.
 */
public class ConnectionUtil {
    public static Connection getConnection(){
        Connection connection=null;
        try {
            ConnectionFactory factory=new ConnectionFactory();
            factory.setHost("localhost");
            factory.setUsername("cdl");
            factory.setPassword("2013cdlhh");
            factory.setVirtualHost("/");
            connection=factory.newConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
