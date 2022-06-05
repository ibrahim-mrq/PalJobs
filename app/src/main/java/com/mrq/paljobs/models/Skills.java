package com.mrq.paljobs.models;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Skills {

    private String name;
    private ArrayList<String> skills;

    public Skills() {
    }

    public Skills(String name, ArrayList<String> skills) {
        this.name = name;
        this.skills = skills;
    }

    @NotNull
    @Override
    public String toString() {
        return "Skills{" +
                "name='" + name + '\'' +
                ", skills=" + skills +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getSkills() {
        return skills;
    }

    public void setSkills(ArrayList<String> skills) {
        this.skills = skills;
    }
}
