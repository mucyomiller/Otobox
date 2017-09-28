package rw.sd.otobox;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SearchableActivity extends AppCompatActivity {
    private static final String TAG = "SearchableActivity";
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        // Get the intent, verify the action and get the query

            btn = (Button) findViewById(R.id.btn);
            btn.setOnClickListener(v ->{
                Toast.makeText(this, "Ok lambda", Toast.LENGTH_SHORT).show();
            });
        Intent intent = getIntent();
        HandleIntent(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        HandleIntent(intent);
        super.onNewIntent(intent);
    }

    private void HandleIntent(Intent intent){
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
//            doMySearch(query);
            Log.d(TAG, "onCreate: QUERY =>"+query);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

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
                Intent mSearchIntent = new Intent(SearchableActivity.this,AboutActivity.class);
                startActivity(mSearchIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
