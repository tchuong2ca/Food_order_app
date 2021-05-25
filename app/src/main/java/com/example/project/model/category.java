package com.example.project.model;

import java.io.Serializable;

public class category implements Serializable {
    private String image, name;
public category(){}
    public category(String image, String name) {
        this.image = image;
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
