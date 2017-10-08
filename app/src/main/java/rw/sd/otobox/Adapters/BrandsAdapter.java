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
import com.elyeproj.loaderviewlibrary.LoaderImageView;
import com.elyeproj.loaderviewlibrary.LoaderTextView;

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
        public LoaderTextView title;
        public LoaderImageView thumbnail;
        public View mHolder;

        public MyViewHolder(View view) {
            super(view);
            mHolder = view;
            title = (LoaderTextView) view.findViewById(R.id.title);
            title.resetLoader();
            thumbnail = (LoaderImageView) view.findViewById(R.id.thumbnail);
            thumbnail.resetLoader();
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
      if(brandList.size()>0)
      {
          final Brand brand = brandList.get(position);
          holder.title.setText(brand.getName());
//      loading brand cover using Glide library
          Glide.with(mContext).load(brand.getThumbnail()).into(holder.thumbnail);

          holder.mHolder.setOnClickListener(v -> {
              Intent mIntent = new Intent(v.getContext(),ModelActivity.class);
              mIntent.putExtra("Brand",brand);
              v.getContext().startActivity(mIntent);
          });
//          notifyItemChanged(position);
      }
    }

    @Override
    public int getItemCount() {
        return brandList.size()>0?brandList.size():8;
    }
}
