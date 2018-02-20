package rw.sd.otobox.Fragments;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import rw.sd.otobox.Adapters.ProductsAdapter;
import rw.sd.otobox.App;
import rw.sd.otobox.Models.Generation;
import rw.sd.otobox.Models.Product;
import rw.sd.otobox.R;
import rx.parse2.ParseObservable;

/**
 * A simple {@link Fragment} subclass.
 */
public class SeventhPagerFragment extends Fragment {
    private static final String TAG = "SixthPagerFragment";
    private RecyclerView recyclerView;
    private ProductsAdapter adapter;
    private List<Product> productList;
    private Context mContext;
    private Generation generation;

    public SeventhPagerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            generation = getArguments().getParcelable("generation");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pager_first, container, false);
        //fragment content here

        mContext = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        productList = new ArrayList<>();
        adapter = new ProductsAdapter(getContext(), productList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new SeventhPagerFragment.GridSpacingItemDecoration(2, dpToPx(5), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        prepareProducts();

        return view;
    }

    /**
     * Adding few products
     */
    private void prepareProducts() {
        getProducts("mid");
        ((App)getActivity().getApplication()).bus().toObservable().subscribe(o -> {
            if(o instanceof  String){
                Log.d(TAG, " RxBus values we got! =>"+ o);
                getProducts(o.toString());
            }
        });
    }


    private  void  getProducts(String index){
        //clear adapter
        adapter.removeAll();
        //creating observable
        Observable<ParseObject> mSpare = ParseObservable.find(ParseQuery.getQuery("Spare").include("category").include("generation").include("generation.model"));
        mSpare.filter(object ->{
            if(index.equals("mid") && object.getParseObject("category").get("order").toString().equals("6") && (object.getParseObject("generation") == null || object.getParseObject("generation").get("name").equals(generation.getName()))){
                return true;
            }else
            {
                return object.get("quality").toString().equals(index) && object.getParseObject("category").get("order").toString().equals("6") && (object.getParseObject("generation") == null || object.getParseObject("generation").get("name").equals(generation.getName()));
            }
        }).observeOn(AndroidSchedulers.mainThread()).doOnComplete(() -> adapter.notifyDataSetChanged()).subscribe(object -> {
            Log.d(TAG, "spares found:"+object.get("name").toString());
            Product a = new Product(object.getObjectId(),object.get("name").toString(),object.get("quality").toString(),getString(R.string.server_base_url)+object.get("url").toString(),Integer.valueOf(object.get("warranty").toString()),BigDecimal.valueOf(Integer.valueOf(object.get("price").toString())));
            productList.add(a);
        }, e ->{
            Log.d(TAG, "error occured!: "+ e.getMessage());
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
