package com.stixis.ems.dao;

import com.stixis.ems.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TokenRepository extends JpaRepository<Token,Long> {

    @Query("SELECT T FROM Token T INNER JOIN Employee E ON T.employee.employeeId = E.employeeId WHERE E.employeeId = :userId AND (T.expired=FALSE OR T.revoked=FALSE)")
    Set<Token> findValidTokenByUser(Long userId);

    void deleteByEmployee_EmployeeId(Long employeeId);
    Token findByToken(String token);
}
