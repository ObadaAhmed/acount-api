package com.accountapi.api.services;
import com.accountapi.api.constans.Constans;
import com.accountapi.api.constans.Message;
import com.accountapi.api.models.Account;
import com.accountapi.api.models.Statement;
import com.accountapi.api.models.request.StatementRequestDto;
import com.accountapi.api.models.response.StatementResponse;
import com.accountapi.api.repositories.AccountRepository;
import com.accountapi.api.repositories.StatementRepository;
import com.accountapi.api.utility.DateUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;
@Service
public class AccountService {
    Logger logger = LoggerFactory.getLogger(AccountService.class);
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private StatementRepository statementRepository;
    public List<Account> fetchAllAccounts(String accountId) {
        return accountRepository.findAll();
    }
    public Optional<Account> fetchAccountDetails(Long accountId) {
        try {
            Optional<Account> account = accountRepository.findById(accountId);
            if (account.isPresent()) {
                logger.info("Account details fetched successfully for accountId: {}", accountId);
                return account;
            }
        }catch (Exception e) {
            logger.error("Error occurred while fetching account details for accountId: {}", accountId, e);
        }

        return Optional.empty();
    }
    public Map<String, Object> fetchAccountStatements(StatementRequestDto Dto) {
            Map<String, Object> resultMap = new HashMap<>();
        try {
            if (Dto.getAccountId() == null || Dto.getAccountId() == 0) {
                 resultMap.put(Constans.STR_STATUS , Constans.STATUS_ERROR);
                 resultMap.put(Constans.STR_ERROR_KEY , Message.REQUIRED_ACCOUNT_ID );
                 return resultMap;
            }
            LocalDate fromDateObj;
            LocalDate toDateObj;

            if (Dto.getFromDate() != null && DateUtility.isNotValidDateFormat(Dto.getFromDate()) || Dto.getToDate() != null && DateUtility.isNotValidDateFormat(Dto.getToDate())) {
                resultMap.put(Constans.STR_STATUS , Constans.STATUS_ERROR);
                resultMap.put(Constans.STR_ERROR_KEY , Message.INVALID_DATE_FORMAT );
                return resultMap;
            }else {
                fromDateObj = DateUtility.parseDate(Dto.getFromDate(), LocalDate.now().minusMonths(3));
                toDateObj = DateUtility.parseDate(Dto.getToDate(), LocalDate.now());
            }

            if (fromDateObj.isAfter(toDateObj)) {
                resultMap.put(Constans.STR_STATUS , Constans.STATUS_ERROR);
                resultMap.put(Constans.STR_ERROR_KEY , Message.INVALID_DATE_RANGE );
                return resultMap;
            }

            if (Dto.getFromAmount() != null && Dto.getToAmount() != null && Dto.getFromAmount() > Dto.getToAmount()) {
                resultMap.put(Constans.STR_STATUS , Constans.STATUS_ERROR);
                resultMap.put(Constans.STR_ERROR_KEY , Message.INVALID_AMOUNT_RANGE );
                return resultMap;
            }
            Optional<List<Statement>> accountStatements = statementRepository.findStatementsByAccount_Id(Dto.getAccountId());
            if (accountStatements.isEmpty()) {
                resultMap.put(Constans.STR_STATUS , Constans.STATUS_ERROR);
                resultMap.put(Constans.STR_ERROR_KEY , Message.NO_STATEMENTS_FOUND );
                return resultMap;
            }
            List<Statement> filteredStatements;
            filteredStatements = filterStatementsByDate(accountStatements.get() , fromDateObj , toDateObj);
            filteredStatements = filterStatementsByAmount(filteredStatements , Dto.getFromAmount() , Dto.getToAmount());
            resultMap.put(Constans.STR_STATUS , Constans.STATUS_SUCCESS);
            resultMap.put(Constans.RESPONSE_STATEMENT_DTO , new StatementResponse(filteredStatements));
            return resultMap;
        }catch (Exception e){
            logger.error(e.getMessage());
            resultMap.put(Constans.STR_STATUS , Constans.STATUS_ERROR);
            resultMap.put(Constans.STR_ERROR_KEY , Message.FAILURE_FETCHING_STATEMENT );
            return resultMap;
        }
    }
    private List<Statement> filterStatementsByDate(List<Statement> statements, LocalDate fromDate, LocalDate toDate) {
        List<Statement> filteredStatements = new ArrayList<>();
        for (Statement statement : statements) {
            LocalDate statementDate = LocalDate.parse(statement.getDateField(), DateUtility.DATE_FORMATTER);
            if (!statementDate.isBefore(fromDate) && !statementDate.isAfter(toDate)) {
                filteredStatements.add(statement);
            }
        }
        return filteredStatements;
    }
    private List<Statement> filterStatementsByAmount(List<Statement> statements, Double fromAmount, Double toAmount) {
        List<Statement> filteredStatements = new ArrayList<>();
        if (fromAmount != null && toAmount != null) {
            for (Statement statement : statements) {
                Double amount = statement.getAmount();
                if ((fromAmount == null || amount >= fromAmount) && (toAmount == null || amount <= toAmount)) {
                    filteredStatements.add(statement);
                }
            }
            return filteredStatements;
        }else {
            return statements;
        }
    }
}
