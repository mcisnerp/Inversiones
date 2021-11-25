package com.example.Inversiones.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Cuentas")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Cuenta {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idCuenta;
    private String tipoCuenta;
    private String usuario;
    private String monedaCuenta;
    private Double balance;
    private Integer numeroCuenta;
}

