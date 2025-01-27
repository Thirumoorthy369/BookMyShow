import java.time.LocalDate;

public class Movie {
    private Show show;
    private Screen screen;
    private Theatre theatre;
    private int duration;
    private String movie_name;
    private LocalDate start_date;
    private String location;

    public Movie(String movie_name,String location, LocalDate start_date,int duration,Theatre theatre,Screen screen,Show show){
        this.movie_name = movie_name;
        this.location = location;
        this.start_date = start_date;
        this.duration = duration;
        this.theatre = theatre;
        this.screen = screen;
        this.show = show;
    }
    public Show getShow(){
        return show;
    }

    public Screen getScreen(){
        return screen;
    }

    public Theatre getTheatre(){
        return theatre;
    }

    public int getDuration(){
        return duration;
    }

    public String getMovie_name(){
        return movie_name;
    }

    public LocalDate getStart_date(){
        return start_date;
    }

    public String getLocation(){
        return location;
    }

    public void setmovie_name(String movie_name){
        this.movie_name = movie_name;
    }
}
