package com.accountapi.api.repositories;

import com.accountapi.api.models.Statement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StatementRepository extends JpaRepository<Statement , Long> {
    Optional<List<Statement>> findStatementsByAccount_Id(Long accountId);
}
