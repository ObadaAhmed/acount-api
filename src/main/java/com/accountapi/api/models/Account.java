package com.accountapi.api.models;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "account")
@Data
public class Account {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String account_type;
    private String account_number;
}
