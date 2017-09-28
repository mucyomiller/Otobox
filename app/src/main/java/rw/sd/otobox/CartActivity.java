package rw.sd.otobox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.mucyomiller.shoppingcart.model.Cart;
import com.mucyomiller.shoppingcart.model.Saleable;
import com.mucyomiller.shoppingcart.util.CartHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rw.sd.otobox.Adapters.BrandsAdapter;
import rw.sd.otobox.Adapters.CartAdapter;
import rw.sd.otobox.Models.CartItem;
import rw.sd.otobox.Models.Product;

public class CartActivity extends AppCompatActivity {
    private static final String TAG = "CartActivity";
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private List<CartItem> ItemList;
    private Button mCheckout;
    private TextView mTotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Cart");
        final Cart cart = CartHelper.getCart();
        Log.d(TAG, "onCreate: "+cart.getTotalPrice());
        mTotalPrice = (TextView) findViewById(R.id.total_price);
        mTotalPrice.setText(cart.getTotalPrice().toString());
        mCheckout  = (Button) findViewById(R.id.checkout);
        mCheckout.setOnClickListener(v -> {
            Intent mIntent = new Intent(getApplicationContext(),OrderActivity.class);
            startActivity(mIntent);
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        ItemList = new ArrayList<>();
        ItemList = getCartItems(cart);
        adapter = new CartAdapter(this, ItemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

    }
    private List<CartItem> getCartItems(Cart cart) {
        List<CartItem> cartItems = new ArrayList<CartItem>();
        Log.d(TAG, "Current shopping cart: " + cart);

        Map<Saleable, Integer> itemMap = (Map<Saleable, Integer>)cart.getItemWithQuantity();

        for (Map.Entry<Saleable, Integer> entry : itemMap.entrySet()) {
            CartItem cartItem = new CartItem();
            cartItem.setProduct((Product)entry.getKey());
            cartItem.setQuantity(entry.getValue());
            cartItems.add(cartItem);
        }

        Log.d(TAG, "Cart item list: " + cartItems);
        return cartItems;
    }
}
