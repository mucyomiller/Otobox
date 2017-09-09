package rw.sd.otobox.Fragments;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import rw.sd.otobox.Adapters.ProductsAdapter;
import rw.sd.otobox.Models.Product;
import rw.sd.otobox.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LightFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProductsAdapter adapter;
    private List<Product> productList;
    private Context mContext;

    public LightFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_light, container, false);
        //fragment content here

        mContext = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        productList = new ArrayList<>();
        adapter = new ProductsAdapter(getContext(), productList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new LightFragment.GridSpacingItemDecoration(2, dpToPx(5), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        prepareProducts();

        return view;
    }


    /**
     * Adding few products
     */
    private void prepareProducts() {
        int[] covers = new int[]{
                R.drawable.lighting_icon,
                R.drawable.fog_light,
                R.drawable.fog_light,
                R.drawable.lighting_icon,
                R.drawable.lighting_icon,
                R.drawable.fog_light
        };

        Product a = new Product("product1","good",covers[0],2 , BigDecimal.valueOf(5000));
        productList.add(a);

        a = new Product("product2","excellent", covers[1],3,BigDecimal.valueOf(1000));
        productList.add(a);

        a = new Product("product3","good", covers[2],1,BigDecimal.valueOf(2000));
        productList.add(a);

        a = new Product("product4","good", covers[3],2,BigDecimal.valueOf(4000));
        productList.add(a);
        a = new Product("product5","good", covers[4],3,BigDecimal.valueOf(5000));
        productList.add(a);

        a = new Product("product6","bad", covers[5],1,BigDecimal.valueOf(9000));
        productList.add(a);
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
}
