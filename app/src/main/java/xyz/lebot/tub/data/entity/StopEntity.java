package xyz.lebot.tub.data.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by axell on 04/11/2016.
 */

public class StopEntity {
    @SerializedName("id")
    private String id;
    @SerializedName("label")
    private String label;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longitude")
    private String longitude;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
