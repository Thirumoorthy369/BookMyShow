import java.time.*;
import java.util.*;

public class Useractions {

    public static User userlogin(Scanner sc, List<User> userlist) {
        System.out.print("Enter user Name:");
        String name = sc.nextLine();
        for (User um : userlist) {
            if (um.getUsername().equals(name)) {
                System.out.println("Enter password: ");
                String password = sc.nextLine();
                if (um.getUsername().equals(name) && um.getPassword().equals(password)) {
                    return um;
                } else {
                    System.out.println("Invalid credentials...");
                    return new User(null, null, null);
                }
            }
        }
        return null;
    }

    public static void register(Scanner sc, List<User> userlist) {
        System.out.println("Enter the new Username :");
        String name = sc.nextLine();
        System.out.println("Enter the pass");
        String pass = sc.nextLine();
        System.out.print("Enter your Location :");
        String location = sc.nextLine();
        userlist.add(new User(name, pass, location));
        System.out.println("User added !");
    }

    public static void Movieslist(User user, LocalDate currentdate) {
        Scanner sc = new Scanner(System.in);
        List<Movie> movielist = new ArrayList<>();
        System.out.println("========================");

        LocalDate today = LocalDate.now();
        System.out.println("Now available movies in your Location  :" + user.getLocation());
        boolean movieisthere = false;

        for (String moviename : BookMyShow.getMovielist().keySet()) {
            for (Movie movie : BookMyShow.getMovielist().get(moviename)) {
                if (movie.getLocation().equalsIgnoreCase(user.getLocation()) && today.equals(movie.getStart_date())) {
                    System.out.println("=>" + movie.getMovie_name());
                    movieisthere = true;
                }
            }
        }

        if (!movieisthere) {
            System.out.println("No movie Available in your location on current date!");
            return;
        }

        System.out.println(" Would you like to change the (Location or Date): [yes = 1 | no = 0]");
        int ch = Integer.parseInt(sc.nextLine());

        if (ch == 1) {
            LocalDate newDate = changeLocationorDate(user, today);
            if (!(newDate == null)) {
                today = newDate;
            }

            for (String movieName : BookMyShow.getMovielist().keySet()) {
                for (Movie movies : BookMyShow.getMovielist().get(movieName)) {
                    if (movies.getLocation().equalsIgnoreCase(user.getLocation()) && today.equals(movies.getStart_date())) {
                        System.out.println("=>" + movies.getMovie_name());
                    }
                }
            }
        }

        System.out.println("Enter the movie name to book tickets :");
        String movietobook = sc.nextLine();
        List<String> mnlist = new ArrayList<>(BookMyShow.getMovielist().keySet());

        if (mnlist.contains(movietobook)) {
            for (var movieobject : BookMyShow.getMovielist().get(movietobook)) {
                if (movieobject.getLocation().equalsIgnoreCase(user.getLocation()) && movieobject.getStart_date().isEqual(today)) {
                    movielist.add(movieobject);
                }
            }
            bookticket(user, movielist, currentdate, new Screen());
        } else {
            System.out.println("No movie available as you entered !");
        }
    }

    public static void bookticket(User user, List<Movie> movies, LocalDate currentdate, Screen screen) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Available Theatres");
        HashMap<String, HashSet<Show>> ShowonTheatre = new HashMap<>();

        for (var movie : movies) {
            ShowonTheatre.computeIfAbsent(movie.getTheatre().getTheatreName(), k -> new HashSet<>()).add(movie.getShow());
        }

        for (String theatreName : ShowonTheatre.keySet()) {
            System.out.println("Theatre Name :" + theatreName);
            System.out.println("Shows        :" + ShowonTheatre.get(theatreName).toString());
        }

