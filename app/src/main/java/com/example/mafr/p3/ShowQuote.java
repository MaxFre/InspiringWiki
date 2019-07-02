package com.example.mafr.p3;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

/**
 * Created by Max Frennessen last updated on 01/11 - 2018
 */
public class ShowQuote extends android.support.v4.app.Fragment {

  TextView tvQuote;
  TextView tvWiki;
  TextView tvAuthor;
  Button btnNext;
  ImageView imageView;
  boolean first = true;
  boolean changedPlace = false;
  int turn = 1;
  String author = "";
  private Controller controller;
  private boolean ifOkeyToPressGetQuoteBtn = true;
  private boolean ifOkeyToPressAuthorBtn = true;
  int height;
  int width;

  public ShowQuote() {
    // Required empty public constructor
  }

  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_show_quote, container, false);
    initializeComponents(view);
    return view;
  }

  public static int getScreenWidth() {
    return Resources.getSystem().getDisplayMetrics().widthPixels;
  }

  public static int getScreenHeight() {
    return Resources.getSystem().getDisplayMetrics().heightPixels;
  }

  private void initializeComponents(View view) {
    int height = getScreenHeight();
    int width = getScreenWidth();
    tvWiki = view.findViewById(R.id.tvWiki);
    tvQuote = view.findViewById(R.id.tvQuote);
    tvAuthor = view.findViewById(R.id.tvAuthor);
    btnNext = view.findViewById(R.id.btnNext);
    imageView = view.findViewById(R.id.ivStart);


    fadeinAndout();
    tvWiki.animate()
        .translationYBy(950)
        .setDuration(1)
        .start();
    btnNext.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        ifOkeyToPressAuthorBtn = true;
        if (ifOkeyToPressGetQuoteBtn) {
          ifOkeyToPressGetQuoteBtn = false;
          fadeinAndout();
        }
      }

    });

    tvAuthor.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (!(author.equals("Unknown"))) {
          if (ifOkeyToPressAuthorBtn) {
            ifOkeyToPressAuthorBtn = false;
            controller.getWIkiFromApi(author);


          }
        }
      }
    });

  }

  public void setController(Controller controller) {
    this.controller = controller;
  }

  public void fadeinAndout() {
    //FADE OUT
    ObjectAnimator fadeOutImage = ObjectAnimator.ofFloat(imageView, "alpha", 1f, 0f);
    fadeOutImage.setDuration(1000);

    ObjectAnimator fadeOutQuote = ObjectAnimator.ofFloat(tvQuote, "alpha", 1f, 0f);
    fadeOutQuote.setDuration(1000);

    ObjectAnimator fadeOutAuthor = ObjectAnimator.ofFloat(tvAuthor, "alpha", 1f, 0f);
    fadeOutAuthor.setDuration(1000);

    ObjectAnimator fadeoutWiki = ObjectAnimator.ofFloat(tvWiki, "alpha", 1f, 0f);
    fadeoutWiki.setDuration(1000);

    ObjectAnimator fadeoutBtn = ObjectAnimator.ofFloat(btnNext, "alpha", 1f, 0f);
    fadeoutBtn.setDuration(1000);

    if (first) {
      new DownloadImageTask(imageView).execute("https://picsum.photos/520/820/?random");
      first = false;

    } else {
      if (changedPlace) {

        new DownloadImageTask(imageView)
            .execute("https://picsum.photos/520/820/?random");

        AnimatorSet sa = new AnimatorSet();
        sa.play(fadeOutAuthor).with(fadeOutQuote).with(fadeoutWiki).with(fadeOutImage).with(
            fadeoutBtn);
        sa.start();

        fadeOutAuthor.addListener(new Animator.AnimatorListener() {
          @Override
          public void onAnimationStart(Animator animation) {
          }

          @Override
          public void onAnimationEnd(Animator animation) {
          }

          @Override
          public void onAnimationCancel(Animator animation) {
          }

          @Override
          public void onAnimationRepeat(Animator animation) {
          }
        });

      } else {
        AnimatorSet sa = new AnimatorSet();
        sa.play(fadeOutAuthor).with(fadeOutQuote).with(fadeOutImage).with(fadeoutBtn);
        sa.start();

        fadeOutAuthor.addListener(new Animator.AnimatorListener() {
          @Override
          public void onAnimationStart(Animator animation) {
          }

          @Override
          public void onAnimationEnd(Animator animation) {
            if (changedPlace) {
              tvQuote.setTextSize(22);
              tvQuote.animate()
                  .translationYBy(470)
                  .setDuration(1)
                  .start();
              tvAuthor.animate()
                  .translationYBy(500)
                  .setDuration(1)
                  .start();
              tvWiki.animate()
                  .translationYBy(1150)
                  .setDuration(1)
                  .start();
              changedPlace = false;
            }
            new DownloadImageTask(imageView).execute("https://picsum.photos/520/820/?random");
          }

          @Override
          public void onAnimationCancel(Animator animation) {
          }

          @Override
          public void onAnimationRepeat(Animator animation) {
          }
        });

      }
    }
  }


  public void reset(String quote, String author) {

    this.author = author;

    tvQuote.setText(quote);

    if (author.equals("")) {
      this.author = "Unknown";
    }
    tvAuthor.setText(this.author);
    turn++;

    ObjectAnimator fadeInImage = ObjectAnimator.ofFloat(imageView, "alpha", 0f, 1f);
    fadeInImage.setDuration(1000);

    ObjectAnimator fadeInQuote = ObjectAnimator.ofFloat(tvQuote, "alpha", 0f, 1f);
    fadeInQuote.setDuration(1000);


    ObjectAnimator fadeInAuthor = ObjectAnimator.ofFloat(tvAuthor, "alpha", 0f, 1f);
    fadeInAuthor.setDuration(1000);

    ObjectAnimator fadeInBtn = ObjectAnimator.ofFloat(btnNext, "alpha", 0f, 1f);
    fadeInBtn.setDuration(1000);


    AnimatorSet sa = new AnimatorSet();
    sa.play(fadeInQuote).with(fadeInImage).with(fadeInBtn).before(fadeInAuthor);
    sa.start();
    if (changedPlace) {
      tvQuote.setTextSize(22);
      tvQuote.animate()
          .translationYBy(470)
          .setDuration(1)
          .start();
      tvAuthor.animate()
          .translationYBy(500)
          .setDuration(1)
          .start();
      tvWiki.animate()
          .translationYBy(1150)
          .setDuration(1)
          .start();
      changedPlace = false;
    }
    fadeInQuote.addListener(new Animator.AnimatorListener() {
      @Override
      public void onAnimationStart(Animator animation) {

      }

      @Override
      public void onAnimationEnd(Animator animation) {
        ifOkeyToPressGetQuoteBtn = true;
      }

      @Override
      public void onAnimationCancel(Animator animation) {
      }

      @Override
      public void onAnimationRepeat(Animator animation) {
      }
    });


  }


  public void showWiki(String wiki) {
    if (!(changedPlace)) {
      tvWiki.setAlpha(0f);
      changedPlace = true;
      ObjectAnimator fadeOutQuote = ObjectAnimator.ofFloat(tvQuote, "textSize", 22, 22);
      fadeOutQuote.setDuration(1000);
      fadeOutQuote.start();
      tvQuote.animate()
          .translationYBy(-470)
          .setDuration(1000)
          .start();
      tvWiki.setText(wiki);
      tvAuthor.animate()
          .translationYBy(-500)
          .setDuration(1000)
          .start();
    }
  }

  public void fadeinWIkiText() {
    tvWiki.setAlpha(0f);
    tvWiki.animate()
        .translationYBy(-1150)
        .setDuration(1000)
        .start();

    ObjectAnimator fadeInBack = ObjectAnimator.ofFloat(tvWiki, "alpha", 0f, 1f);
    fadeInBack.setDuration(1000);
    fadeInBack.start();
  }


  private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    DownloadImageTask(ImageView bmImage) {
      this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
      String urlDisplay = urls[0];
      Bitmap mIcon11 = null;
      try {
        InputStream in = new java.net.URL(urlDisplay).openStream();
        mIcon11 = BitmapFactory.decodeStream(in);
      } catch (Exception e) {
        Log.e("Error", e.getMessage());
        e.printStackTrace();
      }
      return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
      imageView.setImageBitmap(result);
      System.out.println("Changing new image");
      if (controller != null)
        controller.getQuoteFromApi();
    }

  }
}
