package ma.fstt.ecommerce.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class ProductImageGenerator {

    private ProductImageGenerator() {
    }

    public static String createSvgDataUri(String type, String badgeText, String accentStart, String accentEnd) {
        String iconMarkup;
        switch (type == null ? "" : type.toLowerCase()) {
            case "laptop":
                iconMarkup = "<rect x='110' y='72' width='180' height='114' rx='10' fill='#0f172a' stroke='#e2e8f0' stroke-width='4'/>"
                        + "<rect x='122' y='84' width='156' height='90' rx='4' fill='url(#screenGrad)'/>"
                        + "<path d='M86 188 H314 L298 204 H102 Z' fill='#cbd5e1'/>"
                        + "<rect x='176' y='188' width='48' height='5' rx='2' fill='#64748b'/>";
                break;
            case "phone":
                iconMarkup = "<rect x='152' y='52' width='96' height='176' rx='18' fill='#0f172a' stroke='#e2e8f0' stroke-width='4'/>"
                        + "<rect x='161' y='66' width='78' height='146' rx='10' fill='url(#screenGrad)'/>"
                        + "<rect x='186' y='58' width='28' height='4' rx='2' fill='#64748b'/>"
                        + "<circle cx='200' cy='198' r='5' fill='rgba(255,255,255,0.5)'/>";
                break;
            case "gaming":
                iconMarkup = "<rect x='95' y='96' width='210' height='92' rx='42' fill='#0f172a' stroke='#e2e8f0' stroke-width='4'/>"
                        + "<path d='M145 124 V160 M127 142 H163' stroke='#38bdf8' stroke-width='8' stroke-linecap='round'/>"
                        + "<circle cx='252' cy='128' r='8' fill='#fb7185'/>"
                        + "<circle cx='272' cy='142' r='8' fill='#34d399'/>"
                        + "<circle cx='232' cy='142' r='8' fill='#fbbf24'/>"
                        + "<circle cx='252' cy='156' r='8' fill='#818cf8'/>";
                break;
            case "audio":
                iconMarkup = "<path d='M132 156 V132 A68 68 0 0 1 268 132 V156' fill='none' stroke='#e2e8f0' stroke-width='12' stroke-linecap='round'/>"
                        + "<rect x='114' y='138' width='34' height='62' rx='16' fill='#0f172a' stroke='#38bdf8' stroke-width='4'/>"
                        + "<rect x='252' y='138' width='34' height='62' rx='16' fill='#0f172a' stroke='#38bdf8' stroke-width='4'/>"
                        + "<circle cx='200' cy='150' r='20' fill='rgba(255,255,255,0.12)'/>";
                break;
            default:
                iconMarkup = "<rect x='116' y='78' width='168' height='120' rx='16' fill='#0f172a' stroke='#e2e8f0' stroke-width='4'/>"
                        + "<rect x='136' y='100' width='128' height='22' rx='6' fill='url(#screenGrad)'/>"
                        + "<circle cx='152' cy='156' r='8' fill='#34d399'/>"
                        + "<circle cx='178' cy='156' r='8' fill='#38bdf8'/>"
                        + "<rect x='204' y='150' width='56' height='12' rx='6' fill='#475569'/>";
                break;
        }

        String safeBadge = escapeXml(badgeText != null ? badgeText : "ATLASTECH");
        String svg = "<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 400 260'>"
                + "<defs>"
                + "<linearGradient id='bgGrad' x1='0%' y1='0%' x2='100%' y2='100%'>"
                + "<stop offset='0%' stop-color='" + accentStart + "'/>"
                + "<stop offset='100%' stop-color='" + accentEnd + "'/>"
                + "</linearGradient>"
                + "<linearGradient id='screenGrad' x1='0%' y1='0%' x2='100%' y2='100%'>"
                + "<stop offset='0%' stop-color='#38bdf8'/>"
                + "<stop offset='100%' stop-color='#6366f1'/>"
                + "</linearGradient>"
                + "</defs>"
                + "<rect width='400' height='260' fill='url(#bgGrad)'/>"
                + "<circle cx='340' cy='40' r='90' fill='rgba(255,255,255,0.08)'/>"
                + "<circle cx='50' cy='230' r='80' fill='rgba(255,255,255,0.06)'/>"
                + iconMarkup
                + "<rect x='120' y='218' width='160' height='26' rx='13' fill='rgba(15,23,42,0.55)'/>"
                + "<text x='200' y='235' font-family='Segoe UI, Arial, sans-serif' font-size='12' font-weight='700' fill='#ffffff' text-anchor='middle' letter-spacing='1'>"
                + safeBadge + "</text>"
                + "</svg>";

        String base64 = Base64.getEncoder().encodeToString(svg.getBytes(StandardCharsets.UTF_8));
        return "data:image/svg+xml;base64," + base64;
    }

    private static String escapeXml(String input) {
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}
