package com.smu8.service;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class EmpServiceImpTest {
    @Test
    void removeEmp() throws SQLException {
        EmpService empService=new EmpServiceImp();
        empService.removeEmp(7566);
    }
}