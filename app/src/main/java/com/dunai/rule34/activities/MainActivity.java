package com.dunai.rule34.activities;

import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.dunai.rule34.R;
import com.dunai.rule34.exceptions.RESTException;
import com.dunai.rule34.fragments.ForumFragment;
import com.dunai.rule34.fragments.PostsFragment;
import com.dunai.rule34.helpers.FetchTask;
import com.dunai.rule34.helpers.InstallTracker;
import com.dunai.rule34.interfaces.NamedFragment;
import com.dunai.rule34.interfaces.OnFetchReceiver;
import com.dunai.rule34.models.Query;
import com.dunai.rule34.models.Remote;
import com.dunai.rule34.models.impl.*;
import com.dunai.rule34.views.EndlessGridView;
import com.dunai.rule34.views.PostItemView;

import java.util.List;


public class MainActivity extends ActionBarActivity {
    private static final String TAG = "rule34/MainActivity";

    ViewPager viewPager;
    private MainFragmentPagerAdapter mainFragmentPagerAdapter;
    private LinearLayout viewPagerNav;
    private LinearLayout viewPagerAccent;

    class MainFragmentPagerAdapter extends FragmentPagerAdapter {
        private boolean initial = true;

        public MainFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new PostsFragment();
                case 1:
                    return new ForumFragment();
            }

            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);

            if (initial) {
                if (viewPager.getWidth() > 0) {
                    updateAccent(position, 0);
                    initial = false;
                }
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        this.viewPager = (ViewPager) this.findViewById(R.id.viewPager);
        this.mainFragmentPagerAdapter = new MainFragmentPagerAdapter(this.getSupportFragmentManager());
        this.viewPager.setAdapter(this.mainFragmentPagerAdapter);

        this.viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                updateAccent(i, v);
            }

            @Override
            public void onPageSelected(int i) {
                getSupportActionBar().setTitle(((NamedFragment) mainFragmentPagerAdapter.getItem(i)).getName());
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        this.viewPagerNav = (LinearLayout) this.findViewById(R.id.viewPagerNav);

        this.viewPagerAccent = (LinearLayout) this.findViewById(R.id.viewPagerAccent);

        for (int i = 0; i < this.mainFragmentPagerAdapter.getCount(); i++) {
            final int finalI = i;
            this.viewPagerNav.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(finalI, true);
                }
            });
        }

        this.updateAccent(0, 0);
    }

    private void updateAccent(int i, float v) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) this.viewPagerAccent.getLayoutParams();
        switch (this.getWindowManager().getDefaultDisplay().getRotation()) {
            case Surface.ROTATION_0:
            case Surface.ROTATION_180:
                lp.height = 4;
                lp.width = this.viewPagerNav.getWidth() / this.mainFragmentPagerAdapter.getCount();
                lp.leftMargin = (int) (((float) lp.width) * (i + v));
                break;
            case Surface.ROTATION_90:
            case Surface.ROTATION_270:
                lp.width = 4;
                lp.height = this.viewPagerNav.getHeight() / this.mainFragmentPagerAdapter.getCount();
                lp.topMargin = (int) (((float) lp.height) * (i + v));
        }
        this.viewPagerAccent.setLayoutParams(lp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_my_account) {
            Intent logIn = new Intent(this, LoginActivity.class);
            startActivity(logIn);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
