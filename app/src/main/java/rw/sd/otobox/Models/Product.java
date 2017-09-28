package rw.sd.otobox.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.mucyomiller.shoppingcart.model.Saleable;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by miller on 9/7/17.
 */

public class Product implements Saleable, Serializable, Parcelable {
    
    private static final long serialVersionUID = -4073256626483275668L;
    
    private int pId;
    private String name;
    private String quality;
    private int thumbnail;
    private int warranty;
    private BigDecimal price;

    public Product() {
    }

    public Product(int pId,String name, String quality, int thumbnail, int warranty, BigDecimal price) {
        this.pId = pId;
        this.name = name;
        this.quality = quality;
        this.thumbnail = thumbnail;
        this.warranty = warranty;
        this.price = price;
    }

    public int getpId() {return pId;}

    public void setpId(int pId) {this.pId = pId;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

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

    public int hashCode() {
        final int prime = 31;
        int hash = 1;
        hash = hash* prime + pId;
        hash = hash * prime + (name == null?0:name.hashCode());
        hash = hash * prime + (price==null?0:price.hashCode());
        hash = hash * prime + (quality == null?0:quality.hashCode());
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof Product)) return false;
        return (this.pId == ((Product)o).getpId());
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.pId);
        dest.writeString(this.name);
        dest.writeString(this.quality);
        dest.writeInt(this.thumbnail);
        dest.writeInt(this.warranty);
        dest.writeSerializable(this.price);
    }

    protected Product(Parcel in) {
        this.pId = in.readInt();
        this.name = in.readString();
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
