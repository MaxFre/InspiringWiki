package com.example.mafr.p3;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CountDownLatch;

/**
 * Created by AMIN HARIRCHIAN on 2018/10/22 .
 */

public class QuoteBuilder {

  /*Metoden Get a new quote from Forismatic API and send a text and the author */
  public Quote getQuote() {
    final String[] text = new String[1];
    final String[] Author = new String[1];
    final CountDownLatch latch = new CountDownLatch(1);
    new Thread(new Runnable() {
      public void run() {
        try {
          String query = "https://api.forismatic.com/api/1.0/?method=getQuote&key=15456&format=json&json=showQuote&lang=en";
          URL url = new URL(query);
          HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
          InputStream in = new BufferedInputStream(urlConnection.getInputStream());
          BufferedReader reader = new BufferedReader(new InputStreamReader(in));
          String line;
          StringBuilder sb = new StringBuilder();
          while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
          }
          String test = sb.substring(sb.indexOf("quoteText\":\"")+12, sb.indexOf("\"quoteAuthor")-4);

          System.out.println(test);
          test = test.replace("\"", "\'");
          System.out.println(test);
          sb.replace(sb.indexOf("quoteText\":\"")+12, sb.indexOf("\"quoteAuthor")-4, test);
          System.out.println(sb.toString());
          in.close();
          reader.close();
          JSONObject jsonObj = new JSONObject(sb.toString());
          text[0] = jsonObj.getString("quoteText");
          Author[0] = jsonObj.getString("quoteAuthor");
          latch.countDown();
        } catch (Exception e) {
          e.printStackTrace();
        }


      }


    }).start();
    try {
      latch.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    return new Quote(text[0], Author[0]);
  }

  @Override
  public String toString() {
    return "";
  }

}
