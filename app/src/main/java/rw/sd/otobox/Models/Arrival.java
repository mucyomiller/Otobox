package rw.sd.otobox.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by miller on 2/14/18.
 */

public class Arrival implements Parcelable {
    public String brand;
    public String model;
    public String generation;
    public Product product;
    public Date createdAt;

    public Arrival() {
    }

    public Arrival(String brand, String model, String generation, Product product, Date createdAt) {
        this.brand = brand;
        this.model = model;
        this.generation = generation;
        this.product = product;
        this.createdAt = createdAt;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getGeneration() {
        return generation;
    }

    public void setGeneration(String generation) {
        this.generation = generation;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.brand);
        dest.writeString(this.model);
        dest.writeString(this.generation);
        dest.writeParcelable(this.product, flags);
        dest.writeLong(this.createdAt != null ? this.createdAt.getTime() : -1);
    }

    protected Arrival(Parcel in) {
        this.brand = in.readString();
        this.model = in.readString();
        this.generation = in.readString();
        this.product = in.readParcelable(Product.class.getClassLoader());
        long tmpCreatedAt = in.readLong();
        this.createdAt = tmpCreatedAt == -1 ? null : new Date(tmpCreatedAt);
    }

    public static final Parcelable.Creator<Arrival> CREATOR = new Parcelable.Creator<Arrival>() {
        @Override
        public Arrival createFromParcel(Parcel source) {
            return new Arrival(source);
        }

        @Override
        public Arrival[] newArray(int size) {
            return new Arrival[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Arrival arrival = (Arrival) o;

        if (!brand.equals(arrival.brand)) return false;
        if (!model.equals(arrival.model)) return false;
        if (!generation.equals(arrival.generation)) return false;
        if (!product.equals(arrival.product)) return false;
        return createdAt.equals(arrival.createdAt);
    }

    @Override
    public int hashCode() {
        int result = brand.hashCode();
        result = 31 * result + model.hashCode();
        result = 31 * result + generation.hashCode();
        result = 31 * result + product.hashCode();
        result = 31 * result + createdAt.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Arrival{" +
                "brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", generation='" + generation + '\'' +
                ", product=" + product +
                ", createdAt=" + createdAt +
                '}';
    }
}
