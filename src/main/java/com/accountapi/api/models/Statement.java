package com.accountapi.api.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "statement")
@Data
@AllArgsConstructor

public class Statement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id" , referencedColumnName = "id" ,nullable = false)
    private Account account;
    @Column(name = "datefield")
    private String dateField;
    private double amount;

    public Statement(){}
}
