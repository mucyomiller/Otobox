package rw.sd.otobox.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import rw.sd.otobox.ModelActivity;
import rw.sd.otobox.Models.Brand;
import rw.sd.otobox.R;
import rw.sd.otobox.YearActivity;

/**
 * Created by mucyo miller on 8/30/17.
 */
public class BrandsAdapter extends RecyclerView.Adapter<BrandsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Brand> brandList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView thumbnail;
        public View mHolder;

        public MyViewHolder(View view) {
            super(view);
            mHolder = view;
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }


    public BrandsAdapter(Context mContext, List<Brand> brandList) {
        this.mContext = mContext;
        this.brandList = brandList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.brand_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Brand brand = brandList.get(position);
        holder.title.setText(brand.getName());
        holder.count.setText(brand.getNumOfModels() + " models");

        // loading brand cover using Glide library
        Glide.with(mContext).load(brand.getThumbnail()).into(holder.thumbnail);

        holder.mHolder.setOnClickListener(v -> {
            Intent mIntent = new Intent(v.getContext(),ModelActivity.class);
            mIntent.putExtra("Brand",brand);
            v.getContext().startActivity(mIntent);
        });
    }

    @Override
    public int getItemCount() {
        return brandList.size();
    }
}
