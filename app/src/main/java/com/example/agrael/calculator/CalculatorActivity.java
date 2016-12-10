package com.example.agrael.calculator;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Surface;

import java.util.ArrayList;

enum CalcType {SimpleCalc, ProgCalc, EngCalc, Chart};

public class CalculatorActivity extends AppCompatActivity {

    private final String BUNDLE_KEY = "bundleKey";
    private final String FIRST_PAGE_TAG = "android:switcher:"+R.id.fragmentPager+":0";
    private final String SECOND_PAGE_TAG = "android:switcher:"+R.id.fragmentPager+":1";

    private CalcType type;
    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Display display = getWindowManager().getDefaultDisplay();

        Bundle bundle = new Bundle();
        if(savedInstanceState != null){
            bundle = savedInstanceState.getBundle(BUNDLE_KEY);
        }

        switch (display.getRotation()){
            case Surface.ROTATION_0: {
                pager = (ViewPager) findViewById(R.id.fragmentPager);

                if(fragmentManager.findFragmentByTag(FIRST_PAGE_TAG) != null) {
                    fragmentManager.beginTransaction()
                                    .remove(fragmentManager.findFragmentByTag(FIRST_PAGE_TAG))
                                    .commit();
                    fragmentManager.beginTransaction()
                                    .remove(fragmentManager.findFragmentByTag(SECOND_PAGE_TAG))
                                    .commit();
                }
                CalculatorPagerAdapter adapter = new CalculatorPagerAdapter(fragmentManager);
                SimpleCalcFragment simpleCalcFragment = new SimpleCalcFragment();
                simpleCalcFragment.setArguments(bundle);

                ChartEditorFragment chartFragment = new ChartEditorFragment();

                adapter.addPages(simpleCalcFragment);
                adapter.addPages(chartFragment);

                pager.setAdapter(adapter);
                pager.setCurrentItem(0);
                type = CalcType.SimpleCalc;
                pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

                    @Override
                    public void onPageSelected(int position) {
                        switch(position){
                            case 0: {
                                type = CalcType.SimpleCalc;
                                break;
                            }
                            case 1: {
                                type = CalcType.Chart;
                                break;
                            }
                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {}
                });

                break;
            }
            case Surface.ROTATION_90: {
                ProgrammerCalcFragment programmerCalcFragment = new ProgrammerCalcFragment();
                type = CalcType.ProgCalc;
                programmerCalcFragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, programmerCalcFragment)
                        .commit();
                break;
            }
            case Surface.ROTATION_270: {
                type = CalcType.EngCalc;
                EngineeringCalcFragment engineeringCalcFragment = new EngineeringCalcFragment();
                engineeringCalcFragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, engineeringCalcFragment)
                        .commit();
                break;
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        switch (type){
            case SimpleCalc: {
                SimpleCalcFragment fragment = (SimpleCalcFragment) getSupportFragmentManager()
                                                                 .findFragmentByTag(FIRST_PAGE_TAG);

                outState.putBundle(BUNDLE_KEY, fragment.getData());
                break;
            }
            case ProgCalc: {
                ProgrammerCalcFragment fragment = (ProgrammerCalcFragment) getSupportFragmentManager().
                                                                    findFragmentById(R.id.fragmentContainer);
                outState.putBundle(BUNDLE_KEY, fragment.getData());
                break;
            }
            case EngCalc: {
                EngineeringCalcFragment fragment = (EngineeringCalcFragment) getSupportFragmentManager().
                                                                    findFragmentById(R.id.fragmentContainer);
                outState.putBundle(BUNDLE_KEY, fragment.getData());
                break;
            }
            case Chart: {
                break;
            }
        }
    }


    private class CalculatorPagerAdapter extends FragmentPagerAdapter{

        ArrayList<Fragment> pages;

        public CalculatorPagerAdapter(FragmentManager fm) {
            super(fm);
            pages = new ArrayList<>();
        }

        public void addPagesFromFragmmentManager(String tag, int pageNumber){
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
            if(fragment != null){
                pages.add(pageNumber, fragment);
            }
        }

        public void addPages(Fragment page){
            pages.add(page);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0: {
                    return pages.get(0);
                }
                case 1: {
                    return pages.get(1);
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
