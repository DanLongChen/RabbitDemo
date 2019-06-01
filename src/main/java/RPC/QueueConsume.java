package RPC;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * Created by Paser on 2019/2/15.
 */
public class QueueConsume extends DefaultConsumer{
    /**
     * Constructs a new instance and records its association to the passed-in channel.
     *
     * @param channel the channel to which this consumer is attached
     */
    public QueueConsume(Channel channel) {
        super(channel);
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        super.handleDelivery(consumerTag, envelope, properties, body);
        System.out.println("Server receive message "+new String(body));
        String routeKey=envelope.getRoutingKey();
        String contentType=properties.getContentType();
        Long deliveryTag=envelope.getDeliveryTag();
        getChannel().basicAck(deliveryTag,false);
        getChannel().basicPublish("server",routeKey,null,("Pong "+System.currentTimeMillis()).getBytes());
    }
}
