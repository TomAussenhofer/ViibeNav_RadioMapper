package de.beacon.tom.viibenav_radiomapper.model.dbmodels;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by TomTheBomb on 24.07.2015.
 */
public class OnyxBeaconView {

    private int id,major,minor;
    private String macAddress,uuid;

    private static ArrayList<OnyxBeaconView> all = new ArrayList<>();

    public OnyxBeaconView(int id, int major, int minor, String macAddress, String uuid) {
        this.id = id;
        this.major = major;
        this.minor = minor;
        this.macAddress = macAddress;
        this.uuid = uuid;
    }

    public static ArrayList<OnyxBeaconView> getAll() {
        return all;
    }

    public static void setAll(ArrayList<OnyxBeaconView> all) {
        OnyxBeaconView.all = all;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OnyxBeaconView that = (OnyxBeaconView) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
