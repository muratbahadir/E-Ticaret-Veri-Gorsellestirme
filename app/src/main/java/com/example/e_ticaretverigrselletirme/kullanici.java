package com.example.e_ticaretverigrselletirme;

//veritabanına yazılacak nesnenin oluşturulduğu sınıf
public class kullanici {
    String username,password;
    int idd;

    public kullanici() {

    }

    public kullanici(String username, String password,int idd) {
        this.username = username;
        this.password = password;
        this.idd=idd;
    }

    public int getIdd() {
        return idd;
    }

    public void setIdd(int idd) {
        this.idd = idd;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
