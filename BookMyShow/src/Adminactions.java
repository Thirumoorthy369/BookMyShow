import java.time.*;
import java.util.*;

public class Adminactions {

    public static Admin adminlogin(Scanner sc, List<Admin> adminlist) {
        System.out.println("Enter the Admin sign in: ");
        String username = sc.nextLine();
            for (Admin am : adminlist) {
                if (am.getUsername().equals(username)) {
                    System.out.println("Enter password: ");
                    String password = sc.nextLine();
                    if (am.getUsername().equals(username) && am.getPassword().equals(password)) {
                        return am;
                    } else {
                        System.out.println("Invalid password");
                    }
                } else {
                    return new Admin(null,"0");
                }
            }
        return null;
    }

    public static void addTheatre(Scanner scanner) {
        System.out.println("Enter the theatre name to add:");
        String theatrename = scanner.nextLine();
        System.out.println("Enter the location:");
        String location = scanner.nextLine();
        for (var temp : BookMyShow.getTheatrelist().keySet()) {
            var currenttheatre = BookMyShow.getTheatrelist().get(temp);
            if (temp.equals(theatrename) && currenttheatre.getLocation().equals(location)) {
                System.out.println("Theatre is already exist!");
                return;
            }
        }
        System.out.println("Enter the no.of Screens:");
        int screens = Integer.parseInt(scanner.nextLine());
        HashMap<String, Screen> screenHashMap = new HashMap<>();
        while (screens != 0) {
            for(int i = 1;i<=screens;i++) {
                System.out.print("Enter the name of the screen:");
                String scrn_name = scanner.nextLine();

                if (screenHashMap.containsKey(scrn_name)){
                    System.out.println("The given screen name is already exist in the theatre, give some unique name..!");
                    continue;
                }
                System.out.print("Enter the no.of seats  :");
                int seats_count = Integer.parseInt(scanner.nextLine());
                System.out.print("Enter the grid (eg: 2*4*2):");
                String scrgrid = scanner.nextLine();
                var griden = Utilities.generatingpatternseat(seats_count, scrgrid);
                if (griden == null) {
                    continue;
                }
                Screen screen = (new Screen(scrn_name, seats_count, griden, scrgrid));
                screenHashMap.put(scrn_name, screen);
                screens--;
            }
        }

        Theatre theatre = new Theatre(theatrename, screenHashMap, location);
        BookMyShow.getTheatrelist().put(theatrename, theatre);
        System.out.println("Theatre added successfully!");
    }

