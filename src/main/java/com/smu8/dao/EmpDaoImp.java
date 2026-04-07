package com.smu8.dao;

import com.smu8.dto.EmpDto;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmpDaoImp implements EmpDao{
    //DAO 쿼리를 실행해서 결과를 맵핑
    private final Connection conn;

    public EmpDaoImp(Connection conn){
        this.conn=conn;
    }



    @Override
    public void insert(EmpDto emp) throws SQLException {

    }

    @Override
    public int update(EmpDto emp) throws SQLException {
        return 0;
    }

    @Override
    public int delete(int empno) throws SQLException {
        return 0;
    }

    @Override
    public int deletePayHistory(int empno) throws SQLException {
        return 0;
    }

    @Override
    public List<EmpDto> findAll() throws SQLException {
        List<EmpDto> emps=null;
        String sql="SELECT * FROM EMP";
        Statement st=conn.createStatement();
        ResultSet rs=st.executeQuery(sql);
        emps=new ArrayList<>();
        while(rs.next()){
            EmpDto emp=new EmpDto(
                    rs.getInt("empno"),
                    rs.getString("ename"),
                    rs.getString("job"),
                    rs.getObject("mgr", Integer.class),
                    rs.getObject("hiredate", LocalDate.class),
                    rs.getObject("sal", Double.class),
                    rs.getObject("comm", Double.class),
                    rs.getObject("deptno", Integer.class)
            );
            emps.add(emp);

        }

        return emps;
    }

    @Override
    public EmpDto findByEmpno(int empno) {
        return null;
    }

    @Override
    public List<EmpDto> findByDeptno(int deptno) throws SQLException {
        return List.of();
    }

    @Override
    public List<EmpDto> findByEnameContaining(String ename) throws SQLException {
        return List.of();
    }
}
