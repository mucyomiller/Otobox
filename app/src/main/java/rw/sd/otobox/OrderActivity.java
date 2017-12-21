package rw.sd.otobox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mucyomiller.shoppingcart.model.Cart;
import com.mucyomiller.shoppingcart.model.Saleable;
import com.parse.ParseException;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rw.sd.otobox.Models.CartItem;
import rw.sd.otobox.Models.Product;

public class OrderActivity extends AppCompatActivity {
    private static final String TAG = "OrderActivity";
    private Cart cart;
    private EditText names;
    private EditText phone;
    private EditText location;
    private EditText vin;
    private TextView totalprice,vat,totalitems;
    private Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        cart = (Cart) bundle.getParcelable("cart");
        totalprice = (TextView) findViewById(R.id.totalprice);
        vat        = (TextView) findViewById(R.id.vat);
        vin        = (EditText) findViewById(R.id.vin);
        totalitems = (TextView) findViewById(R.id.totalitems);
        totalprice.setText("TOTAL : "+cart.getTotalPrice());
        vat.setText("VAT : "+(cart.getTotalPrice().multiply(BigDecimal.valueOf(18))).divide(BigDecimal.valueOf(100)));
        totalitems.setText("Items : "+cart.getTotalQuantity());

        Log.d(TAG, "onCreate: ");
        names = (EditText) findViewById(R.id.names);
        phone = (EditText) findViewById(R.id.phone);
        location = (EditText) findViewById(R.id.location);
        send = (Button) findViewById(R.id.send);
        send.setOnClickListener(v->{
            if(validate())
            {
                //getting cart items
                List<CartItem> cartItems = new ArrayList<CartItem>();
                Map<Saleable, Integer> itemMap = (Map<Saleable, Integer>)cart.getItemWithQuantity();
                for (Map.Entry<Saleable, Integer> entry : itemMap.entrySet()) {
                    CartItem cartItem = new CartItem();
                    cartItem.setProduct((Product)entry.getKey());
                    cartItem.setQuantity(entry.getValue());
                    cartItems.add(cartItem);
                }
                Gson gson = new Gson();
                String listString = gson.toJson(cartItems, new TypeToken<ArrayList<CartItem>>() {}.getType());
                try {
                    JSONArray itemsArray =  new JSONArray(listString);
                    Log.d(TAG, "onCreate: itemsArray"+itemsArray.toString());
                    ParseObject orderObject = new ParseObject("Order");
                    orderObject.put("names", names.getText().toString());
                    orderObject.put("phone",phone.getText().toString());
                    orderObject.put("location",location.getText().toString());
                    orderObject.put("items",itemsArray);
                    orderObject.put("amount", cart.getTotalPrice());
                    orderObject.put("vat", (cart.getTotalPrice().multiply(BigDecimal.valueOf(18))).divide(BigDecimal.valueOf(100)));
                    orderObject.put("itemcount",cart.getTotalQuantity());
                    orderObject.put("vin",vin.getText().toString());
                    orderObject.save();
                    Toast.makeText(getApplicationContext(),"Order Sent Successfull",Toast.LENGTH_LONG).show();
                    //save basic info is temp storage
                    
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    Toast.makeText(getApplicationContext(),"error occured in saving try again!",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean validate() {
        if(names.getText().toString().isEmpty()){
              names.setError("Amazina Arakenewe!");
              return false;
        }
        if(phone.getText().toString().isEmpty()){
            names.setError("Telephoni Arakenewe!");
            return false;
        }
        if(location.getText().toString().isEmpty()){
            names.setError("Ahubarizwa Hakenewe!");
            return false;
        }
        if(vin.getText().toString().isEmpty()){
            names.setError("Nimero iranga ikinyabiziga irakenewe!");
            return false;
        }
        return true;
    }
}
