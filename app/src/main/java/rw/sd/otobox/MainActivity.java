package rw.sd.otobox;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.orhanobut.hawk.Hawk;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;

import rw.sd.otobox.Adapters.SectionsPagerAdapter;
import rw.sd.otobox.Fragments.BrandsFragment;
import rw.sd.otobox.Fragments.NewArrivalFragment;

/**
 * Created by mucyo miller on 8/30/17.
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private ArrayList<String> mAboutInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        initCollapsingToolbar();
        setupViewPager();
        try {
            Glide.with(this).load(R.drawable.cover).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }

        //retrieving About Us Information from Server
        mAboutInfo = new ArrayList<>();
        //retrieving data first form cache then online
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Contact");
        query.fromNetwork().getFirstInBackground((object, e) -> {
            if(e == null){
                Log.d(TAG, "getMaterialAboutList: Data");
                //setting mAbout ArrayList
                mAboutInfo.add(object.get("website").toString());
                mAboutInfo.add(object.get("phone").toString());
                mAboutInfo.add(object.get("email").toString());
                mAboutInfo.add(object.get("lat").toString());
                mAboutInfo.add(object.get("long").toString());
                Log.d(TAG, "getMaterialAboutList: GOT =>"+ mAboutInfo.toString());
                Hawk.put("mAboutInfo",mAboutInfo);
            }
        });
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }


    private void setupViewPager(){
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        BrandsFragment mBrandsFragment = new BrandsFragment();
        mSectionsPagerAdapter.addFragemnt(mBrandsFragment);
        NewArrivalFragment mNewArrivalFragment = new NewArrivalFragment();
        mSectionsPagerAdapter.addFragemnt(mNewArrivalFragment);
//         Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(1);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.mainactivity_tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setText("Brands");
        tabLayout.getTabAt(1).setText("New Arrivals");

        //checking if we are comming from OrderActivity to switch to NewArrivals Tab
        Intent ourIntent = getIntent();
        if(ourIntent != null){
            boolean checkIfTrue = ourIntent.getBooleanExtra("goto_new_arrivals",false);
            if(checkIfTrue){
                mViewPager.setCurrentItem(1);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
       SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        ComponentName componentName = new ComponentName(getApplicationContext(),SearchableActivity.class);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch (id){
            case R.id.action_search:
                return true;
            case R.id.action_about:
                Intent mSearchIntent = new Intent(MainActivity.this,AboutActivity.class);
                startActivity(mSearchIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
