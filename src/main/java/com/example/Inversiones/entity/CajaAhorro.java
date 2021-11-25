package com.example.Inversiones.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CajaAhorro extends Cuenta {

    public Double getRetirar(Double cantidad, Double balance) {
        if (balance > cantidad) {
            balance -= cantidad;
        }
        return balance;
    }

    public Double getDepositar(Double cantidad, Double balance) {
        balance += cantidad;
        return balance;
    }
}