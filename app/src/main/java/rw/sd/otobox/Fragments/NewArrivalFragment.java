package rw.sd.otobox.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.common.base.Function;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import es.dmoral.toasty.Toasty;
import rw.sd.otobox.Adapters.NewArrivalsRecyclerviewAdapter;
import rw.sd.otobox.Models.Arrival;
import rw.sd.otobox.Models.Product;
import rw.sd.otobox.Models.SectionDataModel;
import rw.sd.otobox.R;

/**
 * Created by miller on 11/29/17.
 */


public class NewArrivalFragment extends Fragment {
    private static final String TAG = "NewArrivalFragment";
    ArrayList<SectionDataModel> allData;
    ArrayList<Arrival> mArrivalList;
    private RecyclerView recyclerView;
    private Context mContext;
    private  NewArrivalsRecyclerviewAdapter adapter;

    public NewArrivalFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allData = new ArrayList<>();
        mArrivalList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LoadUncategorizedData();
        LoadData();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_newarrivals, container, false);
        //fragement content here
        mContext = view.getContext();
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        adapter = new NewArrivalsRecyclerviewAdapter(getContext(), allData);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        return view;
    }


    public void LoadUncategorizedData() {
        Log.d(TAG, "Load data withou categorized");
        //setting section model
        SectionDataModel mSectionDataModel = new SectionDataModel();
        mSectionDataModel.setHeaderTitle("");
        mSectionDataModel.setHeaderSubTitle("");
        //setting items model
        ArrayList<Product> mProductRow = new ArrayList<>();
        ParseQuery<ParseObject> mQuery = ParseQuery.getQuery("Spare");
        mQuery.whereEqualTo("generation", null);
        mQuery.orderByDescending("createdAt");
        mQuery.setLimit(5);
        mQuery.findInBackground((Spare, err) -> {
            if(err == null) {
                for (ParseObject mSpare : Spare) {
                    Product a = new Product(mSpare.getObjectId(), mSpare.get("name").toString(), mSpare.get("quality").toString(), NewArrivalFragment.this.getString(R.string.server_base_url) + mSpare.get("url").toString(), Integer.valueOf(mSpare.get("warranty").toString()), BigDecimal.valueOf(Integer.valueOf(mSpare.get("price").toString())));
                    mProductRow.add(a);
                }
                if(mProductRow.size()>0){
                    mSectionDataModel.setAllItemsInSection(mProductRow);
                    allData.add(mSectionDataModel);
                    Log.d(TAG, "uncategorized dataset change event!");
                    adapter.notifyDataSetChanged();
                }
            }
            else
            {
                Toasty.error(getContext(),"Error Occured!"+err.getMessage(),Toast.LENGTH_LONG,true).show();
            }
        });
    }

    public void LoadData() {
    Log.d(TAG, "Loading data with category");
    //setting items model
    ParseQuery<ParseObject> mQuery = ParseQuery.getQuery("Spare");
    mQuery.include("generation");
    mQuery.include("generation.model");
    mQuery.include("generation.model.parent");
    mQuery.setLimit(1000);
    mQuery.orderByDescending("createdAt");
    mQuery.findInBackground((Spare, err) -> {
    if(err == null && Spare.size() > 0) {
        for (ParseObject mSpare : Spare) {
            if (mSpare.getParseObject("generation") != null && mSpare.getParseObject("generation").getParseObject("model") != null && mSpare.getParseObject("generation").getParseObject("model").getParseObject("parent") != null){
                Product a = new Product(mSpare.getObjectId(), mSpare.get("name").toString(), mSpare.get("quality").toString(), NewArrivalFragment.this.getString(R.string.server_base_url) + mSpare.get("url").toString(), Integer.valueOf(mSpare.get("warranty").toString()), BigDecimal.valueOf(Integer.valueOf(mSpare.get("price").toString())));
                Arrival mArrival = new Arrival(mSpare.getParseObject("generation").getParseObject("model").getParseObject("parent").get("name").toString(),mSpare.getParseObject("generation").getParseObject("model").get("name").toString(),mSpare.getParseObject("generation").get("name").toString(),a,mSpare.getCreatedAt());
                mArrivalList.add(mArrival);
            }
        }
//        Log.d(TAG, "mArrivalList Size =>"+mArrivalList.size());
//        Log.d(TAG, "i HAVE =>"+mArrivalList.toString());
        if(mArrivalList.size() > 0){
            //sorting & Multimaping returned datas
            //define group function
            Function<Arrival,String> ArrivalGroupFunc = new Function<Arrival, String>() {
                @Override
                public String apply(Arrival input) {
                    return input.model;
                }
            };
            Multimap<String, Arrival> multimap = Multimaps.index(mArrivalList, ArrivalGroupFunc);
            Log.d(TAG, "WE GOT =>"+ multimap.toString());
            //setting section model
            int mCount = 0;
           for(String key : multimap.keySet()){
               mCount++;
               ArrayList<Product> mProductRow = new ArrayList<>();
               ArrayList<Arrival> mArrivalArrayListRowData = new ArrayList<>();
               Collection<Arrival> mCollectionRowData = multimap.get(key);
               mArrivalArrayListRowData.addAll(mCollectionRowData);
               SectionDataModel mSectionDataModel = new SectionDataModel();
               mSectionDataModel.setHeaderTitle(mArrivalArrayListRowData.get(0).brand);
               mSectionDataModel.setHeaderSubTitle(key);
               for(int i = 0; i < mArrivalArrayListRowData.size(); i++){
                   if(i == 5){
                       break;
                   }
                   Product mProduct = mArrivalArrayListRowData.get(i).getProduct();
                   mProductRow.add(mProduct);
               }

               if(mCount == 10){
                   break;
               }
               mSectionDataModel.setAllItemsInSection(mProductRow);
               allData.add(mSectionDataModel);
           }
            Log.d(TAG, "categorized dataset change event!");
            adapter.notifyDataSetChanged();
        }
    }
    });
    }

}
