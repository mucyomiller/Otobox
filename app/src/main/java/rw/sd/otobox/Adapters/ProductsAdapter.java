package rw.sd.otobox.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mucyomiller.shoppingcart.model.Cart;
import com.mucyomiller.shoppingcart.util.CartHelper;

import java.util.List;

import es.dmoral.toasty.Toasty;
import rw.sd.otobox.BuyActivity;
import rw.sd.otobox.Models.Model;
import rw.sd.otobox.Models.Product;
import rw.sd.otobox.ProductDetailActivity;
import rw.sd.otobox.R;

/**
 * Created by miller on 9/7/17.
 */

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyViewHolder> {

    private static final String TAG = "ProductsAdapter";

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
        holder.title.setText(product.getName());
        holder.quality.setText(product.getQuality());
        holder.warranty.setRating(product.getWarranty());
        holder.price.setText(product.getPrice().toString()+" Rwf");
        holder.addToCart.setOnClickListener(v -> {
            Cart cart =  CartHelper.getCart();
            Log.d(TAG, "Adding product: " + product.getName());
            cart.add(product,1);
            Toasty.success(mContext, product.getName()+" added to Cart!", Toast.LENGTH_SHORT).show();
        });

        // loading brand cover using Glide library
        Glide.with(mContext).load(product.getThumbnail()).into(holder.thumbnail);
        //set listener that goes to product detail activity
        holder.mHolder.setOnClickListener(v -> {
           Intent mIntent = new Intent(v.getContext(), ProductDetailActivity.class);
           mIntent.putExtra("product",product);
           v.getContext().startActivity(mIntent);
        });
    }

    public void removeAll(){
        productList.clear();
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}