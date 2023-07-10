import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ApiThreads {

    private static String parse(String elements){

        JSONArray jsonArray = new JSONArray(elements);
        int numThreads = 10; // Number of threads to use
        int totalAPICalls = jsonArray.length();


        // Creating a thread pool with the specified number of threads
        //ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(numThreads);


        for(int i=0;i<totalAPICalls;i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String total = "";
            int id = jsonObject.getInt("id");
            int albumId = jsonObject.getInt("albumId");
            String title = jsonObject.getString("title");

            total += "id:"+id + " ,albumId:" + albumId + " ,title:"+title;

            Runnable task = new APICallTask(total); // new task for each API call
            //executor.submit(task);
            executor.scheduleAtFixedRate(task, 0, 1000 / 60, TimeUnit.MILLISECONDS);

            // since our target is to generate around 600 API calls in 10 seconds so the
            // executor.scheduleAtFixedRate() function will produce the desired output
            // at constant rate. But if we use execute.submit() function it will do all
            // 5000 API calls
        }

        /*

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */

        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) {
        HttpURLConnection httpURLConnection = null;
        BufferedReader reader = null;
        String line = null;
        StringBuffer contents = new StringBuffer();
        try {
            URL url = new URL("https://jsonplaceholder.typicode.com/photos"); // containing 5000 data in json format
            httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(4000);
            httpURLConnection.setReadTimeout(4000);
            int response_code = httpURLConnection.getResponseCode();
            System.out.println(response_code);

            if(response_code > 299){
                reader = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
                while ((line = reader.readLine()) != null){
                    contents.append(line);
                }
                reader.close();
            }
            else{
                reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                while ((line = reader.readLine()) != null){
                    contents.append(line);
                }
                reader.close();
                ApiThreads.parse(contents.toString());
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpURLConnection.disconnect();
        }
    }
}
