package com.example.deannhom.utils;

public class Tuple<isDarkMode, Editor> {
    public final isDarkMode isDarkModeOn;
    public final Editor editor;

    public Tuple(isDarkMode isDarkMode, Editor editor) {
        this.isDarkModeOn = isDarkMode;
        this.editor = editor;
    }
}
