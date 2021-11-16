package Lab.planmytrip;


import java.util.Date;

public class User {

    public String fname, lname, passw, email, phone;
    public Date date;

    public User() {
    }

    public User(String fname, String lname, String email, String passw, String phone, Date date) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.passw = passw;
        this.date = date;
        this.phone = phone;
    }
}
