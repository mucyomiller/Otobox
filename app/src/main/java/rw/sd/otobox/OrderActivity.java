package rw.sd.otobox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mucyomiller.shoppingcart.model.Cart;

import java.math.BigDecimal;

public class OrderActivity extends AppCompatActivity {

    private Cart cart;
    private EditText names;
    private EditText phone;
    private EditText location;
    private TextView totalprice,vat,totalitems;
    private Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        cart = (Cart) bundle.getSerializable("cart");
        totalprice = (TextView) findViewById(R.id.totalprice);
        vat        = (TextView) findViewById(R.id.vat);
        totalitems = (TextView) findViewById(R.id.totalitems);
        totalprice.setText("TOTAL : "+cart.getTotalPrice());
        vat.setText("VAT : "+(cart.getTotalPrice().multiply(BigDecimal.valueOf(18))).divide(BigDecimal.valueOf(100)));
        totalitems.setText("Items : "+cart.getTotalQuantity());

        names = (EditText) findViewById(R.id.names);
        phone = (EditText) findViewById(R.id.phone);
        location = (EditText) findViewById(R.id.location);
        send = (Button) findViewById(R.id.send);
        send.setOnClickListener(v->{
            if(validate())
            {
                Toast.makeText(getApplicationContext(),"Order Sent Successfull",Toast.LENGTH_LONG).show();
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
        return true;
    }
}
