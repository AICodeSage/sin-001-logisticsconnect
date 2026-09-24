package co.wethinkcode.logisticsconnect;

import io.javalin.Javalin;

public class TransitServiceApp {

    public static void main(String[] args) {
        DelayStageCache stages = new DelayStageCache();
        new DelayStageSubscriber().start(stages);
        HubClient hubs = new HubClient();
        EtaCalculator calculator = new EtaCalculator();
        Javalin app = Javalin.create().start(7053);

        app.get("/health", ctx -> ctx.result("OK"));
        app.get("/eta/{hubId}", ctx -> {
            try {
                Hub hub = hubs.find(ctx.pathParam("hubId"));
                if (hub == null) {
                    ctx.status(404).json(java.util.Map.of("error", "Hub not found"));
                } else if (!hub.active()) {
                    ctx.status(409).json(java.util.Map.of("error", "Hub is inactive"));
                } else {
                    ctx.json(calculator.calculate(hub, stages.stageFor(hub.hubId())));
                }
            } catch (Exception exception) {
                ctx.status(503).json(java.util.Map.of("error", "Hub service unavailable"));
            }
        });
    }
}

// MQ TODO: subscribes to ActiveMQ topic MqConfig.TOPIC at MqConfig.BROKER_URL (see co.wethinkcode.logisticsconnect.mq.MqConfig)
