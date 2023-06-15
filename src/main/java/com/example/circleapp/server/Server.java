package com.example.circleapp.server;

import com.example.circleapp.Dot;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Server {
    ServerSocket serverSocket;
    List<ClientThread> clients=new ArrayList<>();
    static Connection connection;
    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void listen() throws SQLException {
        Thread listen = new Thread(() ->{
        while (true)
        {
            try {
                Socket socket=serverSocket.accept();
                ClientThread client=new ClientThread(socket,this);
                clients.add(client);
                System.out.println("client connected");
                client.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }});
        listen.start();
    }
    void broadcast(String msg) throws SQLException {
        saveDot(Dot.FromMessage(msg));
        for(ClientThread c:clients)
        {
            c.writer.println(msg);
        }
    }

    public void removeClient(ClientThread clientThread) {
        clients.remove(clientThread);
    }
    public static void saveDot(Dot dot) throws SQLException {
        String values=dot.x()+","+ dot.y()+",'"+dot.color().toString()+"',"+dot.radius();
        PreparedStatement query=connection.prepareStatement("INSERT INTO dot(x,y,color,radius) VALUES("+values+")");
        query.execute();
    }
    public static void dropDatabase() throws SQLException {
        PreparedStatement query=connection.prepareStatement("DELETE FROM DOT;");
        query.execute();
    }
    public static void getConnection()
    {
        try {
            connection=DriverManager.getConnection("jdbc:sqlite:C:\\Users\\jantr\\IdeaProjects\\demo1\\src\\main\\java\\com\\example\\circleapp\\dots.db");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } {
    }
    }
    public static List<Dot> getSavedDots() throws SQLException {
        List<Dot>dots=new ArrayList<>();
        PreparedStatement query=connection.prepareStatement("SELECT x,y,radius,color from dot;");
        query.execute();
        ResultSet set=query.getResultSet();
        while (set.next())
        {
            double x=set.getDouble("x");
            double y=set.getDouble("y");
            double radius=set.getDouble("radius");
            Color color=Color.valueOf(set.getString("color"));
            dots.add(new Dot(x,y,radius,color));
        }
        return dots;
    }
}
