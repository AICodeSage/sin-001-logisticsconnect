package co.wethinkcode.logisticsconnect;

import com.fasterxml.jackson.databind.ObjectMapper;
import co.wethinkcode.logisticsconnect.mq.MqConfig;
import javax.jms.Connection;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;

/** Receives all stage-change events and forwards relevant ones to AlertService. */
public final class AlertSubscriber {
    private final ObjectMapper mapper = new ObjectMapper();

    public void start(AlertService alerts) {
        Thread listener = new Thread(() -> consume(alerts), "alert-topic-listener");
        listener.setDaemon(true);
        listener.start();
    }

    private void consume(AlertService alerts) {
        try {
            Connection connection = new ActiveMQConnectionFactory(MqConfig.BROKER_URL).createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            session.createConsumer(session.createTopic(MqConfig.TOPIC)).setMessageListener(message -> handle(alerts, message));
            connection.start();
        } catch (Exception exception) {
            System.err.println("Alert subscriber unavailable: " + exception.getMessage());
        }
    }

    private void handle(AlertService alerts, Message message) {
        if (message instanceof TextMessage text) {
            try { alerts.handle(mapper.readValue(text.getText(), DelayStageEvent.class)); }
            catch (Exception exception) { System.err.println("Ignoring invalid alert event: " + exception.getMessage()); }
        }
    }
}
