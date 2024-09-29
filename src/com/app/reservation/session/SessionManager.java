package com.app.reservation.session;

import com.app.reservation.database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class SessionManager {
    private static String username;

    private static long  id;

    public static boolean verifyUserCredentials(String username, String password){

        boolean response = authentication(username,password);
        return response;
    }

    private static boolean authentication(String userName, String pass){

        String query = "SELECT * FROM users WHERE name =? AND password =?";

        try {
            Connection connection = DBConnection.connect();

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,userName);
            preparedStatement.setString(2,pass);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                id = resultSet.getLong("id");
                username = userName;
                return true;
            }
        }
        catch (SQLException e ){
            System.err.println(e.getMessage());
        }


        return false;
    }

    public static void registerUser(String username, String password){

        Connection connection = DBConnection.connect();

        String query = "INSERT INTO users(name, password) VALUES (?,?)";

        try(PreparedStatement preparedStatement = connection.prepareStatement(query)){

            preparedStatement.setString(1,username);
            preparedStatement.setString(2,password);
            preparedStatement.executeUpdate();
            System.out.println("User created!!");

        }
        catch (SQLException e){

            System.err.println(e.getMessage());
        }
        finally {
            setUserDetails(username);

            if(connection != null) {

                try {

                    connection.close();
                    System.out.println("Connection closed.");
                }
                catch (SQLException e){

                    System.err.println(e.getMessage());
                }
            }
        }

    }

    public static void setUserDetails(String uName){
        username = uName;

    }
    public static String getUsername(){
        return username;
    }
    public static long getId(){
        return id;
    }
}
