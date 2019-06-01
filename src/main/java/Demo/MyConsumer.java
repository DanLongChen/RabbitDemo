package Demo;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * Created by Paser on 2019/2/14.
 */
public class MyConsumer implements Runnable, Consumer {
    private final static String queue_name="test_queue";

    @Override
    public void run() {
        try {
            Connection connection=ConnectionUtil.getConnection();
            Channel channel=connection.createChannel();
            //订阅队列
            if(channel.isOpen()){
                channel.basicConsume(queue_name,true,"tag1",this);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Called when consumer is registered.
     */
    @Override
    public void handleConsumeOk(String consumerTag) {
        System.out.println("Myconsumer "+consumerTag+" registered");
    }

    @Override
    public void handleCancelOk(String consumerTag) {

    }

    @Override
    public void handleCancel(String consumerTag) throws IOException {

    }

    @Override
    public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {

    }

    @Override
    public void handleRecoverOk(String consumerTag) {

    }

    /**
     * Called when new message is available.
     */
    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        System.out.println("Consumer get "+new String(body));
    }
}
