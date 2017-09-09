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

import com.shawnlin.numberpicker.NumberPicker;

import java.util.Calendar;

import mehdi.sakout.fancybuttons.FancyButton;
import rw.sd.otobox.Models.Brand;


public class YearActivity extends AppCompatActivity {
    private static final String TAG = "YearActivity";

    NumberPicker numberPicker;
    TextView  year_cover_text;
    FancyButton mGoButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_year);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        year_cover_text = (TextView) findViewById(R.id.year_cover_text);
        mGoButton = (FancyButton) findViewById(R.id.btn_go);
        int CURRENT_YEAR = Calendar.getInstance().get(Calendar.YEAR);
        numberPicker = (NumberPicker) findViewById(R.id.number_picker);
        numberPicker.setMaxValue(CURRENT_YEAR);
        numberPicker.setMinValue(CURRENT_YEAR - 50);
        Brand mBrand = getIntent().getParcelableExtra("Brand");
        year_cover_text.setText(mBrand.getName() +" MODEL YEAR");
            mGoButton.setOnClickListener(v -> {
                Intent mIntent = new Intent(v.getContext(),ModelActivity.class);
                mIntent.putExtra("Brand",mBrand);
                Log.d(TAG, "onCreate: selected year =>"+ numberPicker.getValue());
                mIntent.putExtra("SelectedYear",numberPicker.getValue());
                v.getContext().startActivity(mIntent);
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
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
