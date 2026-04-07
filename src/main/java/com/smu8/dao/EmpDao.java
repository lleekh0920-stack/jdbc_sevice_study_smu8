package com.smu8.dao;

import com.smu8.dto.EmpDto;

import java.sql.SQLException;
import java.util.List;
//설계: Service-> Dao
//구현: Dao -> Service

public interface EmpDao {
    // CRUD : DAO 의 CRUD는 sql과  비슷하게 작성 (오류는 서비스에서 처리)
    // 등록 => insert
    // 수정 => update
    // 삭제 => delete
    // 조회 => select,find(select), by(where) [JPA: 쿼리 결과를 자동매핑해주는 라이브러리]
    void insert(EmpDto emp) throws SQLException;
    int update(EmpDto emp) throws SQLException;
    //void insert(List<EmpDto> emp);
    int delete(int empno) throws SQLException;

    //급여기록은 PayHistoryDao에서 구현해야합니다.
    int deletePayHistory(int empno) throws SQLException;

    //조회 (상세,전체,부서번호조회,이름검색)
    // Select * From emp Where empno=?
    List<EmpDto> findAll() throws SQLException;
    EmpDto findByEmpno(int empno) throws SQLException;
    List<EmpDto> findByDeptno(int deptno) throws SQLException;

    // Select * From emp Where empno Like %?%
    //List<EmpDto> findByEname(String ename) throws SQLException;
    List<EmpDto> findByEnameContaining(String ename) throws SQLException;

}
