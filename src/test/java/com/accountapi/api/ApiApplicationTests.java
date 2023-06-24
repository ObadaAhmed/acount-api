package com.accountapi.api;

import com.accountapi.api.constans.Constans;
import com.accountapi.api.constans.Message;
import com.accountapi.api.models.Account;
import com.accountapi.api.models.request.StatementRequestDto;
import com.accountapi.api.repositories.AccountRepository;
import com.accountapi.api.repositories.StatementRepository;
import com.accountapi.api.services.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@SpringBootTest
class AccountServiceTest {

	@Mock
	private AccountRepository accountRepository;

	@Mock
	private StatementRepository statementRepository;

	@InjectMocks
	private AccountService accountService;

	@Mock
	private Logger logger;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void fetchAllAccounts_ShouldReturnListOfAccounts() {
		// Arrange
		List<Account> accounts = new ArrayList<>();
		accounts.add(new Account());
		when(accountRepository.findAll()).thenReturn(accounts);

		// Act
		List<Account> result = accountService.fetchAllAccounts("accountId");

		// Assert
		assertEquals(accounts, result);
	}

	@Test
	void fetchAccountDetails_WithValidAccountId_ShouldReturnAccountOptional() {
		// Arrange
		Long accountId = 1L;
		Optional<Account> expectedAccount = Optional.of(new Account());
		when(accountRepository.findById(accountId)).thenReturn(expectedAccount);

		// Act
		Optional<Account> result = accountService.fetchAccountDetails(accountId);

		// Assert
		assertEquals(expectedAccount, result);
		verify(logger).info("Account details fetched successfully for accountId: {}", accountId);
	}
	@Test
	void fetchAccountStatements_WithMissingAccountId_ShouldReturnErrorResult() {
		// Arrange
		StatementRequestDto dto = new StatementRequestDto();

		// Act
		Map<String, Object> resultMap = accountService.fetchAccountStatements(dto);

		// Assert
		assertEquals(Constans.STATUS_ERROR, resultMap.get(Constans.STR_STATUS));
		assertEquals(Message.REQUIRED_ACCOUNT_ID, resultMap.get(Constans.STR_ERROR_KEY));
		verify(statementRepository, never()).findStatementsByAccount_Id(anyLong());
		verify(logger, never()).error(anyString());
	}

	@Test
	void fetchAccountStatements_WithInvalidDateFormats_ShouldReturnErrorResult() {
		// Arrange
		StatementRequestDto dto = new StatementRequestDto();
		dto.setAccountId(1L);
		dto.setFromDate("2022/01/01");
		dto.setToDate("2022/12/31");

		// Act
		Map<String, Object> resultMap = accountService.fetchAccountStatements(dto);

		// Assert
		assertEquals(Constans.STATUS_ERROR, resultMap.get(Constans.STR_STATUS));
		assertEquals(Message.INVALID_DATE_FORMAT, resultMap.get(Constans.STR_ERROR_KEY));
		verify(statementRepository, never()).findStatementsByAccount_Id(anyLong());
		verify(logger, never()).error(anyString());
	}
//
	@Test
	void fetchAccountStatements_WithInvalidDateRange_ShouldReturnErrorResult() {
		// Arrange
		StatementRequestDto dto = new StatementRequestDto();
		dto.setAccountId(1L);
		dto.setFromDate("19.10.2020");
		dto.setToDate("30.09.2020");

		// Act
		Map<String, Object> resultMap = accountService.fetchAccountStatements(dto);

		// Assert
		assertEquals(Constans.STATUS_ERROR, resultMap.get(Constans.STR_STATUS));
		assertEquals(Message.INVALID_DATE_RANGE, resultMap.get(Constans.STR_ERROR_KEY));
		verify(statementRepository, never()).findStatementsByAccount_Id(anyLong());
		verify(logger, never()).error(anyString());
	}

	@Test
	void fetchAccountStatements_WithInvalidAmountRange_ShouldReturnErrorResult() {
		// Arrange
		StatementRequestDto dto = new StatementRequestDto();
		dto.setAccountId(1L);
		dto.setFromAmount(1000.0);
		dto.setToAmount(100.0);

		// Act
		Map<String, Object> resultMap = accountService.fetchAccountStatements(dto);

		// Assert
		assertEquals(Constans.STATUS_ERROR, resultMap.get(Constans.STR_STATUS));
		assertEquals(Message.INVALID_AMOUNT_RANGE, resultMap.get(Constans.STR_ERROR_KEY));
		verify(statementRepository, never()).findStatementsByAccount_Id(anyLong());
		verify(logger, never()).error(anyString());
	}
}
