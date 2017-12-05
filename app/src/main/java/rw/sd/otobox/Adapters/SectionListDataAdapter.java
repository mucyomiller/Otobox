package rw.sd.otobox.Adapters;

/**
 * Created by miller on 11/29/17.
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import rw.sd.otobox.Models.Product;
import rw.sd.otobox.R;

public class SectionListDataAdapter extends RecyclerView.Adapter<SectionListDataAdapter.SingleItemRowHolder> {

    private ArrayList<Product> itemsList;
    private Context mContext;

    public SectionListDataAdapter(Context context, ArrayList<Product> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_single_card, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int i) {

        Product singleItem = itemsList.get(i);

        holder.tvTitle.setText(singleItem.getName());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.fallback(R.drawable.otobox_start);
        requestOptions.error(R.drawable.otobox_start);
        requestOptions.placeholder(R.drawable.otobox_start);

        Glide.with(mContext)
                .load(singleItem.getThumbnail())
                .apply(requestOptions)
                .into(holder.itemImage);
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView tvTitle;

        protected ImageView itemImage;

        public SingleItemRowHolder(View view) {
            super(view);
            this.tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            this.itemImage = (ImageView) view.findViewById(R.id.itemImage);
            view.setOnClickListener(v -> Toast.makeText(v.getContext(), tvTitle.getText(), Toast.LENGTH_SHORT).show());
        }
    }

}