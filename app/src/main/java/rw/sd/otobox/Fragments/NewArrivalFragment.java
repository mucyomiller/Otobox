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

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import rw.sd.otobox.Adapters.NewArrivalsRecyclerviewAdapter;
import rw.sd.otobox.Models.Product;
import rw.sd.otobox.Models.SectionDataModel;
import rw.sd.otobox.R;

/**
 * Created by miller on 11/29/17.
 */


public class NewArrivalFragment extends Fragment {
    private static final String TAG = "NewArrivalFragment";
    ArrayList<SectionDataModel> allData;
    private RecyclerView recyclerView;
    private Context mContext;
    private  NewArrivalsRecyclerviewAdapter adapter;

    public NewArrivalFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allData = new ArrayList<SectionDataModel>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LoadData();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_newarrivals, container, false);
        //fragement content here
        mContext = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        adapter = new NewArrivalsRecyclerviewAdapter(getContext(), allData);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        return view;
    }


    public void LoadData() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Model");
        query.include("parent");
        query.orderByAscending("createdAt");
        query.setLimit(10);
        query.findInBackground((Model, e) -> {
            if(e == null)
            {
                for(ParseObject mModel: Model) {
                    Log.d(TAG, "LoadData: for =>"+mModel.get("name").toString());
                    ParseObject mBrand = mModel.getParseObject("parent");
                    //setting section model
                    SectionDataModel mSectionDataModel = new SectionDataModel();
                    mSectionDataModel.setHeaderTitle(mBrand.get("name").toString());
                    mSectionDataModel.setHeaderSubTitle(mModel.get("name").toString());
                    //setting items model
                    ArrayList<Product> mProductRow = new ArrayList<>();
                    ParseQuery<ParseObject> mQuery = ParseQuery.getQuery("Spare");
                    mQuery.whereEqualTo("model", mModel);
                    mQuery.orderByDescending("createdAt");
                    mQuery.setLimit(5);
                    mQuery.findInBackground((Spare, err) -> {
                        if(err == null) {
                            for (ParseObject mSpare : Spare) {
                                Product a = new Product(mSpare.getObjectId(), mSpare.get("name").toString(), mSpare.get("quality").toString(), NewArrivalFragment.this.getString(R.string.server_base_url) + mSpare.get("url").toString(), Integer.valueOf(mSpare.get("warranty").toString()), BigDecimal.valueOf(Integer.valueOf(mSpare.get("price").toString())));
                                mProductRow.add(a);
                            }
                            mSectionDataModel.setAllItemsInSection(mProductRow);
                            allData.add(mSectionDataModel);
                            Log.d(TAG, "notified data change event!");
                            adapter.notifyDataSetChanged();
                        }
                        else
                        {
                            Toast.makeText(getContext(),"error occured!"+err.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
            else{
                Toast.makeText(getContext(),"NETWORK ERROR!",Toast.LENGTH_LONG).show();
                Log.d(TAG, "LoadData: error"+ e.getMessage());
            }
        });

//        SectionDataModel test = new SectionDataModel();
//        test.setHeaderTitle("test");
//        ArrayList<SingleItemModel> singleItemTest = new ArrayList<SingleItemModel>();
//        singleItemTest.add(new SingleItemModel("carina x", "http://google.com"));
//        singleItemTest.add(new SingleItemModel("carina y", "http://google.com"));
//        singleItemTest.add(new SingleItemModel("carina z", "http://google.com"));
//        singleItemTest.add(new SingleItemModel("carina t", "http://google.com"));
//        test.setAllItemsInSection(singleItemTest);
//        allSampleData.add(test);

    }


}
