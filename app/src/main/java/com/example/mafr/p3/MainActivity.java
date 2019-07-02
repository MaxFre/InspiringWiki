package com.example.mafr.p3;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/*
  created by Sebastian Andersson and Max Frennessen
 */

public class MainActivity extends AppCompatActivity {
  private FragmentManager fragmentManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    fragmentManager = getSupportFragmentManager();
    Controller controller = new Controller(this);
  }

  public void setFragment(Fragment fragment) {
    FragmentTransaction transaction = fragmentManager.beginTransaction();
    transaction.replace(R.id.fragment_container, fragment);
    transaction.commit();
  }

  public Fragment getFragment(String tag) {
    return fragmentManager.findFragmentByTag(tag);
  }


}
