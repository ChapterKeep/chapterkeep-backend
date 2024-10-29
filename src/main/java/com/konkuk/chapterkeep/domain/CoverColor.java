package com.konkuk.chapterkeep.domain;

public enum CoverColor {

    RED("#FF5733"),
    GREEN("#33FF57"),
    BLUE("#3357FF"),
    PINK("#FF33A1"),
    YELLOW("#F1C40F"),
    ORANGE("#FFA500"),
    PURPLE("#800080"),
    BLACK("#000000"),
    WHITE("#FFFFFF");

    private final String hexCode;

    CoverColor(String hexCode) {
        this.hexCode = hexCode;
    }

    public String getHexCode() {
        return hexCode;
    }
}