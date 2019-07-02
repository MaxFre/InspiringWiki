package com.example.mafr.p3;

/**
 * Created by AMIN HARIRCHIAN on 2018/10/22 .
 */
public class Quote {


  public String rawJsonString;
  public String quoteText;
  public String quoteAuthor;

  public Quote(String quoteText, String quoteAuthor) {
    this.quoteText = quoteText;
    this.quoteAuthor = quoteAuthor;
  }

  @Override
  public String toString() {
    return rawJsonString;
  }

}
