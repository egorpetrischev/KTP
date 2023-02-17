import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Crawler {
    private static final LinkedList<UrlDepthPair> urls = new LinkedList<>();
    private static final HashMap<String, UrlDepthPair> visitedUrls = new HashMap<>();
    private static int maxDepth;
    private static boolean keepRunning = true;


    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java Crawler <URL> <depth> <numOfThreads>");
            return;
        }

        String startUrl = args[0];
        maxDepth = Integer.parseInt(args[1]);
        int maxThreads = Integer.parseInt(args[2]);

        urls.add(new UrlDepthPair(startUrl, 0));
        visitedUrls.put(startUrl, new UrlDepthPair(startUrl, 0));

        for (int i = 0; i < maxThreads; i++) {
            new Thread(new CrawlerTask()).start();
        }
    }
    private static class CrawlerTask implements Runnable {
        @Override
        public void run() {
            Object lock = this;
            while (keepRunning) {

                synchronized (urls) {
                    while (urls.isEmpty()) {
                        keepRunning = false;
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

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

                        if (!visitedUrls.containsKey(newUrl)) {
                            UrlDepthPair newUrlPair = new UrlDepthPair(newUrl, depth + 1);

                            synchronized (visitedUrls) {
                                visitedUrls.put(newUrl, newUrlPair);
                            }
                            synchronized (urls) {
                                urls.add(newUrlPair);
                            }
                            keepRunning = true;
                            notifyAll();
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error connecting to " + url);
                }
            }
        }
    }
}
