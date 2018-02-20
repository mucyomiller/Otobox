package rw.sd.otobox.Adapters;

import android.app.PendingIntent;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mucyomiller.shoppingcart.exception.ProductNotFoundException;
import com.mucyomiller.shoppingcart.exception.QuantityOutOfRangeException;
import com.mucyomiller.shoppingcart.model.Cart;
import com.mucyomiller.shoppingcart.util.CartHelper;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import rw.sd.otobox.App;
import rw.sd.otobox.CartActivity;
import rw.sd.otobox.Event.CartEvent;
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

        try {
            //Currency Formatting
            NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
            format.setCurrency(Currency.getInstance("RWF"));
            format.setMinimumFractionDigits(0);

            Cart cart = CartHelper.getCart();
            CartItem mCartItem = itemList.get(position);
            holder.cartItemName.setText(mCartItem.getProduct().getName());
            holder.cartItemUnitPrice.setText(format.format(mCartItem.getProduct().getPrice()));
            holder.cartItemPrice.setText(format.format(cart.getCost(mCartItem.getProduct())));
            holder.cartItemQuantity.setText(String.valueOf(cart.getQuantity(mCartItem.getProduct())));
            holder.itemQuantity.setText(String.valueOf(cart.getQuantity(mCartItem.getProduct())));
            holder.btnAdd.setOnClickListener((View v) -> {
                cart.add(mCartItem.getProduct(), 1);
                //broadcast Cart change Event!
                CartEvent mCartEvent = new CartEvent("ADD",cart);
                ((App)v.getContext().getApplicationContext()).bus().send(mCartEvent);
                notifyItemChanged(position);
            });
            holder.btnMinus.setOnClickListener(v -> {
                try {
                        cart.remove(mCartItem.getProduct(), 1);
                        //broadcast Cart change Event!
                        CartEvent mCartEvent = new CartEvent("REMOVE",cart);
                        ((App)v.getContext().getApplicationContext()).bus().send(mCartEvent);
                } catch (QuantityOutOfRangeException e) {
                    Toasty.error(v.getContext(), "Out of range Error!", Toast.LENGTH_SHORT, true).show();
                } catch (ProductNotFoundException e) {
                    Toasty.error(v.getContext(), "Error Not Found or Deleted!", Toast.LENGTH_SHORT, true).show();
                }
                notifyItemChanged(position);
            });
            // loading brand cover using Glide library
            Glide.with(mContext).load(mCartItem.getProduct().getThumbnail()).into(holder.thumbnail);
        }catch (ProductNotFoundException e){
            Toasty.error(mContext,e.getMessage(),Toast.LENGTH_SHORT,true).show();
        }
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
        public TextView cartItemName,cartItemUnitPrice,cartItemPrice,cartItemQuantity,itemQuantity;
        public ImageView thumbnail;
        public RelativeLayout viewBackground, viewForeground;
        public Button btnMinus,btnAdd;
        public MyViewHolder(View itemView) {
            super(itemView);
            cartItemName = (TextView) itemView.findViewById(R.id.cartItemName);
            cartItemUnitPrice = (TextView) itemView.findViewById(R.id.cartItemUnitPrice);
            cartItemPrice = (TextView) itemView.findViewById(R.id.cartItemPrice);
            cartItemQuantity = (TextView) itemView.findViewById(R.id.cartItemQuantity);
            itemQuantity = (TextView) itemView.findViewById(R.id.itemQuantity);
            btnAdd       = (Button) itemView.findViewById(R.id.qtyadd);
            btnMinus       = (Button) itemView.findViewById(R.id.qtyminus);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            viewBackground = (RelativeLayout) itemView.findViewById(R.id.view_background);
            viewForeground = (RelativeLayout) itemView.findViewById(R.id.view_foreground);

        }
    }

//    public void updateCartItems(List<CartItem> itemList) {
//        this.itemList = itemList;
//        notifyDataSetChanged();
//    }
}
