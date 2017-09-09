package rw.sd.otobox;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import rw.sd.otobox.Adapters.BrandsAdapter;
import rw.sd.otobox.Models.Brand;

/**
 * Created by mucyo miller on 8/30/17.
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private BrandsAdapter adapter;
    private List<Brand> brandList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initCollapsingToolbar();
        
        runOnUiThread(()->{
            Log.d(TAG, "onCreate: ok");
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        brandList = new ArrayList<>();
        adapter = new BrandsAdapter(this, brandList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(5), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareBrands();

        try {
            Glide.with(this).load(R.drawable.cover).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    /**
     * Adding few brands
     */
    private void prepareBrands() {
        int[] covers = new int[]{
                R.drawable.car1,
                R.drawable.car2,
                R.drawable.car3,
                R.drawable.car4,
                R.drawable.car5,
                R.drawable.car6,
                R.drawable.car7,
                R.drawable.car8,
                R.drawable.car9,
                R.drawable.car10,
                R.drawable.car11,
                R.drawable.car12,
                R.drawable.car13,
                R.drawable.car14,
                R.drawable.car15,
                R.drawable.car16,
                R.drawable.car17,
                R.drawable.car18,
                R.drawable.car19,
                R.drawable.car20,
                R.drawable.car21,
                R.drawable.car22,
                R.drawable.car23,
                R.drawable.car24,
                R.drawable.car25,
                R.drawable.car26};

        Brand a = new Brand("NISSAN", 10, covers[0]);
        brandList.add(a);

        a = new Brand("DAIHATSU", 8, covers[1]);
        brandList.add(a);

        a = new Brand("HONDA", 11, covers[2]);
        brandList.add(a);

        a = new Brand("HINO", 12, covers[3]);
        brandList.add(a);

        a = new Brand("MITSUOKA", 14, covers[4]);
        brandList.add(a);

        a = new Brand("TOYOTA", 1, covers[5]);
        brandList.add(a);

        a = new Brand("MITSUBISHI", 11, covers[6]);
        brandList.add(a);

        a = new Brand("ACURA", 14, covers[7]);
        brandList.add(a);

        a = new Brand("??", 11, covers[8]);
        brandList.add(a);

        a = new Brand("ISUZU", 17, covers[9]);
        brandList.add(a);

        a = new Brand("SUZUKI", 19, covers[10]);
        brandList.add(a);

        a = new Brand("INFINITI", 8, covers[11]);
        brandList.add(a);

        a = new Brand("DATSUN", 11, covers[12]);
        brandList.add(a);

        a = new Brand("SUBARU", 12, covers[13]);
        brandList.add(a);

        a = new Brand("??", 14, covers[14]);
        brandList.add(a);

        a = new Brand("MAZDA", 1, covers[15]);
        brandList.add(a);

        a = new Brand("BENZ", 11, covers[16]);
        brandList.add(a);

        a = new Brand("BMW", 14, covers[17]);
        brandList.add(a);

        a = new Brand("AUDI", 11, covers[18]);
        brandList.add(a);

        a = new Brand("LAMBORGHNI", 17, covers[19]);
        brandList.add(a);

        a = new Brand("VORSKWAGON", 19, covers[20]);
        brandList.add(a);
        a = new Brand("MINI", 8, covers[21]);
        brandList.add(a);

        a = new Brand("JAGUAR", 11, covers[22]);
        brandList.add(a);

        a = new Brand("HYUNDAI", 12, covers[23]);
        brandList.add(a);

        a = new Brand("BENTLEY", 14, covers[24]);
        brandList.add(a);

        a = new Brand("KIA", 1, covers[25]);
        brandList.add(a);

        adapter.notifyDataSetChanged();
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
