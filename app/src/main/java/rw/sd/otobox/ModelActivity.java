package rw.sd.otobox;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import rw.sd.otobox.Adapters.ModelsAdapter;
import rw.sd.otobox.Models.Brand;
import rw.sd.otobox.Models.Model;

public class ModelActivity extends AppCompatActivity {
    private static final String TAG = "ModelActivity";
    private RecyclerView recyclerViewModel;
    private ImageView  brand_logo;
    private TextView brand_title,modelscount;
    private ModelsAdapter adapter;
    private List<Model> modelList;
    private Context mContext;
    private Brand mBrand;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model);
        mContext = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerViewModel = (RecyclerView) findViewById(R.id.recycler_view_model);
        brand_logo = (ImageView) findViewById(R.id.brand_logo);
        brand_title = (TextView) findViewById(R.id.brand_title);
        modelscount     = (TextView) findViewById(R.id.models_count);

        mBrand = getIntent().getParcelableExtra("Brand");
        brand_title.setText(mBrand.getName());
        Glide.with(mContext).load(mBrand.getThumbnail()).into(brand_logo);


        modelList = new ArrayList<>();
        adapter = new ModelsAdapter(this, modelList,mBrand);

//        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerViewModel.setLayoutManager(mLayoutManager);
//        recyclerViewModel.addItemDecoration(new ModelActivity.GridSpacingItemDecoration(2, dpToPx(5), true));
        recyclerViewModel.setItemAnimator(new DefaultItemAnimator());
        recyclerViewModel.setAdapter(adapter);

        prepareModels();
        try {
            Glide.with(this).load(R.drawable.cover).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adding few brands
     */
    private void prepareModels() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Model");
        query.include("parent");
        query.findInBackground((Model, e) -> {
            for (ParseObject mModel: Model) {
                if(mModel.getParseObject("parent").getObjectId().equals(mBrand.getId())){
                    Model a = new Model(mModel.getObjectId(),mModel.get("name").toString(), getString(R.string.server_base_url)+mModel.get("url").toString());
                    modelList.add(a);
                }
            }
            modelscount.setText(modelList.size()+" models");
            adapter.notifyDataSetChanged();
        });
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
                Intent mSearchIntent = new Intent(ModelActivity.this,AboutActivity.class);
                startActivity(mSearchIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}