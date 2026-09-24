package co.wethinkcode.logisticsconnect;

import io.javalin.Javalin;

public class IngestionServiceApp {

    public static void main(String[] args) {
        HubRepository hubs = new HubRepository();
        Javalin app = Javalin.create().start(7050);

        app.get("/health", ctx -> ctx.result("OK"));
        app.get("/hubs", ctx -> ctx.json(hubs.all()));
        app.get("/hubs/{hubId}", ctx -> {
            HubRecord hub = hubs.findById(ctx.pathParam("hubId"));
            if (hub == null) {
                ctx.status(404).json(java.util.Map.of("error", "Hub not found"));
            } else {
                ctx.json(hub);
            }
        });
    }
}
