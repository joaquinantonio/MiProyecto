package com.ja.miproyecto;

public class Users {
    private String name,email, status, user_type;

    public Users() {
    }

    public Users(String name, String email, String status, String user_type) {
        this.name = name;
        this.email = email;
        this.status = status;
        this.user_type = user_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
