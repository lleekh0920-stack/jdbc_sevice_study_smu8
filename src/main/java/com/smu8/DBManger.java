package com.smu8;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManger {

    private static final DBManger INSTANCE= new DBManger();
    private static HikariDataSource dataSource;
    private final static String url="jdbc:oracle:thin:@//localhost:1521/XEPDB1";
    private final static String user="scott";
    private final static String pw="tiger";
    private DBManger(){
        HikariConfig config=new HikariConfig();
        config.setMaximumPoolSize(10);
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(pw);
        dataSource=new HikariDataSource(config);
    }

    public static DBManger getInstance(){
        return INSTANCE;
    }
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    public HikariDataSource getDataSource(){
        return dataSource;
    }

}

//class DBManagerTest{
//    public static void main(String[] args) {
//        DBManger dbManger=DBManger.getInstance();
//        try(Connection conn= dbManger.getConnection();
//            Statement st=conn.createStatement();
//            ResultSet rs=st.executeQuery("SELECT 1+1 result FROM DUAL")
//        ) {
//            if(rs.next()){
//                System.out.println(rs.getInt("result"));
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();;
//        }
//    }
//}
