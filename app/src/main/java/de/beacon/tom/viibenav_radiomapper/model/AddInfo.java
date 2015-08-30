package de.beacon.tom.viibenav_radiomapper.model;

/**
 * Created by TomTheBomb on 27.08.2015.
 *
 * This class contains additional Info to a specific AnchorPoint
 */
public class AddInfo {

    private String person_name;
    private String room_name;
    private String environment;
    private String category;

    public AddInfo(String person_name, String room_name, String environment, String category) {
        this.person_name = person_name;
        this.room_name = room_name;
        this.environment = environment;
        this.category = category;
    }

    public AddInfo() {
        person_name = "";
        room_name = "";
        environment = "";
        category = "";
    }

    public boolean hasAddInfo(){
        if(hasRoomInfo() || hasPersonInfo() || hasEnvironmentInfo())
            return true;
        return false;
    }

    public boolean hasRoomInfo(){
        return !room_name.isEmpty();
    }
    public boolean hasPersonInfo(){
        return !person_name.isEmpty();
    }
    public boolean hasCategoryInfo(){
        return !category.isEmpty();
    }
    public boolean hasEnvironmentInfo(){
        return !environment.isEmpty();
    }




    public void reset(){
        person_name = "";
        room_name = "";
        environment = "";
        category = "";
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

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPerson_name(String person_name) {
        this.person_name = person_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }
}
