import java.time.LocalTime;
import java.util.List;

public class Ticket {

    private String theatre ;
    private String movie;
    private String screen;
    private String location;
    private LocalTime startTime;
    private List<String> bookedTicket;
    private int price;

    public Ticket(String theatre, String movie, String screen, String location, LocalTime startTime, List<String> bookedTicket, int price) {
        this.theatre = theatre;
        this.movie = movie;
        this.screen = screen;
        this.location = location;
        this.startTime = startTime;
        this.bookedTicket = bookedTicket;
        this.price = price;
    }

    public String getTheatre() {
        return theatre;
    }

    public String getMovie() {
        return movie;
    }

    public String getScreen() {
        return screen;
    }

    public String getLocation() {
        return location;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public List<String> getBookedTicket() {
        return bookedTicket;
    }

    public int getPrice() {
        return price;
    }
}

