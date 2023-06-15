package com.example.circleapp;

import com.example.circleapp.client.ServerThread;
import com.example.circleapp.server.Server;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.sql.SQLException;
import java.util.StringJoiner;

import static com.example.circleapp.server.Server.*;

public class Controller {
    Server server;
    ServerThread serverThread;
    public TextField addressField;
    public TextField portField;
    public ColorPicker colorPicker;
    public Slider radiusSlider;
    public Canvas canvas;
    public void onStartServerClicked(ActionEvent actionEvent) throws SQLException {
        getConnection();
        System.out.println("connected to database");
        dropDatabase();
        String address=addressField.getText();
        int port= Integer.parseInt(portField.getText());
        //System.out.println(address+port);
        server=new Server(5000);
        server.listen();
        serverThread=new ServerThread(address,port);
        serverThread.controller=this;
        serverThread.start();
    }

    public void onConnectClicked(ActionEvent actionEvent) throws SQLException {
        getConnection();
        String address=addressField.getText();
        int port= Integer.parseInt(portField.getText());
            for(Dot dot:getSavedDots())
            {
                drawCircle(dot.x(),dot.y(),dot.radius(),dot.color());
            }
        serverThread=new ServerThread(address,port);
        serverThread.controller=this;
        serverThread.start();
    }

    public void onMouseClicked(MouseEvent mouseEvent) {
        double x=mouseEvent.getX();
        double y=mouseEvent.getY();
        double radius=radiusSlider.getValue();
        Color color=colorPicker.getValue();
        //drawCircle(x,y,radius,color);
        serverThread.send(x,y,radius,color);
    }
    public void drawCircle(double x, double y, double radius, Color color)
    {
       canvas.getGraphicsContext2D().setFill(color);
       canvas.getGraphicsContext2D().fillOval(x-radius/2,y-radius/2,radius,radius);
    }
}