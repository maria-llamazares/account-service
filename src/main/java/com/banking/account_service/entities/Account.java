package com.banking.account_service.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Entity
@Getter
@Setter
public class Account {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String iban;

    @ElementCollection
    @CollectionTable(name = "account_balances", joinColumns = @JoinColumn(name = "account_id"))
    @MapKeyColumn(name = "currency")
    @Column(name = "balance")
    private Map<String, BigDecimal> balances = new HashMap<>();

}
