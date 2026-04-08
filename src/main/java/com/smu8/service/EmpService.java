package com.smu8.service;

import com.smu8.dto.EmpDto;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface EmpService {
    // 사원관리 swing 어플의 서비스 (C R U D)
    // 서비스에서 함수 이름의 규칙
    // 등록 register (성공, 실패,오류)
    // 조회 get (상세, 전체, 검색)
    // 수정 modify (성공, 실패오류, 삭제된 레코드)
    // 삭제 remove (성공,삭제된 레코드,오류x[자식 테이블이 참조시 삭제 불가인데 같이 삭제하기 때문에 오류가 발생하지 않음])


    // 상세조회 pk(id)
    EmpDto getDtail(int id);
    //전체조회
    List<EmpDto> getEmps();
    //List<EmpDto> getEmps(String ename);
    //deptno=10
    List<EmpDto> getEmps(int deptno);
    //이름으로 검색 Like '%경%';
    List<EmpDto> searchEmps(String ename);

    void registerEmp(EmpDto emp) throws SQLException;
    void modifyEmp(EmpDto empDto) throws SQLException,IllegalArgumentException;
    // SQLException uk, 유효성검사오류 , IllegalArgumentException 존재하지 않는 코드일때

    //트랜잭션 (오류 발생시 롤백 구현)
    //emp 지우기 전에 pay_history를 먼저 지우고 삭제 (사원을 참조하는 모든 자식테이블 먼저 삭제)
    //지워야할 사원이 이미 삭제되었거나 없을때 IllegalArgumentException
    void removeEmp(int id)throws SQLException, IllegalArgumentException;
}

