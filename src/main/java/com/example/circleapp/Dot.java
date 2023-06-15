package com.example.circleapp;

import javafx.scene.paint.Color;

public record Dot(double x, double y, double radius,Color color) {
    public static String toMessage(double x, double y, double radius,Color color)
    {
        return x+";"+y+";"+radius+";"+color.toString();
    }
    public static Dot FromMessage(String message)
    {
        String split[]=message.split(";",4);
        return new Dot(Double.parseDouble(split[0]),Double.parseDouble(split[1]),Double.parseDouble(split[2]),Color.valueOf(split[3]));
    }
}
