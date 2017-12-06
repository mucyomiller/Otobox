package rw.sd.otobox.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by miller on 12/5/17.
 */

public class Generation implements Parcelable {
    private String id;
    private String name;
    private String released;
    private String url;

    public Generation() {
    }

    public Generation(String id, String name, String released, String url) {
        this.id = id;
        this.name = name;
        this.released = released;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.released);
        dest.writeString(this.url);
    }

    protected Generation(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.released = in.readString();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<Generation> CREATOR = new Parcelable.Creator<Generation>() {
        @Override
        public Generation createFromParcel(Parcel source) {
            return new Generation(source);
        }

        @Override
        public Generation[] newArray(int size) {
            return new Generation[size];
        }
    };

    @Override
    public String toString() {
        return released ;
    }
}
