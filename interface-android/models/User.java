package com.example.liaison.models;

public class User {
    private int id;
    private String name, mail, mdp, type;

    public User(int id, String get_name, String get_mail, String get_mdp, String get_type) {
        this.id = id;
        this.name = name;
        this.mail = mail;
        this.mdp = mdp;
        this.type = type;
    }

    public User() {
    }

    public User(String name, String mail, String mdp, String type) {
        this.name = name;
        this.mail = mail;
        this.mdp = mdp;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

}
