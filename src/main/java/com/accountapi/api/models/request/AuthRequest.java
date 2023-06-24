package com.accountapi.api.models.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.antlr.v4.runtime.misc.NotNull;


@Data
@AllArgsConstructor
@ToString
public class AuthRequest {
    @NotNull
    private String username;
    @NotNull
    private String password;
}
