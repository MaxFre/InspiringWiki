package com.example.mafr.p3;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by Sebastian Andersson
 */

public class StartPage extends android.support.v4.app.Fragment {
    private Controller controller;
    private ImageView ivStart;
    private Button btnStart;
    private boolean okeyToPress = false;
    private boolean userAlreadyPressed = false;
    private MainActivity main;

    public StartPage() {
        // Required empty public constructor
    }

    public void setMain(MainActivity main){
        this.main = main;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start_page, container, false);
        initializeComponents(view);

        return view;
    }

    private void initializeComponents(View view) {
        int test = 1;
        if(main!=null) {
             test = main.getResources().getConfiguration().orientation;
        }

        ivStart = view.findViewById(R.id.ivStart);
        if(test == Configuration.ORIENTATION_LANDSCAPE){
            new StartPage.DownloadImageTask(ivStart)
                    .execute("https://picsum.photos/900/700/?random");
        }
        else {
            new StartPage.DownloadImageTask(ivStart)
                    .execute("https://picsum.photos/570/900/?random");


        }

        btnStart = view.findViewById(R.id.btnStart);
        btnStart.setAlpha(0F);
        btnStart.setOnClickListener(new ButtonListener());


    }

    public void fadeInAnimiation(){
        ObjectAnimator fadeInPictureStart = ObjectAnimator.ofFloat(ivStart, "alpha", 0f, 1f);
        ObjectAnimator fadeInButtonStart = ObjectAnimator.ofFloat(btnStart, "alpha", 0f, 1f);

        AnimatorSet as = new AnimatorSet();
        as.play(fadeInPictureStart).before(fadeInButtonStart);
        as.setDuration(2000);
        as.start();

        fadeInPictureStart.addListener(new Animator.AnimatorListener() {
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

        fadeInButtonStart.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                okeyToPress = true;
                alreadyPressed();
            }
            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }
    public void setController(Controller controller) {
        this.controller = controller;
    }

        private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            userAlreadyPressed = true;
            if(okeyToPress) {
                ObjectAnimator fadeout = ObjectAnimator.ofFloat(ivStart, "alpha", 1f, 0f);
                ObjectAnimator fadeout2 = ObjectAnimator.ofFloat(btnStart, "alpha", 1f, 0f);
                fadeout.setDuration(2000);
                fadeout.setDuration(2000);


                AnimatorSet sa = new AnimatorSet();
                sa.play(fadeout).with(fadeout2);
                sa.start();

                fadeout.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        controller.initializeQuoteFragment();
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

    public void alreadyPressed(){
        if(userAlreadyPressed){
            ObjectAnimator fadeout = ObjectAnimator.ofFloat(ivStart, "alpha", 1f, 0f);
            ObjectAnimator fadeout2 = ObjectAnimator.ofFloat(btnStart, "alpha", 1f, 0f);
            fadeout.setDuration(2000);
            fadeout.setDuration(2000);


            AnimatorSet sa = new AnimatorSet();
            sa.play(fadeout).with(fadeout2);
            sa.start();

            fadeout.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    controller.initializeQuoteFragment();
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
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            ivStart.setImageBitmap(result);
            fadeInAnimiation();
        }
    }
}

