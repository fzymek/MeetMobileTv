package meet.mobile.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filip Zymek on 2015-06-08.
 */
public class Image implements Parcelable {

    public static final String INTENT_EXTRA_IMAGE = "video_extra";
    public static final Parcelable.Creator<Image> CREATOR = new Parcelable.Creator<Image>() {
        public Image createFromParcel(Parcel source) {
            return new Image(source);
        }

        public Image[] newArray(int size) {
            return new Image[size];
        }
    };
    @SerializedName("id")
    String id;
    @SerializedName("caption")
    String caption;
    @SerializedName("title")
    String title;
    @SerializedName("artist")
    String artist;
    @SerializedName("collection_name")
    String collectionName;
    @SerializedName("date_created")
    String dateCreated;
    @SerializedName("display_sizes")
    List<DisplaySize> displaySizes;
    private String videoUrl = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";

    public Image() {
    }

    protected Image(Parcel in) {
        this.id = in.readString();
        this.caption = in.readString();
        this.title = in.readString();
        this.artist = in.readString();
        this.collectionName = in.readString();
        this.dateCreated = in.readString();
        this.displaySizes = new ArrayList<>();
        in.readTypedList(displaySizes, DisplaySize.CREATOR);
        this.videoUrl = in.readString();
    }

    public String getId() {
        return id;
    }

    public String getCaption() {
        return caption;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public List<DisplaySize> getDisplaySizes() {
        return displaySizes;
    }

    public DisplaySize getDisplayByType(DisplaySizeType type) {
        for (DisplaySize size : displaySizes) {
            if (size.getName().equals(type.name)) {
                return size;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Image{" +
                "id='" + id + '\'' +
                ", caption='" + caption + '\'' +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", collectionName='" + collectionName + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", displaySizes=" + displaySizes +
                ", videoUrl=" + videoUrl +
                '}';
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.caption);
        dest.writeString(this.title);
        dest.writeString(this.artist);
        dest.writeString(this.collectionName);
        dest.writeString(this.dateCreated);
        dest.writeTypedList(this.displaySizes);
        dest.writeString(this.videoUrl);
    }

    public enum DisplaySizeType {
        THUMB("thumb"),
        PREVIEW("preview"),
        LARGE("comp");

        protected final String name;

        DisplaySizeType(String type) {
            this.name = type;
        }

    }
}
