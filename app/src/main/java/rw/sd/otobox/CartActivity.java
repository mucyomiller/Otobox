package rw.sd.otobox;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
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

public class CartActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    private static final String TAG = "CartActivity";
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private List<CartItem> ItemList;
    private Button mCheckout;
    private TextView mTotalPrice;
    private RelativeLayout main_content;
    private  Cart cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Cart");
        cart = CartHelper.getCart();
        Log.d(TAG, "onCreate: "+cart.getTotalPrice());
        main_content = (RelativeLayout) findViewById(R.id.main_content);
        mTotalPrice = (TextView) findViewById(R.id.total_price);
        mTotalPrice.setText(cart.getTotalPrice().toString());
        mCheckout  = (Button) findViewById(R.id.checkout);
        mCheckout.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("cart",cart);
            Intent mIntent = new Intent(getApplicationContext(),OrderActivity.class);
            mIntent.putExtras(bundle);
            startActivity(mIntent);
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        ItemList = new ArrayList<>();
        ItemList = getCartItems(cart);
        adapter = new CartAdapter(this, ItemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
//
//        ItemTouchHelper.SimpleCallback itemTouchHelperCallback1 = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP) {
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//                // Row is swiped from recycler view
//                // remove it from adapter
//            }
//
//            @Override
//            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//            }
//        };
//        // attaching the touch helper to recycler view
//        new ItemTouchHelper(itemTouchHelperCallback1).attachToRecyclerView(recyclerView);


    }


    /**
     * callback when recycler view is swiped
     * item will be removed on swiped
     * undo option will be provided in snackbar to restore the item
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CartAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            String name = ItemList.get(viewHolder.getAdapterPosition()).getProduct().getName();

            // backup of removed item for undo purpose
            final CartItem deletedItem = ItemList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            adapter.removeItem(viewHolder.getAdapterPosition());
            // remove it also from cart
            cart.remove(deletedItem.getProduct());
            mTotalPrice.setText(cart.getTotalPrice().toString());
            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(main_content, name + " removed from cart!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    adapter.restoreItem(deletedItem, deletedIndex);
                    cart.add(deletedItem.getProduct(),deletedItem.getQuantity());
                    mTotalPrice.setText(cart.getTotalPrice().toString());
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
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
