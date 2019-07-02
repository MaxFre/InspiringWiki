package com.example.mafr.p3;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.CountDownLatch;

/**
 * Worked on togheter
 * by Max Frennessen
 * Amin HARIRCHIAN
 * Sebastian Andersson
 * Rikard Almgren
 * last updated on 01/11 - 2018
 */

class Controller {
  private MainActivity mainActivity;
  private ShowQuote showQuote;

  Controller(MainActivity mainActivity) {
    this.mainActivity = mainActivity;

    initializeStartPage();
  }

  private void initializeStartPage() {
    StartPage startPage = (StartPage) mainActivity.getFragment("StartPage");
    if (startPage == null) {
      startPage = new StartPage();
    }
    startPage.setMain(mainActivity);
    startPage.setController(this);
    mainActivity.setFragment(startPage);
  }


  void initializeQuoteFragment() {
    showQuote = (ShowQuote) mainActivity.getFragment("ShowQuote");
    if (showQuote == null) {
      showQuote = new ShowQuote();
    }
    showQuote.setController(this);
    mainActivity.setFragment(showQuote);
  }


  void getQuoteFromApi() {
    System.out.println("getQuote");
    new Thread(new Runnable() {
      public void run() {
        try {
          mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
              Quote newQuote = new QuoteBuilder().getQuote();
              String quote = newQuote.quoteText;
              String author = newQuote.quoteAuthor;
              showQuote.reset(quote, author);
            }
          });

        } catch (NumberFormatException e) {
          e.printStackTrace();
        }
      }
    }).start();
  }


  void getWIkiFromApi(String author) {
    System.out.println("getWiki");
    String wiki;
    try {
      author = URLEncoder.encode(author.replace(":", ""),
                                 java.nio.charset.StandardCharsets.UTF_8.toString());
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    wiki = getAuthorInformation(author);
    showQuote.showWiki(wiki);

    new Thread(new Runnable() {
      public void run() {
        try {
          mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
              showQuote.fadeinWIkiText();
            }
          });

        } catch (NumberFormatException e) {
          e.printStackTrace();
        }
      }
    }).start();

  }

  private String getAuthorInformation(final String author) {
    final CountDownLatch latch = new CountDownLatch(1);
    final String[] res = new String[1];
    new Thread(new Runnable() {
      public void run() {
        String query = "http://jsonpedia.org/annotate/resource/json/en%3A" + author;
        try {
          URL url = new URL(query);
          HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
          InputStream in = new BufferedInputStream(urlConnection.getInputStream());
          BufferedReader reader = new BufferedReader(new InputStreamReader(in));
          String line;
          StringBuilder sb = new StringBuilder();
          while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
          }
          in.close();
          reader.close();

          res[0] = parse(sb.toString());
          latch.countDown();
        } catch (Exception e) {
          //TODO Write better error message
          res[0] = "No information could be found on this Author. " +
                   "\n Maybe you have lost connection, \n" +
                   "or an insufficient amount of information \n " +
                   "was available to retrieve an Author";
          latch.countDown();
          e.printStackTrace();
        }
      }

    }).start();
    try {
      latch.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println(res[0]);
    return res[0];
  }

  private String parse(String input){
    JsonParser parser = new JsonParser();
    JsonObject jsonObject = parser.parse(input).getAsJsonObject();
    String abstractStr = jsonObject.get("abstract").getAsString();
    if (abstractStr.contains("File:")) {
      abstractStr = abstractStr.substring(abstractStr.indexOf(":") + 1, abstractStr.indexOf(":"));
    }
    if (abstractStr.startsWith("#") || abstractStr.contains("may refer to")) {
      String test = jsonObject.getAsJsonArray("references").get(0).getAsJsonObject().get(
          "url").getAsString();
      try {
        return getAuthorInformation(URLEncoder.encode(test.substring(test.lastIndexOf("/") + 1),
                                                      java.nio.charset.StandardCharsets.UTF_8.toString()));
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
    }
    if (abstractStr.length() > 500) {
      try {
        abstractStr = abstractStr.substring(0, 500);
        abstractStr = abstractStr.substring(0, abstractStr.lastIndexOf(
            ".") + 1) + "\nVisit Wikipedia for more information";
      } catch (StringIndexOutOfBoundsException e) {
        e.printStackTrace();
      }
    }
    return abstractStr;
  }
}
