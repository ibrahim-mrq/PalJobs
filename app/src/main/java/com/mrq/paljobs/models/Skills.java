package com.mrq.paljobs.models;

import org.jetbrains.annotations.NotNull;

public class Skills {

    private String id;
    private String name;

    public Skills() {
    }

    @NotNull
    @Override
    public String toString() {
        return "Skills{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