        while (true) {
            System.out.println("Enter the name of theatre :");
            String theName = sc.nextLine();
            ArrayList<String> thlist = new ArrayList<>(ShowonTheatre.keySet());

            if (thlist.contains(theName)) {
                System.out.println("Enter the show time :");
                LocalTime showTime = LocalTime.parse(sc.nextLine(), BookMyShow.getTimeformat());

                HashSet<Show> show = ShowonTheatre.get(theName);
                if (show == null) {
                    System.out.println("Invalid Show !");
                    continue;
                }

                Show nowShow = null;
                for (Show shows : show) {
                    if (shows.getStart_time().equals(showTime)) {
                        nowShow = shows;
                        break;
                    }
                }

                if (nowShow == null) {
                    System.out.println("Enter the valid time of show !");
                    continue;
                } else {
                    System.out.println("Screen Name :" + nowShow.getScreen().getScreenName());
                    System.out.println("No. of Seats:" + nowShow.getScreen().getNoOfSeats());
                    var seatGrids = nowShow.getScreen().getSeatarr();
                    System.out.println("Seat Arrangement :\n=========================");

                    for (var seats : seatGrids.entrySet()) {
                        System.out.println(seatGrids.get(seats.getKey()) + " " + seats.getValue());
                    }
                }

                System.out.println("Enter the no. of seats to book :");
                int seatsCount = Integer.parseInt(sc.nextLine());
                int price = seatsCount * nowShow.getPrice();
                var bookedTicket = seatSelection(seatsCount, nowShow, screen);

                if (!(bookedTicket == null)) {
                    System.out.println("Ticket Amount paying Rs.:" + price);
                    Ticket ticket = new Ticket(theName, movies.get(0).getMovie_name(), nowShow.getScreen().getScreenName(), user.getLocation(), showTime, bookedTicket, price);
                    user.getTickets().add(ticket);
                } else {
                    System.out.println("Booking Cancelled !");
                    System.out.println("Exiting...!");
                }
            } else {
                System.out.println("Invalid Theatre name !");
                break;
            }
        }
    }

    public static void viewTickets(User user) {
        if (user.getTickets().isEmpty()) {
            System.out.println("No Tickets booked !");
            return;
        }
        List<Ticket> ticket = user.getTickets();
        for (Ticket tickets : ticket) {
            System.out.println("============================");
            System.out.println("Theatre Name : " + tickets.getTheatre());
            System.out.println("Theatre Location : " + tickets.getLocation());
            System.out.println("Movie Name : " + tickets.getMovie());
            System.out.println("Screen Name : " + tickets.getScreen());
            System.out.println("Show Time : " + tickets.getStartTime());
            System.out.println("Booked Seats : " + tickets.getBookedTicket());
            System.out.println("Price : " + tickets.getPrice());
            System.out.println("============================");
        }
    }


    public static LocalDate changeLocationorDate(User user, LocalDate today) {
        Scanner sc = new Scanner(System.in);
        System.out.println("1.Location \n 2.Date \n 3. Exit \n Enter your choice :");
        int choice = Integer.parseInt(sc.nextLine());
        switch (choice) {
            case 1:
                System.out.println("Your Location :" + user.getLocation());
                System.out.println("Available Locations:");
                var availableLocations = new HashSet<>();
                for (Theatre theatre : BookMyShow.getTheatrelist().values()) {
                    availableLocations.add(theatre.getLocation());
                }
                for (var location : availableLocations) {
                    System.out.println("=>" + location);
                }
                System.out.println("Enter your new Location :");
                String newLocation = sc.nextLine();
                if (availableLocations.contains(newLocation)) {
                    user.setLocation(newLocation);
                    System.out.println("Location changed Successfully to " + newLocation);
                } else {
                    System.out.println("Location not valid !");
                }
                break;
            case 2:
                x:
                while (true) {
                    System.out.println("Enter your new date");
                    try {
                        LocalDate newDate = LocalDate.parse(sc.nextLine(), BookMyShow.getDateformat());
                        if (newDate.isAfter(today) || newDate.isEqual(today)) {
                            System.out.println("Date changed successfully to :" + newDate);
                            return newDate;
                        }
                    } catch (Exception e) {
                        System.out.println("please enter the valid date !");
                        continue x;
                    }
                    break;
                }
                break;
            case 3:
                System.out.println("Exiting...");
                return null;
            default:
                System.out.println("Invalid choice !");
        }
        return null;
    }


    public static ArrayList<String> seatSelection(int seatCount, Show show, Screen screen) {
        Scanner sc = new Scanner(System.in);
        var seatGrid = show.getSeatarr();
        ArrayList<String> bookedTickets = new ArrayList<>();

        while (seatCount > 0) {
            System.out.println("Enter the Seat number to book :");
            String seatnumber = sc.nextLine();
            char row = seatnumber.charAt(0);
            int seatIndex = Integer.parseInt(seatnumber.substring(1)) - 1;

            if (seatIndex >= 0 && seatIndex < seatGrid.get(row).size()) {
                if (seatGrid.get(row).get(seatIndex).equals("X")) {
                    System.out.println("This seat is already booked !");
                    continue;
                } else {
                    seatGrid.get(row).set(seatIndex, "X");
                    bookedTickets.add(seatnumber);
                    seatCount--;
                }
            } else {
                System.out.println("Invalid seat number !");
            }

            System.out.println("Updated Seat Arrangement :");
            for (var seats : seatGrid.entrySet()) {
                System.out.println(seats.getKey() + " " + seats.getValue());
            }
        }

        System.out.println("Confirm your seat booking! [yes = 1 / no = 0]");
        int confirm = Integer.parseInt(sc.nextLine());

        if (confirm == 1) {
            System.out.println("Tickets booked successfully !");
            return bookedTickets;
        } else {
            System.out.println("Booking cancelled !");
            for (String booked : bookedTickets) {
                char row = booked.charAt(0);
                int seatIndex = Integer.parseInt(booked.substring(1)) - 1;
                seatGrid.get(row).set(seatIndex, Integer.toString(seatIndex + 1));
            }
            return null;
        }
    }
}
