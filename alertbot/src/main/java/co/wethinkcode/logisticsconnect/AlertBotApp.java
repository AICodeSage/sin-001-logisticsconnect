package co.wethinkcode.logisticsconnect;

import io.javalin.Javalin;

public class AlertBotApp {

    public static void main(String[] args) {
        AlertService alerts = new AlertService();
        new AlertSubscriber().start(alerts);
        Javalin app = Javalin.create().start(7054);

        app.get("/health", ctx -> ctx.result("OK"));
        app.get("/alerts", ctx -> ctx.json(alerts.all()));
        app.get("/alerts/threshold", ctx -> ctx.json(java.util.Map.of("stage", AlertService.ALERT_THRESHOLD)));
    }
}
