import java.time.*;
import java.util.*;


public class Show {
    private Screen screen;
    private LocalTime start_time;
    private LocalTime end_time;
    private LocalDate date;
    private int price;
    private HashMap<Character, List<String>>seatarr = new HashMap<>();


    public Show(LocalTime start_time, LocalTime end_time, Screen screen, int price, HashMap<Character, List<String>>seatarr) {
        this.start_time = start_time;
        this.end_time = end_time;
        this.screen = screen;
        this.price = price;
        this.seatarr = seatarr;

    }

    public Screen getScreen(){
        return screen;
    }

    public LocalTime getStart_time(){
        return start_time;
    }

    public LocalTime getEnd_time(){
        return end_time;
    }

    public int getPrice(){
        return price;
    }

    public HashMap<Character, List<String>>getSeatarr(){
        return seatarr;
    }

    public void setSeatarrngment(HashMap<Character, List<String>> seatarr) {
        this.seatarr = seatarr;
    }
}

