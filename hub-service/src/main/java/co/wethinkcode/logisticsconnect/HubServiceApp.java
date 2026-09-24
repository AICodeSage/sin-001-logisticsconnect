package co.wethinkcode.logisticsconnect;

import io.javalin.Javalin;

public class HubServiceApp {

    public static void main(String[] args) {
        HubDirectory directory = new HubDirectory(new IngestionClient());
        Javalin app = Javalin.create().start(7051);

        app.get("/health", ctx -> ctx.result("OK"));
        app.get("/hubs", ctx -> respond(ctx, () -> ctx.json(directory.all())));
        app.get("/hubs/{hubId}", ctx -> respond(ctx, () -> {
            Hub hub = directory.find(ctx.pathParam("hubId"));
            if (hub == null) ctx.status(404).json(java.util.Map.of("error", "Hub not found"));
            else ctx.json(hub);
        }));
        app.get("/provinces", ctx -> respond(ctx, () -> ctx.json(directory.all().stream()
                .map(Hub::province).distinct().sorted().toList())));
    }

    private static void respond(io.javalin.http.Context ctx, ThrowingAction action) {
        try { action.run(); }
        catch (Exception exception) { ctx.status(503).json(java.util.Map.of("error", "Ingestion service unavailable")); }
    }

    @FunctionalInterface
    private interface ThrowingAction { void run() throws Exception; }
}
