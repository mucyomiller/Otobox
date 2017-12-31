package rw.sd.otobox.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import rw.sd.otobox.Models.Brand;
import rw.sd.otobox.Models.Model;
import rw.sd.otobox.Models.SectionDataModel;
import rw.sd.otobox.R;
import rw.sd.otobox.YearActivity;

/**
 * Created by miller on 11/28/17.
 */

public class NewArrivalsRecyclerviewAdapter extends RecyclerView.Adapter<NewArrivalsRecyclerviewAdapter.ItemRowHolder> {

    private static final String TAG = "NewArrivalsRecyclerview";
    private ArrayList<SectionDataModel> dataList;
    private Context mContext;
    private Bundle bundle;

    public NewArrivalsRecyclerviewAdapter(Context context, ArrayList<SectionDataModel> dataList) {
        this.dataList = dataList;
        this.mContext = context;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.newarrivals_list_item, null);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(ItemRowHolder itemRowHolder, int i) {

        final String sectionName = dataList.get(i).getHeaderTitle();
        final String sectionSubTitle = dataList.get(i).getHeaderSubTitle();

        ArrayList singleSectionItems = dataList.get(i).getAllItemsInSection();

        itemRowHolder.itemTitle.setText(sectionName);
        itemRowHolder.itemSubTitle.setText(sectionSubTitle);
        itemRowHolder.itemHeader.setMinimumWidth(Resources.getSystem().getDisplayMetrics().widthPixels -16);
        itemRowHolder.itemHeader.setOnClickListener(v -> {
            //check if it's not common/spares
            if(!sectionSubTitle.equals("Spares")){
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Model");
                query.whereEqualTo("name", sectionSubTitle);
                query.include("parent");
                query.getFirstInBackground((object, e) -> {
                    if(e == null)
                    {
                        Model mModel = new Model();
                        mModel.setId(object.getObjectId());
                        mModel.setName(object.get("name").toString());
                        ParseObject pObject = object.getParseObject("parent");
                        Brand mBrand = new Brand();
                        mBrand.setId(pObject.getObjectId());
                        mBrand.setName(pObject.get("name").toString());
                        mBrand.setThumbnail(v.getResources().getString(R.string.server_base_url)+pObject.get("url").toString());
                        mBrand.setNumOfModels(1);
                        Intent mIntent = new Intent(v.getContext(), YearActivity.class);
                        mIntent.putExtra("Model",mModel);
                        mIntent.putExtra("Brand",mBrand);
                        v.getContext().startActivity(mIntent);
                    }
                    else{
                        Toasty.error(v.getContext(),e.getMessage(),Toast.LENGTH_SHORT,true).show();
                        Log.d(TAG, "LoadData: error"+ e.getMessage());
                    }
                });
            }else
            {
                Toasty.info(v.getContext(),"Not supported Here",Toast.LENGTH_SHORT,true).show();
            }
        });

        SectionListDataAdapter itemListDataAdapter = new SectionListDataAdapter(mContext, singleSectionItems);

        itemRowHolder.recycler_view_list.setHasFixedSize(true);
        itemRowHolder.recycler_view_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        itemRowHolder.recycler_view_list.setAdapter(itemListDataAdapter);




       /* Glide.with(mContext)
                .load(feedItem.getImageURL())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.bg)
                .into(feedListRowHolder.thumbView);*/
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {

        protected LinearLayout itemHeader;

        protected TextView itemTitle;

        protected TextView itemSubTitle;

        protected RecyclerView recycler_view_list;


        public ItemRowHolder(View view) {
            super(view);

            this.itemHeader = (LinearLayout) view.findViewById(R.id.itemHeader);
            this.itemTitle = (TextView) view.findViewById(R.id.itemTitle);
            this.itemSubTitle = (TextView) view.findViewById(R.id.itemSubTitle);
            this.recycler_view_list = (RecyclerView) view.findViewById(R.id.recycler_view_list);

        }

    }

}


