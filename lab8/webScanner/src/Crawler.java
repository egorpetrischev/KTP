import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Crawler {
    private static LinkedList<UrlDepthPair> urls = new LinkedList<>();
    private static HashMap<String, UrlDepthPair> visitedUrls = new HashMap<>();
    private static int maxDepth;
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java Crawler <URL> <depth>");
            return;
        }

        String startUrl = args[0];
        maxDepth = Integer.parseInt(args[1]);

        urls.add(new UrlDepthPair(startUrl, 0));

        while (!urls.isEmpty()) {
            UrlDepthPair pair = urls.pop();
            String url = pair.getUrl();
            int depth = pair.getDepth();
            System.out.println(pair);

            if (depth >= maxDepth) {
                continue;
            }

            try {
                URL urlObj = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                String html = response.toString();
                Pattern pattern = Pattern.compile("<a href=\"(http[^\"]+)\"");
                Matcher matcher = pattern.matcher(html);
                while (matcher.find()) {
                    String newUrl = matcher.group(1);

                    if (visitedUrls.containsKey(newUrl)) continue;

                    UrlDepthPair newUrlPair = new UrlDepthPair(newUrl, depth + 1);
                    urls.add(newUrlPair);
                    visitedUrls.put(newUrl, newUrlPair);
                }
            } catch (Exception e) {
                System.out.println("Error connecting to " + url);
            }
        }
    }
}
