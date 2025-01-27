import java.time.LocalTime;
import java.util.ArrayList;

public class Admin {
    private String username;// Username for the user
    private String password;// Password to store pass
    static ArrayList<LocalTime> showTimings = new ArrayList<>();

    protected Admin(String username, String password){
        this.username = username;
        this.password = password;

    }


    public String getUsername() { //Getter for the username
        return username;
    }

    public String getPassword() {  //getter for the password
        return password;
    }

    public static ArrayList<LocalTime> getLocaltime() {
        return showTimings;
    }
}