package Lab.planmytrip;


import java.util.Date;

public class User {

    public String fname, lname, passw, email, phone;
    public Date bdate;

    public User() {
    }

    public User(String fname, String lname, String email, String passw, String phone, Date date) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.passw = passw;
        this.bdate = date;
        this.phone = phone;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getPassw() {
        return passw;
    }

    public void setPassw(String passw) {
        this.passw = passw;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getBdate() {
        return bdate;
    }

    public void setBdate(Date bdate) {
        this.bdate = bdate;
    }
}
