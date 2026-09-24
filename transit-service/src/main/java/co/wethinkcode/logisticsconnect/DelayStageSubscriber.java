package co.wethinkcode.logisticsconnect;

import com.fasterxml.jackson.databind.ObjectMapper;
import co.wethinkcode.logisticsconnect.mq.MqConfig;
import javax.jms.Connection;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;

/** Starts a durable process-local consumer for delay-stage events. */
public final class DelayStageSubscriber {
    private final ObjectMapper mapper = new ObjectMapper();

    public void start(DelayStageCache cache) {
        Thread listener = new Thread(() -> consume(cache), "delay-stage-topic-listener");
        listener.setDaemon(true);
        listener.start();
    }

    private void consume(DelayStageCache cache) {
        try {
            Connection connection = new ActiveMQConnectionFactory(MqConfig.BROKER_URL).createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            session.createConsumer(session.createTopic(MqConfig.TOPIC)).setMessageListener(message -> update(cache, message));
            connection.start();
        } catch (Exception exception) {
            System.err.println("Delay-stage subscriber unavailable: " + exception.getMessage());
        }
    }

    private void update(DelayStageCache cache, Message message) {
        if (message instanceof TextMessage textMessage) {
            try { cache.update(mapper.readValue(textMessage.getText(), DelayStageEvent.class)); }
            catch (Exception exception) { System.err.println("Ignoring invalid delay event: " + exception.getMessage()); }
        }
    }
}
