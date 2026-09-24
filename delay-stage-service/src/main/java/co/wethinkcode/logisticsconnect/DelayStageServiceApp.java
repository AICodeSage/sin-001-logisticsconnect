package co.wethinkcode.logisticsconnect;

import io.javalin.Javalin;

public class DelayStageServiceApp {

    public static void main(String[] args) {
        DelayStageStore store = new DelayStageStore();
        DelayStagePublisher publisher = new DelayStagePublisher();
        Javalin app = Javalin.create().start(7052);

        app.get("/health", ctx -> ctx.result("OK"));
        app.get("/delay-stage/{hubId}", ctx -> {
            try { ctx.json(store.get(ctx.pathParam("hubId"))); }
            catch (IllegalArgumentException exception) { ctx.status(400).json(java.util.Map.of("error", exception.getMessage())); }
        });
        app.post("/delay-stage/{hubId}", ctx -> {
            try {
                DelayStageRequest request = ctx.bodyAsClass(DelayStageRequest.class);
                DelayStage updated = store.set(ctx.pathParam("hubId"), request.stage());
                publisher.publish(updated);
                ctx.status(200).json(updated);
            } catch (IllegalArgumentException exception) {
                ctx.status(400).json(java.util.Map.of("error", exception.getMessage()));
            } catch (IllegalStateException exception) {
                ctx.status(503).json(java.util.Map.of("error", "Stage saved but event broker is unavailable"));
            }
        });
    }
}

// MQ TODO: publishes to ActiveMQ topic MqConfig.TOPIC at MqConfig.BROKER_URL (see co.wethinkcode.logisticsconnect.mq.MqConfig)
