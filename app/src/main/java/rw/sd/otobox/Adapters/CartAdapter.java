package rw.sd.otobox.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mucyomiller.shoppingcart.model.Cart;
import com.mucyomiller.shoppingcart.util.CartHelper;

import java.util.List;

import rw.sd.otobox.CartActivity;
import rw.sd.otobox.Models.CartItem;
import rw.sd.otobox.R;

/**
 * Created by miller on 9/11/17.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    private static final String TAG = "CartAdapter";
    private Context mContext;
    private List<CartItem> itemList;

    public CartAdapter(Context mContext,List<CartItem> itemList){
        this.mContext = mContext;
        this.itemList = itemList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Cart cart = CartHelper.getCart();
        CartItem mCartItem = itemList.get(position);
        holder.cartItemName.setText(mCartItem.getProduct().getName());
        holder.cartItemUnitPrice.setText(mCartItem.getProduct().getPrice().toString());
        holder.cartItemPrice.setText(cart.getCost(mCartItem.getProduct()).toString());
        holder.cartItemQuantity.setText(String.valueOf(mCartItem.getQuantity()));
        // loading brand cover using Glide library
        Glide.with(mContext).load(mCartItem.getProduct().getThumbnail()).into(holder.thumbnail);
        holder.mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                Log.d(TAG, "onProgressChanged: "+progress);
                cart.add(mCartItem.getProduct(),progress);
                notifyItemChanged(position);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void removeItem(int position) {
        itemList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(CartItem item, int position) {
        itemList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView cartItemName,cartItemUnitPrice,cartItemPrice,cartItemQuantity;
        public ImageView thumbnail;
        public SeekBar mSeekBar;
        public RelativeLayout viewBackground, viewForeground;
        public MyViewHolder(View itemView) {
            super(itemView);
            cartItemName = (TextView) itemView.findViewById(R.id.cartItemName);
            cartItemUnitPrice = (TextView) itemView.findViewById(R.id.cartItemUnitPrice);
            cartItemPrice = (TextView) itemView.findViewById(R.id.cartItemPrice);
            cartItemQuantity = (TextView) itemView.findViewById(R.id.cartItemQuantity);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            mSeekBar = (SeekBar) itemView.findViewById(R.id.seekBar);
            viewBackground = (RelativeLayout) itemView.findViewById(R.id.view_background);
            viewForeground = (RelativeLayout) itemView.findViewById(R.id.view_foreground);

        }
    }

//    public void updateCartItems(List<CartItem> itemList) {
//        this.itemList = itemList;
//        notifyDataSetChanged();
//    }
}
