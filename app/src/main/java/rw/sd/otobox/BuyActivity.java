package rw.sd.otobox;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
    private TextView model_title,brand_title;
    private ImageView brand_logo;
    private ArrayList<String> tabnames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        model_title = (TextView) findViewById(R.id.model_title);
        brand_title = (TextView) findViewById(R.id.brand_title);
        brand_logo = (ImageView) findViewById(R.id.brand_logo);
        Model model = getIntent().getParcelableExtra("Model");
        Brand brand = getIntent().getParcelableExtra("Brand");
        tabnames = new ArrayList<>();

        brand_title.setText(brand.getName());
        model_title.setText(model.getName());
        // loading brand logo using Glide library
        Glide.with(getApplicationContext()).load(brand.getThumbnail()).into(brand_logo);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.title_activity_buy));
        setupViewPager();

    }


    private void setupViewPager(){
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter.addFragemnt(new FirstPagerFragment());
        mSectionsPagerAdapter.addFragemnt(new SecondPagerFragment());
        mSectionsPagerAdapter.addFragemnt(new ThirdPagerFragment());
        mSectionsPagerAdapter.addFragemnt(new FourthPagerFragment());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        //gettimg tabs names
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Category");
        query.orderByAscending("order");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> Category, ParseException e) {
                Log.d(TAG, "counter: "+Category.size());
                for (ParseObject mCategory: Category) {
                    tabnames.add(mCategory.get("name").toString());
                }
                tabLayout.getTabAt(0).setText(tabnames.get(0));
                tabLayout.getTabAt(1).setText(tabnames.get(1));
                tabLayout.getTabAt(2).setText(tabnames.get(2));
                tabLayout.getTabAt(3).setText(tabnames.get(3));
            }
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
