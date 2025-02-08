import java.time.LocalDate;
import java.util.Scanner;

public class BookMyShowactions {

    public static void start() {

        Scanner sc = new Scanner(System.in);
        BookMyShow.getAdminlist().add(new Admin("1", "1"));//admin's new admin name and password
        BookMyShow.getUserlist().add(new User("user", "1","cbe"));//user's new username and password
        while (true) { //while loop
            System.out.println("\nBookMyShow Menu:");
            System.out.print("1. Admin Login \n2. User Login \n3. Exit \nEnter your choice : ");
            int choice = Integer.parseInt(sc.nextLine());//choice for switch

            switch (choice) {
                case 1:
                    Admin admin = Adminactions.adminlogin(sc, BookMyShow.getAdminlist());
                    if (admin == null) {
                        System.out.println("Admin not found");
                    } else if (admin.getUsername() == null) {
                        System.out.println("Invalid admin name,enter correct adminname mamey");
                    } else {
                        adminmenulist(sc);
                    }
                    break;
                case 2:
                    User user = Useractions.userlogin(sc, BookMyShow.getUserlist());
                    if (user == null) {
                        System.out.println("No user found");
                        System.out.println("Do you want to sign up ?[y / n]");
                        String ch = sc.nextLine();
                        if (ch.equals("y")) {
                            Useractions.register(sc, BookMyShow.getUserlist());
                        } else {
                            break;
                        }
                    } else if (user.getUsername() == null) {
                        System.out.println("Invalid username..!");
                        break;
                    } else {
                        System.out.println("Varta mamey,durr!!!");
                        usermenulist(sc,user);// it calls the useractions method, if admin username and password get matched
                    }
                    break;
                case 3:
                    System.out.println("The end...");
                    System.exit(0);
                    return;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }
    }

    public static void adminmenulist(Scanner sc) {
        int input;
        boolean exit = false;
        while (!exit) {
            System.out.println("Admin menu list:");
            System.out.print(" 1.Add theatre \n 2.Add movies \n 3.View Theatres \n 4.View Movies list \n 5.exit \n Enter your choice:");
            input = Integer.parseInt(sc.nextLine());

            switch (input) {
                case 1:
                    //calling the add theatre mathod from admin actions
                    Adminactions.addTheatre(sc);
                    break;
                case 2:
                    Adminactions.addMovie(sc);
                    break;
                case 3:
                    Adminactions.viewTheatres();
                    break;
                case 4:
                    Adminactions.viewMovies();
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }
    }

    public static void usermenulist(Scanner sc,User user) {
        boolean exit = false;
        while (!exit) {
            System.out.println("User menu list:");
            System.out.print(" 1. Book Ticket \n2. Change Location/Date \n3. View Ticket \n4. Exit \nEnter your choice:");
            int input = Integer.parseInt(sc.nextLine());

            switch (input) {
                case 1:
                    Useractions.Movieslist(user);
                    break;
                case 2:
                    Useractions.changeLocationOrDate(user, LocalDate.now());
                    break;
                case 3:
                    Useractions.viewTickets(user);
                    break;
                case 4:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }
    }
}