package com.example.db_hw;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MyDB {
    String username = "kurt";
    String password = "x";
    String url = "jdbc:mysql://127.0.0.1:3306/?user=kurt/?serverTimezone=UTC";
//    String url = "jdbc:mysql://1.2.3.4:3306/?useSSL=false";
    String schemaName = "mydb";
    String tableName = "owners";
    List<String > persons = new ArrayList();

    // Helt perfekt b
    public MyDB(){
        connectAndQuery();
    }

    private void connectAndQuery(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try(Connection conn = DriverManager.getConnection(url, username,password)){
            if(!conn.isClosed()){
                System.out.println("DB Conn ok ");
                initializeDatabase(conn);
//                // Get the rows:
                String sql = "SELECT * FROM " + tableName;
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet resultSet = ps.executeQuery();
                persons.clear();
                while (resultSet.next()){
                    String firstName = resultSet.getString("name");
                    System.out.println("Name: " + firstName);
                    persons.add(firstName);
                }
            }
        }catch (Exception e){
            System.out.println("Error " + e.getMessage());
        }
    }

    private void initializeDatabase(Connection conn) throws Exception{
            conn.createStatement().execute("create schema if not exists " + schemaName);
        conn.createStatement().execute("USE " + schemaName);
            System.out.println("Schema ok");
        ResultSet rs;
        try {
            rs = conn.createStatement().executeQuery("SELECT * FROM " + tableName);
            System.out.println("Table ok");
        } catch (Exception e) {
            conn.createStatement().execute("CREATE TABLE "+ tableName +
                            " (idpeersons int not null AUTO_INCREMENT PRIMARY KEY ," +
            " name varchar(45) DEFAULT NULL)");
            System.out.println("Created table");
            rs = conn.createStatement().executeQuery("SELECT COUNT(*) FROM" + tableName);
            System.out.println("Table ok");
        }
        int count = 0;
        try {
            count = rs.getInt(1);
        } catch (Exception e) {

        }

        if(count < 1) {
            conn.createStatement().execute("INSERT INTO " + tableName + "(name) VALUES('Anna'),('Bent')");
            System.out.println("Inserting Anna and Bent");
        }
        System.out.println("Columns ok");

        }

    public List<String> getPersons() {
        return persons;
    }

}
