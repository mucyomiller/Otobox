package rw.sd.otobox.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

/**
 * Created by miller on 9/7/17.
 */

public class Product implements Parcelable {
    private String title;
    private String quality;
    private int thumbnail;
    private int warranty;
    private BigDecimal price;

    public Product() {
    }

    public Product(String title, String quality, int thumbnail, int warranty, BigDecimal price) {
        this.title = title;
        this.quality = quality;
        this.thumbnail = thumbnail;
        this.warranty = warranty;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getWarranty() {
        return warranty;
    }

    public void setWarranty(int warranty) {
        this.warranty = warranty;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.quality);
        dest.writeInt(this.thumbnail);
        dest.writeInt(this.warranty);
        dest.writeSerializable(this.price);
    }

    protected Product(Parcel in) {
        this.title = in.readString();
        this.quality = in.readString();
        this.thumbnail = in.readInt();
        this.warranty = in.readInt();
        this.price = (BigDecimal) in.readSerializable();
    }

    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
