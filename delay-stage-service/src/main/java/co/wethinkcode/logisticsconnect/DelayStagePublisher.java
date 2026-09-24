package co.wethinkcode.logisticsconnect;

import com.fasterxml.jackson.databind.ObjectMapper;
import co.wethinkcode.logisticsconnect.mq.MqConfig;
import javax.jms.Connection;
import javax.jms.Session;
import org.apache.activemq.ActiveMQConnectionFactory;

/** Publishes stage changes to the shared topic. */
public final class DelayStagePublisher {
    private final ObjectMapper mapper = new ObjectMapper();

    public void publish(DelayStage stage) {
        DelayStageEvent event = new DelayStageEvent(stage.hubId(), stage.stage(), java.time.Instant.now().toString());
        try (Connection connection = new ActiveMQConnectionFactory(MqConfig.BROKER_URL).createConnection()) {
            connection.start();
            try (Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)) {
                session.createProducer(session.createTopic(MqConfig.TOPIC))
                        .send(session.createTextMessage(mapper.writeValueAsString(event)));
            }
        } catch (Exception exception) {
            throw new IllegalStateException("Could not publish delay-stage event", exception);
        }
    }
}
