package rw.sd.otobox.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import rw.sd.otobox.BuyActivity;
import rw.sd.otobox.Models.Model;
import rw.sd.otobox.Models.Product;
import rw.sd.otobox.R;

/**
 * Created by miller on 9/7/17.
 */

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Product> productList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail;
        public TextView quality;
        public RatingBar warranty;
        public TextView price;
        public Button addToCart;

        public View mHolder;

        public MyViewHolder(View view) {
            super(view);
            mHolder = view;
            title = (TextView) view.findViewById(R.id.title);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            quality   = (TextView) view.findViewById(R.id.quality);
            warranty  = (RatingBar) view.findViewById(R.id.warrantyRatingBar);
            price     = (TextView) view.findViewById(R.id.price);
            addToCart = (Button) view.findViewById(R.id.addToCart);

        }
    }


    public ProductsAdapter(Context mContext, List<Product> productList) {
        this.mContext = mContext;
        this.productList = productList;
    }

    @Override
    public ProductsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ProductsAdapter.MyViewHolder holder, int position) {
        final Product product = productList.get(position);
        holder.title.setText(product.getTitle());
        holder.quality.setText(product.getQuality());
        holder.warranty.setRating(product.getWarranty());
        holder.price.setText(product.getPrice().toString()+" Rwf");
        holder.addToCart.setOnClickListener(v -> {
            Toast.makeText(mContext, "Added to Cart!", Toast.LENGTH_SHORT).show();
        });

        // loading brand cover using Glide library
        Glide.with(mContext).load(product.getThumbnail()).into(holder.thumbnail);

        holder.mHolder.setOnClickListener(v -> {
//            Intent mIntent = new Intent(v.getContext(), BuyActivity.class);
//            v.getContext().startActivity(mIntent);
            Toast.makeText(mContext, "Clicked All", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}