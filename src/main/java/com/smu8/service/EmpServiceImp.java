package com.smu8.service;

import com.smu8.DBManger;
import com.smu8.dao.EmpDao;
import com.smu8.dao.EmpDaoImp;
import com.smu8.dto.EmpDto;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class EmpServiceImp implements EmpService{
    // 필요한 필드: 접속객체, empDao 객체
//    private final Connection conn;
    private final HikariDataSource dataSource;
//    private EmpDao empDao;
    //내부에서 conn 호출 및 Dao 생성
    public EmpServiceImp() throws SQLException {
//        this.conn= DBManger.getInstance().getConnection();
//        this.empDao=new EmpDaoImp(conn);
        this.dataSource=DBManger.getInstance().getDataSource();
        //서비스에서 Auto close 사용하기 위해
    }


    //외부에서 두객체를 받아서 사용
    public EmpServiceImp( HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }
    //사원을 삭제
    //1. 지울 사원이 있는지 확인 -> 없으면 오류
    //2. 지울 사원을 상사로 참조하는 사원을 Null 처리
    //3. 지울 사원 급여기록 삭제
    //4. 지울 사원을 드디어 삭제
    //5. 이중 하나라도 오류발생하면 rollback
    @Override
    public void removeEmp(int id) throws IllegalArgumentException {
        Connection conn=null;
        try{
            conn=dataSource.getConnection();
            EmpDao empDao=new EmpDaoImp(conn);
            conn.setAutoCommit(false);
            EmpDto existEmp=empDao.findByEmpno(id);//1
            if(existEmp==null)throw new IllegalArgumentException("존재하지 않는 사원입니다.");//1
            int update=empDao.updateMgrToNull(id); // 2
            System.out.println("상사를 null로 만든 사원의 수:"+ update);
            int deletePay=empDao.deletePayHistory(id);//3
            System.out.println("지울 사원의 급여기록 삭제 :" + deletePay);
            int delete=empDao.delete(id);
            System.out.println("삭제 성공" + delete);
            conn.commit();
        } catch (SQLException | IllegalArgumentException e) {
            try{conn.rollback();}catch (Exception ex){}// transaction 으로 한개의 단위로 생성
            throw new RuntimeException(e);
        } finally{
            try{conn.close();}catch(Exception e){} // 이미 닫혔는데 또 닫을때, 통신이 끊어졌을때, null
        }
    }

    @Override
    public EmpDto getDtail(int id) {
        EmpDto emp=null;
        try(Connection conn=dataSource.getConnection()) {
            EmpDao empDao=new EmpDaoImp(conn);
            emp=empDao.findByEmpno(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return emp;
    }

    @Override
    public List<EmpDto> getEmps() {
        List<EmpDto> emps= null;
        try(Connection conn=dataSource.getConnection()
        ) {EmpDao empDao=new EmpDaoImp(conn);
            emps=empDao.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return emps;
    }

    @Override
    public List<EmpDto> getEmps(int deptno) {
        List<EmpDto> emps=null;
        try(Connection conn=dataSource.getConnection()) {
            EmpDao empDao=new EmpDaoImp(conn);
            emps= empDao.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return emps;
    }

    @Override
    public List<EmpDto> searchEmps(String ename) {
        List<EmpDto> emps=null;
        try(Connection conn=dataSource.getConnection()) {
            EmpDao empDao=new EmpDaoImp(conn);
            emps=empDao.findByEnameContaining(ename);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return emps;
    }

    @Override
    public void registerEmp(EmpDto emp) throws SQLException, IllegalArgumentException {
        //1. 이미 등록된 사원은 오류 IllegalArgumentException
        //2. 무결성+타입의 크기 SQLException (** 타입의 크기는 validBean 에서 유효성 검사 가능)
        //3. 트랜잭션이 필요 없이 (수정삭제등록이 2개 이상일때 1개의 단위로 묶는것)
        try(Connection conn=dataSource.getConnection()){
            EmpDao empDao=new EmpDaoImp(conn);
            EmpDto existEmp=empDao.findByEmpno(emp.getEmpno());
            if(existEmp!=null)throw new IllegalArgumentException("이미 존재하는 사원이 있습니다.");
//            if(empDao.findByEmpno(emp.getMgr())==null)
            if(emp.getMgr()!=null){
                EmpDto existMgr=empDao.findByEmpno(emp.getMgr());
                if(existMgr==null)throw new IllegalArgumentException("존재하지 않는 상사 입니다.");
//              EmpDto existMgr=empDao.findByEmpno(emp.getMgr());
            }
//            if(emp.getMgr()!=null && empDao.findByEmpno(emp.getMgr())==null) throw new IllegalArgumentException("존재하지 않는 상사입니다.")  ;
            empDao.insert(emp);
        }

    }

    @Override
    public void modifyEmp(EmpDto emp) throws SQLException, IllegalArgumentException {
        //1.없는 사원은 수정 못함 -> IllegalArgumentException
        //2.무결성 + 타입의 크기 SQLException (** 타입의 크기는 validBean 에서 유효성 검사 가능)
        //3.트랜잭션이 필요 없이(수정삭제등록이 2개 이상일때 1개의 단위로 묶는것)
        //4.없는 상사 -> IllegalArgumentException
        //5.없는 부서 -> IllegalArgumentException
        try(Connection conn=dataSource.getConnection()){
            EmpDao empDao=new EmpDaoImp(conn);
            if(empDao.findByEmpno(emp.getEmpno())==null)
                throw new IllegalArgumentException("존재하지 않는 사원입니다.");
            if(emp.getMgr()!=null && empDao.findByDeptno(emp.getMgr())==null)
                throw  new IllegalArgumentException("존재하지 않는 상사입니다.");
            int update= empDao.update(emp);
            System.out.println("수정 성공 :"+update);
        }
    }


}
