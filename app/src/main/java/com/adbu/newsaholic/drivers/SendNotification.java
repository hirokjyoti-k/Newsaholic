package com.adbu.newsaholic.drivers;

import android.os.AsyncTask;

import com.kwabenaberko.newsapilib.models.Article;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class SendNotification extends AsyncTask<Article, Void, Void>{

    private Article article;
    private String source;

    public SendNotification(String source){
        this.source = source;
    }

    @Override
    protected Void doInBackground(Article... articles) {
        for (Article a : articles){
            article = a;
        }
        try{
            String jsonResponse;
            URL url = new URL("https://onesignal.com/api/v1/notifications");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);

            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Authorization", "Basic OTc1OWY4N2ItMzg5MC00ZGUwLWJkODYtNTU3NjQ2NTk2Njk1");
            con.setRequestMethod("POST");

            String strJsonBody = "{\r\n"
                    + "  \"app_id\": \"99a92059-449e-462e-9f2e-a86993ba166a\",\r\n"
                    + "  \"included_segments\": [\"Subscribed Users\"],\r\n"
                    + "  \"headings\" :{\"en\": \""+source+" \"},\r\n"
                    + "  \"contents\": {\"en\": \""+article.getTitle()+"\"},\r\n"
                    + "  \"url\" : \""+article.getUrl()+"\",\r\n"
                    + "  \"big_picture\": \""+article.getUrlToImage()+"\"\r\n"
                    + "}";

            System.out.println("strJsonBody:\n" + strJsonBody);

            byte[] sendBytes = strJsonBody.getBytes("UTF-8");
            con.setFixedLengthStreamingMode(sendBytes.length);

            OutputStream outputStream = con.getOutputStream();
            outputStream.write(sendBytes);

            int httpResponse = con.getResponseCode();
            System.out.println("httpResponse: " + httpResponse);

            if (  httpResponse >= HttpURLConnection.HTTP_OK
                    && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                scanner.close();
            }
            else {
                Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                scanner.close();
            }
            System.out.println("jsonResponse:\n" + jsonResponse);
        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }
}
