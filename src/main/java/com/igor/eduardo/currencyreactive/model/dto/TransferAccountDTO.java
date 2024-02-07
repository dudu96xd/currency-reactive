package com.igor.eduardo.currencyreactive.model.dto;

import lombok.Data;

@Data
public class TransferAccountDTO {

    private int id;
    private double toBeTranfered;
    private int destination;
}
