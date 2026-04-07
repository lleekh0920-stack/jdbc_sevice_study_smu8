package com.smu8;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class DBMangerTest {

    @Test
    void getInstance() {
        DBManger dbManger=DBManger.getInstance();
        System.out.println(dbManger);
        assertNotNull(dbManger);
    }

    //@ 어노테이션 : 컴파일 단계에서 오류검사, 자동완성 @Overridse @FunctionalInterface..
    @Test
    void getConnection() throws SQLException {
        DBManger dbManger=DBManger.getInstance();
        Connection conn=DBManger.getConnection();
        Statement st=conn.createStatement();
        ResultSet rs=st.executeQuery("SELECT * FROM EMP");
        while(rs.next()){
            System.out.println(rs.getString("ename"));
        }
    }
}