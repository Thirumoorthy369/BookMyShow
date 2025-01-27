import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;// Username for the user
    private String password;// Password to store pass
    private String location;// Location of the user
    private List<Ticket>tickets = new ArrayList<>();
    private String newlocation;

    public User(String username, String password, String location) { //Constructor for the user class
        this.username = username;
        this.password = password;
        this.location = location;
    }

    public String getUsername() { //Getter for the username
        return username;
    }

    public String getPassword() {  //getter for the password
        return password;
    }

    public String getLocation() { //Getter for the location
        return location;
    }

    public List<Ticket> getTickets() { return tickets;}


    public void setLocation(String newlocation) {
        this.newlocation = newlocation;
    }
}
