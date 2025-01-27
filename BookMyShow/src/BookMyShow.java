import java.time.format.DateTimeFormatter;
import java.util.*;

public class BookMyShow {
    private static List<Admin>adminList = new ArrayList<>();
    private static List<User>userList = new ArrayList<>();
    private static HashMap<String,Theatre> theatreList = new HashMap<>();
    private static HashMap<String,Screen>screenHash = new HashMap<>();
    //private static HashMap<String,List<MovieShow>>movieshowlist = new HashMap<>();
    private static HashMap<String,List<Movie>>movielist = new HashMap<>();
    private static HashMap<String,List<Show>>showlist = new HashMap<>();
    private static DateTimeFormatter dateformat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static DateTimeFormatter timeformat = DateTimeFormatter.ofPattern("HH:mm");




    public static List<Admin> getAdminlist() {
        return adminList;
    }
    public static List<User> getUserlist() {
        return userList;
    }
    public static HashMap<String,Theatre> getTheatrelist(){
        return theatreList;
    }
    public static HashMap<String, Screen> getScreenlist(){
        return screenHash;
    }

//    public static HashMap<String,List<MovieShow>>getMovieshowlist(){
//        return movieshowlist;
//    }

    public static HashMap<String,List<Movie>>getMovielist(){ return movielist;}
    public static HashMap<String,List<Show>>getShowlist(){
        return showlist;
    }
    public static DateTimeFormatter getDateformat() {
        return dateformat;
    }

    public static DateTimeFormatter getTimeformat() {
        return timeformat;
    }
}