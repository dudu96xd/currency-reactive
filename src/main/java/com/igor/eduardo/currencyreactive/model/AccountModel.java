package com.igor.eduardo.currencyreactive.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("account")
public class AccountModel {
    
    @Id
    @Column("id")
    private Integer id;

    @Column("currency")
    private double currency;
    
}