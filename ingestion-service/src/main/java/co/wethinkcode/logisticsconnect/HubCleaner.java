package co.wethinkcode.logisticsconnect;

import java.util.Locale;
import java.util.Optional;

/** Normalizes individual CSV values before validation and deduplication. */
public final class HubCleaner {
    private HubCleaner() { }

    public static String text(String value) {
        if (value == null) return "";
        String cleaned = value.trim().replaceAll("\\s+", " ");
        return isPlaceholder(cleaned) ? "" : cleaned;
    }

    public static String hubId(String value) {
        String cleaned = text(value).toUpperCase(Locale.ROOT).replaceAll("\\s+", "");
        return cleaned.matches("H-\\d+") ? cleaned : "";
    }

    public static String placeName(String value) {
        String cleaned = text(value);
        if (cleaned.isEmpty()) return "";
        return switch (cleaned.toLowerCase(Locale.ROOT)) {
            case "kwa-zulu natal", "kwazulu natal" -> "KwaZulu-Natal";
            case "port elizabeth" -> "Gqeberha";
            default -> titleCase(cleaned);
        };
    }

    public static Optional<Boolean> active(String value) {
        return switch (text(value).toLowerCase(Locale.ROOT)) {
            case "y", "yes", "1", "true" -> Optional.of(true);
            case "n", "no", "0", "false" -> Optional.of(false);
            default -> Optional.empty();
        };
    }

    private static boolean isPlaceholder(String value) {
        return switch (value.toLowerCase(Locale.ROOT)) {
            case "", "n/a", "na", "tbd", "unknown", "-", "nan" -> true;
            default -> false;
        };
    }

    private static String titleCase(String value) {
        return String.join(" ", value.toLowerCase(Locale.ROOT).split(" ")).isEmpty() ? "" :
                java.util.Arrays.stream(value.toLowerCase(Locale.ROOT).split(" "))
                        .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1))
                        .reduce((left, right) -> left + " " + right).orElse("");
    }
}
