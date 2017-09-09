package rw.sd.otobox;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import rw.sd.otobox.Adapters.SectionsPagerAdapter;
import rw.sd.otobox.Fragments.BodyFragment;
import rw.sd.otobox.Fragments.EngineFragment;
import rw.sd.otobox.Fragments.LightFragment;
import rw.sd.otobox.Fragments.LubeFragment;
import rw.sd.otobox.Models.Brand;
import rw.sd.otobox.Models.Model;

public class BuyActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private TextView model_title,brand_title;
    private ImageView brand_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        model_title = (TextView) findViewById(R.id.model_title);
        brand_title = (TextView) findViewById(R.id.brand_title);
        brand_logo = (ImageView) findViewById(R.id.brand_logo);
        Model model = getIntent().getParcelableExtra("Model");
        Brand brand = getIntent().getParcelableExtra("Brand");

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
        mSectionsPagerAdapter.addFragemnt(new BodyFragment());
        mSectionsPagerAdapter.addFragemnt(new LubeFragment());
        mSectionsPagerAdapter.addFragemnt(new LightFragment());
        mSectionsPagerAdapter.addFragemnt(new EngineFragment());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setText(R.string.tab_text_1);
        tabLayout.getTabAt(1).setText(R.string.tab_text_2);
        tabLayout.getTabAt(2).setText(R.string.tab_text_3);
        tabLayout.getTabAt(3).setText(R.string.tab_text_4);
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
        if (id == R.id.action_cart) {
            Toast.makeText(this, "Clicked cart", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
