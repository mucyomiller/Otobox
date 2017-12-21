package rw.sd.otobox.Event;

import android.os.Parcel;
import android.os.Parcelable;

import com.mucyomiller.shoppingcart.model.Cart;

/**
 * Created by miller on 12/18/17.
 */

public class CartEvent implements Parcelable {
    public String action;
    public Cart   mCart;

    public CartEvent() {
    }

    public CartEvent(String action, Cart mCart) {
        this.action = action;
        this.mCart = mCart;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Cart getmCart() {
        return mCart;
    }

    public void setmCart(Cart mCart) {
        this.mCart = mCart;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.action);
        dest.writeParcelable(this.mCart, flags);
    }

    protected CartEvent(Parcel in) {
        this.action = in.readString();
        this.mCart = in.readParcelable(Cart.class.getClassLoader());
    }

    public static final Parcelable.Creator<CartEvent> CREATOR = new Parcelable.Creator<CartEvent>() {
        @Override
        public CartEvent createFromParcel(Parcel source) {
            return new CartEvent(source);
        }

        @Override
        public CartEvent[] newArray(int size) {
            return new CartEvent[size];
        }
    };
}
