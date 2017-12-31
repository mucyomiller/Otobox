package rw.sd.otobox;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mucyomiller.shoppingcart.exception.ProductNotFoundException;
import com.mucyomiller.shoppingcart.model.Cart;
import com.mucyomiller.shoppingcart.model.Saleable;
import com.mucyomiller.shoppingcart.util.CartHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import rw.sd.otobox.Adapters.CartAdapter;
import rw.sd.otobox.Event.CartEvent;
import rw.sd.otobox.Models.CartItem;
import rw.sd.otobox.Models.Product;

public class CartActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    private static final String TAG = "CartActivity";
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private List<CartItem> ItemList;
    private Button mCheckout;
    static public TextView mTotalPrice;
    private RelativeLayout main_content;
    private  Cart cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Cart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        cart = CartHelper.getCart();
        Log.d(TAG, "onCreate: "+cart.getTotalPrice());
        main_content = (RelativeLayout) findViewById(R.id.main_content);
        mTotalPrice = (TextView) findViewById(R.id.total_price);

        mTotalPrice.setText(cart.getTotalPrice().toString()+" RWF");
        ((App)getApplication()).bus().toObservable().subscribe(event -> {
            if(event instanceof CartEvent){
                Log.d(TAG, " RxBus cart change event detected type of! =>"+((CartEvent) event).getAction());
                mTotalPrice.setText(cart.getTotalPrice().toString()+" RWF");
            }
        });
        mCheckout  = (Button) findViewById(R.id.checkout);
        mCheckout.setOnClickListener(v -> {
            if(cart.getTotalQuantity()>0){
                Intent mIntent = new Intent(getApplicationContext(),OrderActivity.class);
                mIntent.putExtra("cart",cart);
                startActivity(mIntent);
            }else
            {
                Toasty.warning(v.getContext(),"Please add product on cart!", Toast.LENGTH_SHORT,true).show();
            }
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
            try {
                // remove it also from cart
                cart.remove(deletedItem.getProduct());
                CartEvent mCartEvent = new CartEvent("REMOVE",cart);
                ((App)getApplication()).bus().send(mCartEvent);
//                mTotalPrice.setText(cart.getTotalPrice().toString());
                // showing snack bar with Undo option
                Snackbar snackbar = Snackbar
                        .make(main_content, name + " removed from cart!", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // undo is selected, restore the deleted item
                        adapter.restoreItem(deletedItem, deletedIndex);
                        cart.add(deletedItem.getProduct(),deletedItem.getQuantity());
                        CartEvent mCartEvent = new CartEvent("ADD",cart);
                        ((App)getApplication()).bus().send(mCartEvent);
//                        mTotalPrice.setText(cart.getTotalPrice().toString());
                    }
                });
                snackbar.setActionTextColor(Color.CYAN);
                snackbar.show();
            }catch (ProductNotFoundException e){
                Toasty.error(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT,true).show();
            }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case android.R.id.home:
                this.onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
