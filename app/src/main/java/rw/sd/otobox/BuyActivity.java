package rw.sd.otobox;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import rw.sd.otobox.Adapters.SectionsPagerAdapter;
import rw.sd.otobox.Fragments.FirstPagerFragment;
import rw.sd.otobox.Fragments.FourthPagerFragment;
import rw.sd.otobox.Fragments.ThirdPagerFragment;
import rw.sd.otobox.Fragments.SecondPagerFragment;
import rw.sd.otobox.Models.Brand;
import rw.sd.otobox.Models.Model;

public class BuyActivity extends AppCompatActivity {

    private static final String TAG = "BuyActivity";

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private TextView model_title,model_title_upper,brand_title;
    private ImageView brand_logo;
    private ArrayList<String> tabnames;
    private Bundle bundle;
    private SwitchCompat switch_filter;
    public  static boolean filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        model_title = (TextView) findViewById(R.id.model_title);
        model_title_upper = (TextView) findViewById(R.id.model_title_upper);
        brand_title = (TextView) findViewById(R.id.brand_title);
        brand_logo = (ImageView) findViewById(R.id.brand_logo);
        switch_filter = (SwitchCompat) findViewById(R.id.switch_filter);
        switch_filter.setOnClickListener(v -> {
            //Is the switch is on?
            boolean on = ((SwitchCompat) v).isChecked();
            if(on)
            {
                //Do something when switch is on/checked
                filter = true;
            }
            else
            {
                //Do something when switch is off/unchecked
                filter = false;
            }
        });
        Model model = getIntent().getParcelableExtra("Model");
        Brand brand = getIntent().getParcelableExtra("Brand");
        tabnames = new ArrayList<>();
        bundle = new Bundle();
        bundle.putParcelable("model",model);
        bundle.putBoolean("filter",filter);

        brand_title.setText(brand.getName());
        model_title_upper.setText(model.getName());
        // loading brand logo using Glide library
        Glide.with(getApplicationContext()).load(brand.getThumbnail()).into(brand_logo);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.title_activity_buy));
        setupViewPager();

    }


    private void setupViewPager(){
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        FirstPagerFragment mFirstPagerFragment = new FirstPagerFragment();
        mFirstPagerFragment.setArguments(bundle);
        mSectionsPagerAdapter.addFragemnt(mFirstPagerFragment);
        SecondPagerFragment mSecondPagerFragment = new SecondPagerFragment();
        mSecondPagerFragment.setArguments(bundle);
        mSectionsPagerAdapter.addFragemnt(mSecondPagerFragment);
        ThirdPagerFragment mThirdPagerFragment = new ThirdPagerFragment();
        mThirdPagerFragment.setArguments(bundle);
        mSectionsPagerAdapter.addFragemnt(mThirdPagerFragment);
        FourthPagerFragment mFourthPagerFragment = new FourthPagerFragment();
        mFourthPagerFragment.setArguments(bundle);
        mSectionsPagerAdapter.addFragemnt(mFourthPagerFragment);
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(4);

        //
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        //gettimg tabs names
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Category");
        query.orderByAscending("order");
        query.findInBackground((Category, e) -> {
//                Log.d(TAG, "counter: "+Category.size());
            for (ParseObject mCategory: Category) {
                tabnames.add(mCategory.get("name").toString());
            }
            tabLayout.getTabAt(0).setText(tabnames.get(0));
            tabLayout.getTabAt(1).setText(tabnames.get(1));
            tabLayout.getTabAt(2).setText(tabnames.get(2));
            tabLayout.getTabAt(3).setText(tabnames.get(3));
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_buy, menu);
        return true;
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
                Intent mSearchIntent = new Intent(BuyActivity.this,AboutActivity.class);
                startActivity(mSearchIntent);
                return true;
            case R.id.action_cart:
//                Toast.makeText(this, "Clicked cart", Toast.LENGTH_SHORT).show();
                Intent mCartIntent = new Intent(getApplicationContext(),CartActivity.class);
                startActivity(mCartIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
