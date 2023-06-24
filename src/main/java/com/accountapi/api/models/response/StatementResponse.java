package com.accountapi.api.models.response;


import com.accountapi.api.models.Statement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class StatementResponse {
    private List<Statement> accountStatements;

    public StatementResponse() {}
}
