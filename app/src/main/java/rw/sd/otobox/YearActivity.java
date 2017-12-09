package rw.sd.otobox;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.shawnlin.numberpicker.NumberPicker;

import java.util.ArrayList;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;
import mehdi.sakout.fancybuttons.FancyButton;
import rw.sd.otobox.Models.Brand;
import rw.sd.otobox.Models.Generation;
import rw.sd.otobox.Models.Model;


public class YearActivity extends AppCompatActivity {
    private static final String TAG = "YearActivity";

    NumberPicker numberPicker;
    TextView  year_cover_text;
    FancyButton mGoButton;
    ImageView yearCoverImg;
    ArrayList<Generation> mGenerationList;
    ArrayList<String> mYears;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_year);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mGenerationList = new ArrayList<>();
        mYears = new ArrayList<>();
        year_cover_text = (TextView) findViewById(R.id.year_cover_text);
        yearCoverImg = (ImageView) findViewById(R.id.year_cover_image);
        mGoButton = (FancyButton) findViewById(R.id.btn_go);
        numberPicker = (NumberPicker) findViewById(R.id.number_picker);
        numberPicker.setVisibility(View.INVISIBLE);
        Brand mBrand = getIntent().getParcelableExtra("Brand");
        Model mModel = getIntent().getParcelableExtra("Model");

        //getting Generation of current model
        //create model parse object from model Model class
        ParseObject myModel = ParseObject.createWithoutData("Model",mModel.getId());
        //getting Generation belongs to current Model
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Generation");
        query.whereEqualTo("model",myModel);
        query.orderByDescending("released");
        query.findInBackground((Generation, e) -> {
            if(e == null){
                for (ParseObject mGeneration: Generation) {
                    Generation a = new Generation(mGeneration.getObjectId(),mGeneration.get("name").toString(),mGeneration.get("released").toString(),getString(R.string.server_base_url)+mGeneration.get("url").toString());
                    mGenerationList.add(a);
                    mYears.add(a.getReleased());
                }
                if(mYears.size()>0){
                    numberPicker.setVisibility(View.VISIBLE);
                    numberPicker.setMinValue(0);
                    numberPicker.setMaxValue(mYears.size()-1);
                    numberPicker.setDisplayedValues(mYears.toArray(new String[mYears.size()]));
                    // loading year cover image using Glide library
                    Glide.with(getApplicationContext()).load(mGenerationList.get(numberPicker.getValue()).getUrl()).into(yearCoverImg);
                    year_cover_text.setText(mGenerationList.get(numberPicker.getValue()).getName());
                    numberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
                        Log.d(TAG, " newVal =>: "+newVal);
                        Glide.with(getApplicationContext()).load(mGenerationList.get(newVal).getUrl()).into(yearCoverImg);
                        year_cover_text.setText(mGenerationList.get(newVal).getName());
                    });

                    mGoButton.setOnClickListener(v -> {
                        Intent mIntent = new Intent(v.getContext(),BuyActivity.class);
                        mIntent.putExtra("Brand",mBrand);
                        mIntent.putExtra("Model",mModel);
                        Log.d(TAG, "onCreate: selected year =>"+ numberPicker.getValue());
                        mIntent.putExtra("Generation",mGenerationList.get(numberPicker.getValue()));
                        v.getContext().startActivity(mIntent);
                    });
                }
            }else
            {
                Toasty.error(getApplicationContext(),"error occured!"+e.getMessage(),Toast.LENGTH_SHORT,true).show();
            }
        });

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
                Intent mSearchIntent = new Intent(YearActivity.this,AboutActivity.class);
                startActivity(mSearchIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
