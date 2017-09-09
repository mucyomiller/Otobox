package rw.sd.otobox.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by miller on 9/3/17.
 */

public class Model implements Parcelable {
    private int id;
    private String name;
    private int thumbnail;

    public Model() {
    }

    public Model(int id, String name, int thumbnail) {
        this.id = id;
        this.name = name;
        this.thumbnail = thumbnail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.thumbnail);
    }

    protected Model(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.thumbnail = in.readInt();
    }

    public static final Parcelable.Creator<Model> CREATOR = new Parcelable.Creator<Model>() {
        @Override
        public Model createFromParcel(Parcel source) {
            return new Model(source);
        }

        @Override
        public Model[] newArray(int size) {
            return new Model[size];
        }
    };
}
