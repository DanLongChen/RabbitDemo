package Demo;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

import java.io.IOException;

/**
 * Created by Paser on 2019/2/15.
 */
public class Consumer1 implements Consumer{
    //当consumer注册成功的时候调用
    @Override
    public void handleConsumeOk(String consumerTag) {
        System.out.println("Consumer "+consumerTag+" is register");
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
    //  当消费者恢复的时候调用
    @Override
    public void handleRecoverOk(String consumerTag) {

    }
    //  当接受到消息的时候调用
    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        HandelMessage.Print("Consumer1",new String(body));
    }
}