    public static void addMovie(Scanner scanner) {
        String screenName;

        System.out.println("Enter the movie name:");
        String movieName = scanner.nextLine();

        System.out.println("Enter the Location:");
        String location = scanner.nextLine();
        Theatre selectedTheatre = null;

        // Find the theatre based on location
        for (var theatreEntry : BookMyShow.getTheatrelist().entrySet()) {
            var theatre = theatreEntry.getValue();
            if (theatre.getLocation().equals(location)) {
                selectedTheatre = theatre;
                break;
            }
        }

        if (selectedTheatre == null) {
            System.out.println("Theatre not found at the specified location!");
            return;
        }

        System.out.println("Enter the Duration of the movie (in minutes):");
        long duration = Long.parseLong(scanner.nextLine());

        System.out.println("Available theatres");
        for (var theatreEntry : BookMyShow.getTheatrelist().entrySet()) {
            var theatre = theatreEntry.getValue();
            System.out.println(theatre.getTheatreName());
        }

        System.out.println("Enter the theatre name:");
        String theatreName = scanner.nextLine();

        if (!BookMyShow.getTheatrelist().containsKey(theatreName)) {
            System.out.println("Theatre not found!");
            return;
        }

        Theatre theatre = BookMyShow.getTheatrelist().get(theatreName);

        System.out.println("Available Screens:");
        for (var screenEntry : theatre.getScreenwrap().entrySet()) {
            var screen = screenEntry.getValue();
            System.out.println(screen.getScreenName());
        }

        System.out.println("Enter the screen name:");
        screenName = scanner.nextLine();

        x :while (true) {
            for (var movies : theatre.getScreenwrap().entrySet()) {
                if (!movies.getKey().contains(screenName)) {
                    System.out.println("Screen not found!");
                    continue x;
                }
                else {
                    break x;
                }
            }
        }

        Screen screen = theatre.getScreenwrap().get(screenName);

        System.out.print("Enter the start time of the Show (HH:mm): ");
        LocalTime startingTime = LocalTime.parse(scanner.nextLine(), BookMyShow.getTimeformat());
        LocalTime endingTime = startingTime.plusMinutes(duration + 30); // Including buffer time

        // Check if the show timing conflicts with existing shows
        boolean interrupt = false;
        for (var existingShow : BookMyShow.getShowlist().getOrDefault(screen.getScreenName(), new ArrayList<>())) {
            if ((startingTime.isBefore(existingShow.getEnd_time()) && startingTime.isAfter(existingShow.getStart_time())) ||
                    (endingTime.isAfter(existingShow.getStart_time()) && endingTime.isBefore(existingShow.getEnd_time())) ||
                    startingTime.equals(existingShow.getStart_time()) ||
                    endingTime.equals(existingShow.getEnd_time())) {
                interrupt = true;
                break;
            }
        }

        if (interrupt) {
            System.out.println("Show timing interrupts with an existing show. Please choose a different time.");
            return;
        }

        System.out.println("Enter the price of the single ticket :");
        int price = 0;
        try {
            //getting the price for the ticket
            price = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid ticket price!");
        }

        var seatArrangement = Utilities.generatingpatternseat(screen.getNoOfSeats(), screen.getScrn_grid());
        Show newShow = new Show(startingTime, endingTime, screen, price, seatArrangement);

        // Check if the movie already exists in the same screen
        List<Movie> movies = BookMyShow.getMovielist().get(movieName);
        if (movies == null) {
            movies = new ArrayList<>();
        }

        boolean movieExists = false;
        for (Movie movie : movies) {
            if (movie.getScreen().getScreenName().equals(screenName) && movie.getStart_date().isEqual(LocalDate.now())) {
                movieExists = true;
                break;
            }
        }
         if (!movieExists) {
             BookMyShow.getShowlist().computeIfAbsent(screen.getScreenName(), k -> new ArrayList<>()).add(newShow);
             LocalDate startDate = null;
             System.out.println("Enter the start date (dd-MM-yyyy):");
             try {
                 startDate = LocalDate.parse(scanner.nextLine(), BookMyShow.getDateformat());
             } catch (Exception e) {
                 System.out.println("Invalid format..!");
             }

             Movie newMovie = new Movie(movieName, location, startDate, (int) duration, theatre, screen, newShow);
             BookMyShow.getMovielist().computeIfAbsent(movieName, k -> new ArrayList<>()).add(newMovie);

             System.out.println("Movie successfully scheduled!");
             System.out.println("Movie Name: " + movieName);
             System.out.println("Theatre: " + theatreName);
             System.out.println("Screen: " + screenName);
             System.out.println("Dates: " + startDate);
             System.out.println("Show Timings:" + startingTime);
         } else {
             System.out.println("Movie already scheduled in this screen at the same time.");
         }
    }

    public static void viewTheatres() {
        for (var temp : BookMyShow.getTheatrelist().keySet()) {
            var theatre = BookMyShow.getTheatrelist().get(temp);

            System.out.println("Name :" + theatre.getTheatreName());
            System.out.println("location:" + theatre.getLocation());
            System.out.println("Screens are...");
            System.out.println("--------------");
            for (var tmp : theatre.getScreenwrap().entrySet()) {
                System.out.println("Screen name     :" + tmp.getValue().getScreenName());
                System.out.println("Number of seats :" + tmp.getValue().getNoOfSeats());
                System.out.println("Seat Arrangement:\n");
                for (var tem : tmp.getValue().getSeatarr().keySet()) {
                    var tp = tmp.getValue().getSeatarr().get(tem);
                    System.out.print(tem);
                    System.out.println(tp);
                }
            }
        }
    }

    public static void viewMovies() {
        // Check if there are any movies in the system
        if (BookMyShow.getMovielist().isEmpty()) {
            System.out.println("No movies are available to watch in theatres.");
            return;
        }

        // Iterate through the movie list and display details
        for (var entry : BookMyShow.getMovielist().entrySet()) {
            String movieName = entry.getKey();
            List<Movie> movies = entry.getValue();

            System.out.println("============================");
            System.out.println("Movie Name: " + movieName);

            for (Movie movie : movies) {
                System.out.println("- Theatre: " + movie.getTheatre().getTheatreName());
                System.out.println("  Location: " + movie.getLocation());
                System.out.println("  Screen: " + movie.getScreen().getScreenName());
                System.out.println("  Duration: " + movie.getDuration() + " minutes");
                System.out.println("  Start Date: " + movie.getStart_date());

                // Display associated shows and timings
                Show show = movie.getShow();
                System.out.println("  Show Start Time: " + show.getStart_time());
                System.out.println("  Show End Time: " + show.getEnd_time());
                System.out.println("  Show Timings: "+ show.getStart_time()+ " to " + show.getEnd_time());

                System.out.println();
            }
        }
    }
}
