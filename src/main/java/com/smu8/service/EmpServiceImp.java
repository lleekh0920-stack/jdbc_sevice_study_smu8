package com.smu8.service;

import com.smu8.DBManger;
import com.smu8.dao.EmpDao;
import com.smu8.dao.EmpDaoImp;
import com.smu8.dto.EmpDto;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class EmpServiceImp implements EmpService{
    // 필요한 필드: 접속객체, empDao 객체
    private final Connection conn;
    private final EmpDao empDao;
    //내부에서 conn 호출 및 Dao 생성
    public EmpServiceImp() throws SQLException {
        this.conn= DBManger.getInstance().getConnection();
        this.empDao=new EmpDaoImp(conn);
    }

    //외부에서 두객체를 받아서 사용
    public EmpServiceImp(Connection conn, EmpDao empDao) {
        this.conn = conn;
        this.empDao = empDao;
    }
    //사원을 삭제
    //1. 지울 사원이 있는지 확인 -> 없으면 오류
    //2. 지울 사원을 상사로 참조하는 사원을 Null 처리
    //3. 지울 사원 급여기록 삭제
    //4. 지울 사원을 드디어 삭제
    //5. 이중 하나라도 오류발생하면 rollback
    @Override
    public void removeEmp(int id) throws IllegalArgumentException {
        try{
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
        return null;
    }

    @Override
    public List<EmpDto> getEmps() {
        return List.of();
    }

    @Override
    public List<EmpDto> getEmps(int deptno) {
        return List.of();
    }

    @Override
    public List<EmpDto> searchEmps(String ename) {
        return List.of();
    }

    @Override
    public void registerEmp(EmpDto emp) throws SQLException {

    }

    @Override
    public void modifyEmp(EmpDto empDto) throws SQLException, IllegalArgumentException {

    }


}
