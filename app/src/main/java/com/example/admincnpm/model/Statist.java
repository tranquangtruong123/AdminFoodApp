package com.example.admincnpm.model;

public class Statist {
    private String name;
    private String sl;

    public Statist() {
    }

    public Statist(String name, String sl) {
        this.name = name;
        this.sl = sl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSl() {
        return sl;
    }

    public void setSl(String sl) {
        this.sl = sl;
    }
}
