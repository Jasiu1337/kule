package com.example.circleapp.client;
import com.example.circleapp.Controller;
import com.example.circleapp.Dot;


import javafx.scene.paint.Color;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;

import static com.example.circleapp.server.Server.getSavedDots;
import static com.example.circleapp.server.Server.saveDot;

public class ServerThread extends Thread{
    public Controller controller;
    Socket socket;
    BufferedReader reader;
    PrintWriter writer;

    public ServerThread(String address,int port) {
        try {
            this.socket = new Socket(address,port);
            writer=new PrintWriter(socket.getOutputStream(),true);
            reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void run(){
        String msg;
        try {
            while ((msg = reader.readLine()) != null) {
                if(!msg.isBlank())
                {
                    Dot dot=Dot.FromMessage(msg);
                    controller.drawCircle(dot.x(),dot.y(),dot.radius(),dot.color());
                }
            }
        }catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    public void send(double x, double y, double radius, Color color) {
        writer.println(x + ";" + y + ";" + radius + ";" + color.toString());
    }
}
