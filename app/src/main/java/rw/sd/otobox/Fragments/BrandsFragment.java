package rw.sd.otobox.Fragments;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import rw.sd.otobox.Adapters.BrandsAdapter;
import rw.sd.otobox.Adapters.ProductsAdapter;
import rw.sd.otobox.Adapters.SectionsPagerAdapter;
import rw.sd.otobox.Models.Brand;
import rw.sd.otobox.Models.Model;
import rw.sd.otobox.Models.Product;
import rw.sd.otobox.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BrandsFragment extends Fragment {
    private static final String TAG = "BrandsFragment";
    private RecyclerView recyclerView;
    private BrandsAdapter adapter;
    private List<Brand> brandList;
    private Context mContext;
    private Model model;

    public BrandsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pager_first, container, false);
        //fragement content here
        mContext = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        brandList = new ArrayList<>();
        adapter = new BrandsAdapter(getContext(), brandList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(5), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        prepareBrands();

        return view;
    }



    /**
     * Adding few brands
     */
    private void prepareBrands() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Brand");
        query.findInBackground((Brand, e) -> {
            if(e == null)
            {
                Log.d(TAG, "counter: "+Brand.size());
                for (ParseObject mBrand: Brand) {
                    Brand a = new Brand(mBrand.getObjectId(),mBrand.get("name").toString(),1 , getString(R.string.server_base_url)+mBrand.get("url").toString());
                    brandList.add(a);
                }

                adapter.notifyItemRangeChanged(0,brandList.size());
            }
            else{
                Toast.makeText(getContext(),"NETWORK ERROR!",Toast.LENGTH_LONG).show();
                Log.d(TAG, "prepareBrands: error"+ e.getMessage());
            }
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


}
