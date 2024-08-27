package org.example;
import java.sql.*;
public class Main {

    public static void main(String[] args)
    {
        System.out.println("Hello world!");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb","root","root");
            Statement st=conn.createStatement();
            st.executeUpdate("create table if not exists Student(name varchar(20),id int)");
            st.executeUpdate("insert into Student values('steffy',101),('sneha',102)");
            st.close();
            conn.close();

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}