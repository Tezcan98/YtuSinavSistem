package com.example.enestezcan;

public class Kullanici implements java.io.Serializable {

    private Integer kullaniciId;
    private String Name;
    private String Email;
    private String password;
    private String imagePath;

    public Kullanici(Integer kullaniciId, String name, String email, String password, String imagePath) {
        this.kullaniciId = kullaniciId;
        Name = name;
        Email = email;
        this.password = password;
        this.imagePath = imagePath;
    }

    public Kullanici(String name, String email, String password, String imagePath) {
        Name = name;
        Email = email;
        this.password = password;
        this.imagePath = imagePath;
    }

    public String getName() {
        return Name;
    }
    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return Email;
    }

    public Integer getKullaniciId() {
        return kullaniciId;
    }

    public Boolean LoginVerify(String email, String password){
        if(this.Email.equals(email) && this.password.equals(password))
            return true;
        return false;
    }

    public String getImagePath() {
        return imagePath;
    }

    @Override
    public String toString() {
        return "Kullanici{" +
                "Name='" + Name + '\'' +
                ", Email='" + Email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
