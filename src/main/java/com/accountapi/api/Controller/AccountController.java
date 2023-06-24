package com.accountapi.api.Controller;

import com.accountapi.api.constans.Constans;
import com.accountapi.api.constans.Message;
import com.accountapi.api.models.Account;
import com.accountapi.api.models.request.StatementRequestDto;
import com.accountapi.api.models.response.ErrorResponse;
import com.accountapi.api.models.response.StatementResponse;
import com.accountapi.api.services.AccountService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
@RestController
public class AccountController {
    Logger logger =  LoggerFactory.getLogger(AccountController.class);
    @Autowired
    private AccountService accountService;
    @GetMapping("/admin/fetch-statement")
    public List<Account> getStatements(@Param("id") String id) {
        return accountService.fetchAllAccounts(id);
    }
    @GetMapping("/admin/account-details/{id}")
    public Account getAccountDetails(@PathVariable("id") Long id) throws IllegalAccessException {
        Optional<Account> account = accountService.fetchAccountDetails(id);
        if (account.isPresent()) {
            return account.get();
        }
        // should handle error messsage here
        throw new IllegalAccessException("No account with account id");
    }
    @GetMapping("/admin/statement")
    public ResponseEntity<?> getStatements(
            @RequestParam(required = false) Long accountId,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @RequestParam(required = false) Double fromAmount,
            @RequestParam(required = false) Double toAmount
        ){
        StatementRequestDto requestDto = new StatementRequestDto(accountId , fromDate , toDate , fromAmount , toAmount);
        return getResponseEntity(requestDto);
    }

    @GetMapping("/user/statements/{id}")
    public ResponseEntity<?> getUserStatements(@PathVariable Long id) {
        StatementRequestDto statementRequestDto = new StatementRequestDto(id);
        return getResponseEntity(statementRequestDto);
    }

    private ResponseEntity<?> getResponseEntity(StatementRequestDto statementRequestDto) {
        Map<String, Object> resultMap = accountService.fetchAccountStatements(statementRequestDto);
        if (Constans.STATUS_ERROR.equalsIgnoreCase(resultMap.get(Constans.STR_STATUS).toString())) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse(resultMap.get(Constans.STR_STATUS).toString(),
                            resultMap.get(Constans.STR_ERROR_KEY).toString()));
        }
        if (resultMap.get(Constans.RESPONSE_STATEMENT_DTO) instanceof StatementResponse) {
            return ResponseEntity.ok( (StatementResponse) resultMap.get(Constans.RESPONSE_STATEMENT_DTO));
        }
        return ResponseEntity.ofNullable(new ErrorResponse(Constans.STATUS_ERROR , Message.FAILURE_FETCHING_STATEMENT));
    }
}
