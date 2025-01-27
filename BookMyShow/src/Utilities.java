import java.util.*;

public class Utilities {
    static HashMap<Character, List<String>> generatingpatternseat(int noofseats, String screengrid){
        int count =noofseats;
        String[] splitscreen= screengrid.split("\\*");
        int sum=0;

        int counter = 0;
        for(String grid: splitscreen){
            int temp = Integer.parseInt(grid);
            sum=sum+temp; //2*4*2
            counter++;
        }
        if(count % sum == 0)
        {
            var seatArr = new HashMap<Character,List<String>>();
            char ch = 'A';
            while(count>0){
                List<String> row = new ArrayList<>();
                for(int i = 0 ; i < splitscreen.length  ; i++){
                    for(int j = 0 ; j <Integer.parseInt(splitscreen[i]) ; j++){
                        row.add("{_} ");
                    }
                    if(i < screengrid.length() - counter){
                        row.add("<-----> ");
                    }
                }
                seatArr.put(ch,row);
                ch++;
                count=count-sum;
            }
            return seatArr;
        }
        System.out.println("not a valid grid value");
        return null;
    }
}