import java.util.List;
import java.util.HashMap;

public class Screen {
    private String screenName;//name of the screen
    private int noOfSeats;//no of seats in the screen
    private String scrn_grid;
    private HashMap<Character, List<String>>seatarr;
    private int seatCount = 0;

    Screen (String screenName,int noOfSeats,HashMap<Character, List<String>>seatarr,String scrn_grid){
        this.screenName = screenName;
        this.noOfSeats = noOfSeats;
        this.seatarr = seatarr;
        this.scrn_grid = scrn_grid;
    }

    public String getScreenName() {
       return screenName;
   }

   public  int getNoOfSeats() {
       return noOfSeats;
   }

   public HashMap<Character, List<String>> getSeatarr() {
       return seatarr;
   }

   public  String getScrn_grid(){
        return scrn_grid;
   }

   public int getSeatCount(int i) {
       return seatCount;
   }
}
