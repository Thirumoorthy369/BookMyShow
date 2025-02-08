

public class Admin {
    private String username;// Username for the user
    private String password;// Password to store pass

    protected Admin(String username, String password){
        this.username = username;
        this.password = password;

    }


    public String getUsername() { //Getter for the username
        return username;
    }

    public String getPassword() {  //getter for the password
        return password;
    }

}