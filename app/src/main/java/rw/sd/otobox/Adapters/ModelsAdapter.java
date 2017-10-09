package rw.sd.otobox.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.elyeproj.loaderviewlibrary.LoaderTextView;

import java.util.List;

import rw.sd.otobox.BuyActivity;
import rw.sd.otobox.Models.Brand;
import rw.sd.otobox.Models.Model;
import rw.sd.otobox.R;
import rw.sd.otobox.YearActivity;

/**
 * Created by miller on 9/3/17.
 */

public class ModelsAdapter extends RecyclerView.Adapter<ModelsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Model> modelList;
    private Brand mBrand;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public LoaderTextView title;
//      public ImageView thumbnail;
        public View mHolder;

        public MyViewHolder(View view) {
            super(view);
            mHolder = view;
            title = (LoaderTextView) view.findViewById(R.id.title_model);
            title.resetLoader();
//            thumbnail = (ImageView) view.findViewById(R.id.thumbnail_model);
        }
    }


    public ModelsAdapter(Context mContext, List<Model> modelList,Brand mBrand) {
        this.mContext = mContext;
        this.modelList = modelList;
        this.mBrand = mBrand;
    }

    @Override
    public ModelsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.model_card_v2, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ModelsAdapter.MyViewHolder holder, int position) {
    if(modelList.size()>0){
        final Model model = modelList.get(position);
        holder.title.setText(model.getName());
        // loading brand cover using Glide library
        //  Glide.with(mContext).load(model.getThumbnail()).into(holder.thumbnail);
        holder.mHolder.setOnClickListener(v -> {
            Intent mIntent = new Intent(v.getContext(), YearActivity.class);
            mIntent.putExtra("Model",model);
            mIntent.putExtra("Brand",mBrand);
            v.getContext().startActivity(mIntent);
            //Toast.makeText(mContext, "Not Available Now", Toast.LENGTH_SHORT).show();
        });
    }
    }

    @Override
    public int getItemCount() {
        return modelList.size()>0?modelList.size():8;
    }
}