package rw.sd.otobox.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elyeproj.loaderviewlibrary.LoaderTextView;

import java.util.List;

import rw.sd.otobox.Models.Brand;
import rw.sd.otobox.Models.Description;
import rw.sd.otobox.Models.Model;
import rw.sd.otobox.R;

/**
 * Created by miller on 9/3/17.
 */

public class DescriptionAdapter extends RecyclerView.Adapter<DescriptionAdapter.MyViewHolder> {

    private Context mContext;
    private List<Description> descriptionList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView desc_name;
        public TextView desc_desc;
        public View mHolder;

        public MyViewHolder(View view) {
            super(view);
            mHolder = view;
            desc_name = (TextView) view.findViewById(R.id.desc_name);
            desc_desc = (TextView) view.findViewById(R.id.desc_desc);
        }
    }


    public DescriptionAdapter(Context mContext, List<Description> descriptionList) {
        this.mContext = mContext;
        this.descriptionList = descriptionList;
    }

    @Override
    public DescriptionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_description_card_v2, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DescriptionAdapter.MyViewHolder holder, int position) {
    if(descriptionList.size()>0){
        final Description mDescription = descriptionList.get(position);
        holder.desc_name.setText(mDescription.getDesc_name());
        holder.desc_desc.setText(mDescription.getDesc_desc());
    }
    }

    @Override
    public int getItemCount() {
        return descriptionList.size();
    }
}