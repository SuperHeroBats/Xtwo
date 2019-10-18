package com.xtwo.android;

public class Memory {
    private String name;
    private int imageId;

    public Memory(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }
}
