import java.util.HashMap;

public class Theatre {
    private String theatreName;
    private String location;
    private HashMap<String, Screen> screenwrap;

    public Theatre (String name,HashMap<String,Screen> screenwrap,String location){
        this.theatreName = name;
        this.location = location;
        this.screenwrap = screenwrap;
    }

    public HashMap<String, Screen> getScreenwrap() {
        return screenwrap;
    }

    public String getTheatreName() {
        return theatreName;
    }

    public String getLocation() {
        return location;
    }
}
