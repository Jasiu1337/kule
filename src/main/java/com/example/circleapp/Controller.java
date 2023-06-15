package com.example.circleapp;
import com.example.circleapp.client.ServerThread;
import com.example.circleapp.server.Server;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import java.sql.SQLException;
import static com.example.circleapp.server.Server.*;

public class Controller {
    Server server;
    ServerThread serverThread;
    public TextField addressField;
    public TextField portField;
    public ColorPicker colorPicker;
    public Slider radiusSlider;
    public Canvas canvas;
    public void onStartServerClicked() throws SQLException {
        getConnection();
        System.out.println("connected to database");
        dropDatabase();
        String address=addressField.getText();
        int port= Integer.parseInt(portField.getText());
        server=new Server(5000);
        server.listen();
        serverThread=new ServerThread(address,port);
        serverThread.controller=this;
        serverThread.start();
    }

    public void onConnectClicked(){
        String address=addressField.getText();
        int port= Integer.parseInt(portField.getText());
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