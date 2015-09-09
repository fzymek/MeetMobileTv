package meet.mobile.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Filip Zymek on 2015-06-09.
 */
public class DisplaySize implements Parcelable {
    public static final Parcelable.Creator<DisplaySize> CREATOR = new Parcelable.Creator<DisplaySize>() {
        public DisplaySize createFromParcel(Parcel source) {
            return new DisplaySize(source);
        }

        public DisplaySize[] newArray(int size) {
            return new DisplaySize[size];
        }
    };
    @SerializedName("name")
    String name;
    @SerializedName("uri")
    String uri;

    public DisplaySize() {
    }


    protected DisplaySize(Parcel in) {
        this.name = in.readString();
        this.uri = in.readString();
    }

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }

    @Override
    public String toString() {
        return "DisplaySize{" +
                "name='" + name + '\'' +
                ", uri='" + uri + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.uri);
    }
}
