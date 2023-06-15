package com.example.circleapp.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;

public class ClientThread extends Thread{
Socket socket;
Server server;
BufferedReader reader;
PrintWriter writer;

    public ClientThread(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        try {
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
                    server.broadcast(msg);
                }
            }
            server.removeClient(this);
            socket.close();
        }catch (IOException | SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
}
