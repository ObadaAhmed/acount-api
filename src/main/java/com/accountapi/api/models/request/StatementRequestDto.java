package com.accountapi.api.models.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatementRequestDto {
    private Long accountId;
    private String fromDate;
    private String toDate;
    private Double fromAmount;
    private Double toAmount;
    public StatementRequestDto(Long accountId) {
        this.accountId = accountId;
    }
}
