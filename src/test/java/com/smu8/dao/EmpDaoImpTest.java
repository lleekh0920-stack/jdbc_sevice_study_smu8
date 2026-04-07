package com.smu8.dao;

import com.smu8.DBManger;
import com.smu8.dto.EmpDto;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmpDaoImpTest {
    static DBManger dbManger=DBManger.getInstance();
    static EmpDao empDao;
    static {
        try {
            empDao = new EmpDaoImp(dbManger.getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void insert() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void deletePayHistory() {
    }

    @Test
    void findAll() throws SQLException {
        List<EmpDto> emps=empDao.findAll();
        System.out.println(emps);
    }

    @Test
    void findByEmpno() throws SQLException {
        EmpDto emp=empDao.findByEmpno(7369);
        System.out.println(emp);
        //Assertions.assertNotNull(emp);
    }

    @Test
    void findByDeptno() {
    }

    @Test
    void findByEnameContaining() {
    }
}