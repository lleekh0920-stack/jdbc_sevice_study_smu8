package com.smu8.dao;

import com.smu8.dto.EmpDto;

import java.sql.*;
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
    public int updateMgrToNull(int mgr) throws SQLException {
        int update=0;
        String sql="UPDATE EMP SET mgr=null WHERE mgr=?";
        PreparedStatement ps=conn.prepareStatement(sql);
        ps.setInt(1,mgr);
        update=ps.executeUpdate();
        return update;
    }

    @Override
    public void insert(EmpDto emp) throws SQLException {
        String sql="""
        INSERT INTO EMP (empno, ename, job, mgr, hiredate, sal, comm, deptno) 
        VALUES(?,?,?,?,?,?,?,?)
                """;
        PreparedStatement ps=conn.prepareStatement(sql);
        ps.setInt(1,emp.getEmpno());
        ps.setString(2, emp.getEname());
        ps.setString(3, emp.getJob());
//        ps.setInt(4,emp,getMgr()); // 오류 or null
//        if(emp.getMgr()==null){ps.setNull(4,emp.getMgr());}else {ps.setInt(4,emp.getMgr());} //권장
        ps.setObject(4,emp.getMgr()); //권장하지 않지만 Null 처리
        ps.setObject(5,emp.getHiredate()); // localDate.String
        ps.setObject(6,emp.getSal());
        ps.setObject(7,emp.getComm());
        ps.setObject(8,emp.getDeptno());
        ps.executeUpdate();//dml
    }

    @Override
    public int update(EmpDto emp) throws SQLException {
        int update=0;
        String sql="""
        UPDATE EMP SET 
                       ENAME=?,JOB=?,
                       SAL=?,COMM=?,
                       HIREDATE=?,
                       MGR=? , DEPTNO=?
                 WHERE EMPNO =?  
                            """;
        PreparedStatement ps=conn.prepareStatement(sql);
        ps.setString(1,emp.getEname());
        ps.setString(2,emp.getJob());
        if(emp.getSal()==null){
            ps.setNull(3,Types.NUMERIC);
        }else {
            ps.setDouble(3,emp.getSal());
        }
        if (emp.getComm()==null){
                ps.setNull(4,Types.NUMERIC);
            }else {ps.setDouble(4,emp.getComm());
        }
        ps.setObject(5,emp.getHiredate());

        if(emp.getMgr()==null){
            ps.setNull(6,Types.NUMERIC);
        }else {
            ps.setInt(6,emp.getMgr());
        }
        if(emp.getDeptno()==null){
            ps.setNull(7,Types.NUMERIC);
        }else {
            ps.setInt(7,emp.getDeptno());
        }
        ps.setInt(8,emp.getEmpno());
        update=ps.executeUpdate();
        return update;
    }

    @Override
    public int delete(int empno) throws SQLException {
        int delete=0;
        String sql="DELETE FROM EMP WHERE EMPNO=?";
        PreparedStatement ps=conn.prepareStatement(sql);
        ps.setInt(1,empno);
        delete=ps.executeUpdate();
        return delete;

    }

    @Override
    public int deletePayHistory(int empno) throws SQLException {
        int delete=0;
        String sql="DELETE FROM PAY_HISTORY WHERE EMPNO=?";
        PreparedStatement ps=conn.prepareStatement(sql);
        ps.setInt(1,empno);
        delete=ps.executeUpdate();
        return delete;
    }

    @Override
    public List<EmpDto> findAll() throws SQLException {
        List<EmpDto> emps=null;
        String sql="SELECT * FROM EMP";
        Statement st=conn.createStatement();
        ResultSet rs=st.executeQuery(sql);
        emps=new ArrayList<>();
        while(rs.next()){
            EmpDto emp=parse(rs);
            emps.add(emp);

        }

        return emps;
    }

    @Override
    public EmpDto findByEmpno(int empno) throws SQLException{
        EmpDto emp=null;
        String sql="SELECT * FROM EMP WHERE empno=?";
        PreparedStatement ps=conn.prepareStatement(sql);
        ps.setInt(1,empno);
        ResultSet rs=ps.executeQuery();
        while(rs.next()){
            emp=parse(rs);
        }


        return emp;
    }

    @Override
    public List<EmpDto> findByDeptno(int deptno) throws SQLException {
        List<EmpDto> emps=null;
        String sql="SELECT * FROM EMP WHERE DEPTNO=?";
        PreparedStatement ps=conn.prepareStatement(sql);
        ps.setInt(1,deptno);
        ResultSet rs=ps.executeQuery();
        emps=new ArrayList<>();
        while(rs.next()){
            EmpDto emp=parse(rs);
            emps.add(emp);

        }

        return emps;
    }
    //SMITH
    //m
    @Override
    public List<EmpDto> findByEnameContaining(String ename) throws SQLException {
        List<EmpDto> emps=null;
//        String sql="SELECT * FROM EMP WHERE ENAME LIKE '%?%'"; // 문자열 안에 ? 가 있으면 ps가 못찾음
        String sql="SELECT * FROM EMP WHERE UPPER(ENAME) LIKE  UPPER(?)   ";
        PreparedStatement ps=conn.prepareStatement(sql);
        ps.setString(1,"%"+ename+"%");
        ResultSet rs=ps.executeQuery();
        emps=new ArrayList<>();
        while (rs.next()){
            emps.add(parse(rs));
        }
        return emps;
    }
    //ResultSet-> EmpDto (파스: 형변환)
    //resultSetParseEmpDto
    public EmpDto parse(ResultSet rs) throws SQLException {
        EmpDto emp=null;
        emp=new EmpDto(
                rs.getInt("empno"),
                rs.getString("ename"),
                rs.getString("job"),
                rs.getObject("mgr", Integer.class),
                rs.getObject("hiredate", LocalDate.class),
                rs.getObject("sal", Double.class),
                rs.getObject("comm", Double.class),
                rs.getObject("deptno", Integer.class)
        );
        return emp;
    }


}
