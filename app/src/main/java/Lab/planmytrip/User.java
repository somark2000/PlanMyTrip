package Lab.planmytrip;

public class User {

    public String fname,lname,passw,email,date,phone;

    public User(){}

    public User(String fname, String lname, String passw, String email, String date,String phone) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.passw = passw;
        this.date = date;
        this.phone = phone;
    }
}
