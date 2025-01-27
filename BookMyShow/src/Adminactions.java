import java.util.*;
import java.time.*;


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
            System.out.print("Enter the name of the screen:");
            String scrn_name = scanner.nextLine();
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

        Theatre theatre = new Theatre(theatrename, screenHashMap, location);
        BookMyShow.getTheatrelist().put(theatrename, theatre);
        System.out.println("Theatre added successfully!");
    }


    public static void addMovie(Scanner scanner) {

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

        System.out.println("Enter the theatre name:");
        String theatreName = scanner.nextLine();

        if (!BookMyShow.getTheatrelist().containsKey(theatreName)) {
            System.out.println("Theatre not found!");
            return;
        }

        Theatre theatre = BookMyShow.getTheatrelist().get(theatreName);

        System.out.println("Enter the screen name:");
        String screenName = scanner.nextLine();

        if (!theatre.getScreenwrap().containsKey(screenName)) {
            System.out.println("Screen not found!");
            return;
        }

        Screen screen = theatre.getScreenwrap().get(screenName);

        System.out.print("Enter the start time of the Show (HH:mm): ");
        LocalTime startingTime = LocalTime.parse(scanner.nextLine(), BookMyShow.getTimeformat());
        LocalTime endingTime = startingTime.plusMinutes(duration + 30); // Including buffer time

        // Check if the show timing conflicts with existing shows
        boolean conflict = false;
        for (var existingShow : BookMyShow.getShowlist().getOrDefault(screen.getScreenName(), new ArrayList<>())) {
            if ((startingTime.isBefore(existingShow.getEnd_time()) && startingTime.isAfter(existingShow.getStart_time())) ||
                    (endingTime.isAfter(existingShow.getStart_time()) && endingTime.isBefore(existingShow.getEnd_time())) ||
                    startingTime.equals(existingShow.getStart_time()) ||
                    endingTime.equals(existingShow.getEnd_time())) {
                conflict = true;
                break;
            }
        }

        if (conflict) {
            System.out.println("Show timing conflicts with an existing show. Please choose a different time.");
            return;
        }
        System.out.println("Enter the price of the single ticket :");
        int price = 0;
        try {
            //gettingt he price for the ticket
            price = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid ticket price!");
        }

        var seatArrangement = Utilities.generatingpatternseat(screen.getNoOfSeats(), screen.getScrn_grid());
        Show newShow = new Show(startingTime, endingTime, screen, new ArrayList<>(Admin.showTimings), price, seatArrangement);

        BookMyShow.getShowlist().computeIfAbsent(screen.getScreenName(), k -> new ArrayList<>()).add(newShow);

        LocalDate startDate = null, endDate = null;
        System.out.println("Enter the start date (dd-MM-yyyy):");
        try {
            startDate = LocalDate.parse(scanner.nextLine(), BookMyShow.getDateformat());
        } catch (Exception e) {
            System.out.println("Invalid format..!");
        }

        System.out.println("Enter the end date (dd-MM-yyyy):");
        try {
            endDate = LocalDate.parse(scanner.nextLine(), BookMyShow.getDateformat());
        } catch (Exception e) {
            System.out.println("Invalid format..!");
        }

        System.out.print("Enter the number of show timings: ");
        int numberOfTimings = Integer.parseInt(scanner.nextLine());
        List<LocalTime> showTimings = new ArrayList<>();

        for (int i = 1; i <= numberOfTimings; i++) {
            System.out.print("Enter show time " + i + " (HH:mm): ");
            showTimings.add(LocalTime.parse(scanner.nextLine()));
        }

        Admin.showTimings.clear();
        Admin.showTimings.addAll(showTimings);

        Movie newMovie = new Movie(movieName, location, startDate, (int) duration, theatre, screen, newShow);
        BookMyShow.getMovielist().computeIfAbsent(movieName, k -> new ArrayList<>()).add(newMovie);

        System.out.println("Movie successfully scheduled!");
        System.out.println("Movie Name: " + movieName);
        System.out.println("Theatre: " + theatreName);
        System.out.println("Screen: " + screenName);
        System.out.println("Dates: " + startDate + " to " + endDate);
        System.out.println("Show Timings:");
        for (LocalTime time : Admin.showTimings) {
            System.out.println(" - " + time);
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
                System.out.println("  Show Timings: ");

                for (LocalTime time : show.getShowTimings()) {
                    System.out.println("    - " + time);
                }
                System.out.println();
            }
        }
    }

}