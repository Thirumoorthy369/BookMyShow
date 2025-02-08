import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class Useractions {

    public static User userlogin(Scanner scanner, List<User> userList) {
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        for (User user : userList) {
            if (user.getUsername().equals(username)) {
                System.out.print("Enter Password: ");
                String password = scanner.nextLine();
                if (user.getPassword().equals(password)) {
                    System.out.println("Login Successful!");
                    return user;
                } else {
                    System.out.println("Invalid Password!");
                    return new User(null, null, null);
                }
            }
        }
        return null; // If no matching user is found
    }

    public static void register(Scanner scanner, List<User> userList) {
        System.out.print("Enter New Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        System.out.print("Enter Location: ");
        String location = scanner.nextLine();
        userList.add(new User(username, password, location));
        System.out.println("User Registered Successfully!");
    }

    public static void Movieslist(User user) {
        Scanner scanner = new Scanner(System.in);
        List<Movie> availableMovies = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        LocalDate selectedDate = currentDate;

        while (true) {
            System.out.println("Current Date: " + currentDate.format(BookMyShow.getDateformat()));
            System.out.println("Selected Date: " + selectedDate.format(BookMyShow.getDateformat()));
            System.out.println("Current Location: " + user.getLocation());

            boolean moviesFound = displayAvailableMovies(user, selectedDate);
            if (!moviesFound) {
                System.out.println("No movies available for the selected date or location");
                LocalDate updatedDate = changeLocationOrDate(user, currentDate);
                if (updatedDate != null) {
                    selectedDate = updatedDate;
                } else {
                    return; // Exit if no changes are made
                }
            } else {
                System.out.print("If you want to change your location or date type 'modify' or enter the Movie name:");
                String movieName = scanner.nextLine();
                if (movieName.equalsIgnoreCase("modify")) {
                    LocalDate updatedDate = changeLocationOrDate(user, currentDate);
                    if (updatedDate != null) {
                        selectedDate = updatedDate;
                    }
                } else {
                    for (Movie movie : BookMyShow.getMovielist().getOrDefault(movieName, new ArrayList<>())) {
                        if (movie.getLocation().equals(user.getLocation()) && movie.getStart_date().isEqual(selectedDate)) {
                            availableMovies.add(movie);
                        }
                    }
                    break;
                }
            }
        }

        if (!availableMovies.isEmpty()) {
            bookTicket(user, availableMovies, selectedDate);
        } else {
            System.out.println("No movies found for the selected criteria.");
        }
    }

    private static boolean displayAvailableMovies(User user, LocalDate date) {
        boolean find = false;
        for (String movieName : BookMyShow.getMovielist().keySet()) {
            for (Movie movie : BookMyShow.getMovielist().get(movieName)) {
                if (movie.getLocation().equals(user.getLocation()) && movie.getStart_date().isEqual(date)) {
                    System.out.println("-->" + movieName);
                    find = true;
                }
            }
        }
        return find;
    }

    public static void bookTicket(User user, List<Movie> movieList, LocalDate selectedDate) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Available Theatres and Shows:");
        HashMap<String, HashSet<Show>> Showsintheatre = new HashMap<>();

        for (Movie movie : movieList) {
            Showsintheatre.computeIfAbsent(movie.getTheatre().getTheatreName(), k -> new HashSet<>()).add(movie.getShow());
        }
         /*
         Alternative of this code line (101 - 103) is -->

        String theatreName = movie.getTheatre().getTheatreName();

        if (!Showsintheatre.containsKey(theatreName)) {
            Showsintheatre.put(theatreName, new HashSet<>());
        }

        Showsintheatre.get(theatreName).add(movie.getShow());
        */



        for (String theatreName : Showsintheatre.keySet()) {
            System.out.println("Theatre: " + theatreName);
            for (Show show : Showsintheatre.get(theatreName)) {
                System.out.println("  Show Starting Time: " + show.getStart_time());
            }
        }

        System.out.print("Enter the  Theatre Name: ");
        String selectedTheatre = scanner.nextLine();
        System.out.print("Enter Show Start Time (HH:mm): ");
        LocalTime selectedShowTime = LocalTime.parse(scanner.nextLine(), BookMyShow.getTimeformat());

        Show selectedShow = null;
        for (Show show : Showsintheatre.getOrDefault(selectedTheatre, new HashSet<>())) {
            if (show.getStart_time().equals(selectedShowTime)) {
                selectedShow = show;
                break;
            }
        }

        if (selectedShow == null) {
            System.out.println("Invalid Theatre or Show Time.");
            return;
        }

        System.out.println("Available Seats:");
        for (var entry : selectedShow.getSeatarr().entrySet()) {
            System.out.println("Row: " + entry.getKey() + " " + entry.getValue());
        }

        System.out.print("Enter the Number of Seats to Book: ");
        int seatCount = Integer.parseInt(scanner.nextLine());
        List<String> bookedSeats = selectSeats(seatCount, selectedShow);

        if (bookedSeats != null) {
            int totalPrice = seatCount * selectedShow.getPrice();
            System.out.println("Total Price: " + totalPrice);
            user.getTickets().add(new Ticket(selectedTheatre, movieList.get(0).getMovie_name(),
                    selectedShow.getScreen().getScreenName(), user.getLocation(),
                    selectedShow.getStart_time(), bookedSeats, totalPrice));

            System.out.println();
            System.out.println("Booking Confirmed!");
        } else {
            System.out.println("Booking Canceled.");
        }
    }

    private static List<String> selectSeats(int seatCount, Show show) {
        Scanner scanner = new Scanner(System.in);
        List<String> bookedSeats = new ArrayList<>();
        var seatArrangement = show.getSeatarr();

        while (seatCount > 0) {
            System.out.print("Enter Seat (e.g., A1): ");
            String seat = scanner.nextLine();
            char row = seat.charAt(0);
            int index = Integer.parseInt(seat.substring(1)) - 1;

            if (seatArrangement.containsKey(row) && index < seatArrangement.get(row).size()) {
                if (seatArrangement.get(row).get(index).equals("X")) {
                    System.out.println("Seat already booked!");
                } else {
                    seatArrangement.get(row).set(index, "X");
                    bookedSeats.add(seat);
                    System.out.println(seatArrangement);

                    seatCount--;
                }
            } else {
                System.out.println("Invalid Seat!");
            }
        }
        return bookedSeats;
    }

    public static LocalDate changeLocationOrDate(User user, LocalDate today) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("What you want to Change --> location or date");
        String choice = scanner.nextLine();

        if (choice.equalsIgnoreCase("location")) {
            System.out.println("Current Location: " + user.getLocation());
            System.out.println("Available Locations:");
            Set<String> availableLocations = new HashSet<>();
            for (Theatre theatre : BookMyShow.getTheatrelist().values()) {
                availableLocations.add(theatre.getLocation());
            }
            for (String location : availableLocations) {
                System.out.println("* " + location);
            }
            System.out.print("Enter New Location: ");
            String newLocation = scanner.nextLine();
            if (availableLocations.contains(newLocation)) {
                user.setLocation(newLocation);
                System.out.println("Location Changed Successfully!");
            } else {
                System.out.println("Invalid Location!");
            }
        } else if (choice.equalsIgnoreCase("date")) {
            System.out.print("Enter New Date (dd-MM-yyyy): ");
            LocalDate newDate = LocalDate.parse(scanner.nextLine(), BookMyShow.getDateformat());
            if (!newDate.isBefore(today)) {
                System.out.println("Date Changed Successfully!");
                return newDate;
            } else {
                System.out.println("Invalid Date!");
            }
        }
        return null;
    }

    public static void viewTickets(User user) {
        if (user.getTickets().isEmpty()) {
            System.out.println("No Tickets Booked!");
            return;
        }
        List<Ticket> tickets = user.getTickets();
        for (Ticket ticket : tickets) {
            System.out.println("============================");
            System.out.println("Theatre Name: " + ticket.getTheatre());
            System.out.println("Theatre Location: " + ticket.getLocation());
            System.out.println("Movie Name: " + ticket.getMovie());
            System.out.println("Screen Name: " + ticket.getScreen());
            System.out.println("Show Time: " + ticket.getStartTime());
            System.out.println("Booked Seats: " + ticket.getBookedTicket());
            System.out.println("Price: " + ticket.getPrice());
            System.out.println("============================");
        }
    }
}