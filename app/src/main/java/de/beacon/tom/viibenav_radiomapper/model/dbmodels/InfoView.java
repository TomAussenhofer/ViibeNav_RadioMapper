package de.beacon.tom.viibenav_radiomapper.model.dbmodels;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by TomTheBomb on 27.08.2015.
 */
public class InfoView {

    private int id;
    private String person_name;
    private String room_name;
    private String environment;
    private String category;

    private static ArrayList<InfoView> all = new ArrayList<>();

    public InfoView(int id, String person_name, String room_name, String environment, String category) {
        this.id = id;
        this.person_name = person_name;
        this.room_name = room_name;
        this.environment = environment;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public String getPerson_name() {
        return person_name;
    }

    public String getRoom_name() {
        return room_name;
    }

    public String getEnvironment() {
        return environment;
    }

    public String getCategory() {
        return category;
    }

    public static ArrayList<InfoView> getAll() {
        return all;
    }

    public static void setAll(ArrayList<InfoView> all) {
        InfoView.all = all;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InfoView infoView = (InfoView) o;
        return Objects.equals(id, infoView.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
