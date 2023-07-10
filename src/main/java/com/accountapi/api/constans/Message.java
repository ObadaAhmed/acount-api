package com.accountapi.api.constans;

public class Message {
    public static final String REQUIRED_ACCOUNT_ID = "account id required";
    public static final String INVALID_DATE_FORMAT = "Invalid date format. fromDate & toDate  should be of format dd.MM.yyyy.";
    public static final String INVALID_DATE_RANGE = "Invalid date range. 'fromDate' cannot be after 'toDate'.";
    public static final String INVALID_AMOUNT_RANGE = "Invalid amount range. 'fromAmount' cannot be greater than 'toAmount'.";
    public static final String NO_STATEMENTS_FOUND = "No statements found.";
    public static final String FAILURE_FETCHING_STATEMENT = "Failed to fetch account statements.";
    public static final String INVALID_ID_FORMAT = "Invalid ID format. Only numeric IDs are allowed.";
}
