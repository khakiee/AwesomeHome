package kjh.hantor.hantor_smarthome;

import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;

public class MainActivity extends AppCompatActivity{

    ViewPager vp;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vp = (ViewPager)findViewById(R.id.vp);
        PagerAdapter pa = new pagerAdapter(getSupportFragmentManager());
        vp.setAdapter(pa);
        vp.setCurrentItem(0);
// Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(vp);
    }
    private class pagerAdapter extends FragmentStatePagerAdapter
    {
        private final String[] TITLES = {"Status","Remote"};
        public pagerAdapter(android.support.v4.app.FragmentManager fm)
        {
            super(fm);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
        @Override
        public android.support.v4.app.Fragment getItem(int position)
        {
            switch(position)
            {
                case 0:
                    return new Status();
                case 1:
                    return new Remote();
                default :
                    return null;
            }
        }
        @Override
        public int getCount()
        {
            return 2;
        }
    }
}