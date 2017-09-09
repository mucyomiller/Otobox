package rw.sd.otobox.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mucyo miller on 9/1/17.
 */

public class Brand implements Parcelable {
    private String name;
    private int numOfModels;
    private int thumbnail;

    public Brand() {
    }

    public Brand(String name, int numOfModels, int thumbnail) {
        this.name = name;
        this.numOfModels = numOfModels;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumOfModels() {
        return numOfModels;
    }

    public void setNumOfModels(int numOfModels) {
        this.numOfModels = numOfModels;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.numOfModels);
        dest.writeInt(this.thumbnail);
    }

    protected Brand(Parcel in) {
        this.name = in.readString();
        this.numOfModels = in.readInt();
        this.thumbnail = in.readInt();
    }

    public static final Parcelable.Creator<Brand> CREATOR = new Parcelable.Creator<Brand>() {
        @Override
        public Brand createFromParcel(Parcel source) {
            return new Brand(source);
        }

        @Override
        public Brand[] newArray(int size) {
            return new Brand[size];
        }
    };
}