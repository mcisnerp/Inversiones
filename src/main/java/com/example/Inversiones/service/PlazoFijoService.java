package com.example.Inversiones.service;

import com.example.Inversiones.config.TasasConfig;
import com.example.Inversiones.entity.Cuenta;
//import com.example.Cuenta.entity.Cuenta;
import com.example.Inversiones.entity.CuentaCorriente;
import com.example.Inversiones.entity.PlazoFijo;
import com.example.Inversiones.repository.PlazoFijoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
//import com.example.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
public class PlazoFijoService {
    @Autowired
    PlazoFijoRepository plazoFijoRepository;

    @Autowired
    TasasConfig tasasConfig;

    @Autowired
    RestTemplate restTemplate = new RestTemplate();

    public PlazoFijo solicitudPlazoFijo(PlazoFijo plazoFijo, Cuenta cuenta) {
        plazoFijo.setStatus("Activo");
        if (plazoFijo.getMonto() > 100000) {
            plazoFijo.setTasa(tasasConfig.getTasa100());
        } else if (plazoFijo.getMonto() > 50000) {
            plazoFijo.setTasa(tasasConfig.getTasa50());
        } else if (plazoFijo.getMonto() > 10000) {
            plazoFijo.setTasa(tasasConfig.getTasa10());
        }
        actualizarRetirarCuenta(cuenta, plazoFijo.getMonto());
        plazoFijo.setNumeroCuenta(cuenta.getNumeroCuenta());
        plazoFijo.setUsuario(cuenta.getUsuario());
        plazoFijo.setIntereses(plazoFijo.getMonto() * plazoFijo.getTasa());
        plazoFijo.setMontoTotal(plazoFijo.getMonto() + plazoFijo.getIntereses());
        return plazoFijoRepository.save(plazoFijo);
    }

    public String getTasasVigentes() {
        return tasasConfig.toString();
    }

    public List<PlazoFijo> listadoPlazoFijo(Integer numeroCuenta) {
        //Cuenta cuenta = buscarCuenta(numeroCuenta);
        //System.out.println(cuenta.getIdCuenta());
        return plazoFijoRepository.findAllPlazoFijoByNumeroCuenta(numeroCuenta);
    }

    public PlazoFijo cancelarPlazoFijo(PlazoFijo plazoFijo) {
        List<Cuenta> cuentas = getCuentas();
        for (Cuenta c : cuentas) {
            if (c.getNumeroCuenta().equals(plazoFijo.getNumeroCuenta())) {
                Double rembolso = plazoFijo.getMonto() - (plazoFijo.getMonto() * 0.02);
                actualizarDepositarCuenta(c, rembolso);
                plazoFijo.setStatus("Cancelado");
                plazoFijoRepository.save(plazoFijo);
                return plazoFijo;
            }
        }
        return null;
    }


    public PlazoFijo getPlazoFijo(Integer idPlazoFijo) {
        return plazoFijoRepository.findByIdPlazoFijo(idPlazoFijo);
    }

    public Cuenta buscarCuenta(Integer numeroCuenta) {
        List<Cuenta> cuentas = getCuentas();
        Cuenta cuenta = new Cuenta();
        for (Cuenta c : cuentas) {
            if (c.getNumeroCuenta().equals(numeroCuenta)) {
                return c;
            }
        }
        return cuenta;
    }

    public String comprarDolares(Cuenta cuentaOri, Cuenta cuentaDes, Double cantidad) {
        Double importe = cantidad * 19;
        actualizarRetirarCuenta(cuentaOri, cantidad);
        cuentaOri.setBalance(cuentaOri.getBalance() - cantidad);
        actualizarDepositarCuenta(cuentaDes, importe);
        cuentaDes.setBalance(cuentaDes.getBalance() + importe);
        return "finalizada: cuenta origen " + cuentaOri.getNumeroCuenta() + ", cuenta destino " +
                cuentaDes.getNumeroCuenta() + ", importe pesos " + importe + ", importe dolares " +
                cantidad + ", tipo operacion compra de dolares, balance cuenta origen " + cuentaOri.getBalance() +
                ", balance cuenta destino " + cuentaDes.getBalance();
    }

    public String venderDolares(Cuenta cuentaOri, Cuenta cuentaDes, Double cantidad) {
        Double importe = cantidad / 20;
        actualizarRetirarCuenta(cuentaOri, cantidad);
        cuentaOri.setBalance(cuentaOri.getBalance() - cantidad);
        actualizarDepositarCuenta(cuentaDes, importe);
        cuentaDes.setBalance(cuentaDes.getBalance() + importe);
        return "finalizada: cuenta origen " + cuentaOri.getNumeroCuenta() + ", cuenta destino " +
                cuentaDes.getNumeroCuenta() + ", importe pesos " + importe + ", importe dolares " +
                cantidad + ", tipo operacion compra de dolares, balance cuenta origen " + cuentaOri.getBalance() +
                ", balance cuenta destino " + cuentaDes.getBalance();
    }

    public List<Cuenta> getCuentas() {
        ResponseEntity<Cuenta[]> cuentaResponseEntity = restTemplate.getForEntity("http://localhost:8081/cuenta/listacuentas", Cuenta[].class);
        Cuenta[] cuenta = cuentaResponseEntity.getBody();
        List<Cuenta> cuentas = Arrays.asList(cuenta);
        return cuentas;
    }

    public String actualizarRetirarCuenta(Cuenta cuenta, Double monto) {
        String url = "http://localhost:8081/cuenta/retirar/" + cuenta.getNumeroCuenta() + "/" + monto;
        String requestBody = "";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
        String responseBody = response.getBody();
        return responseBody;
    }

    public String actualizarDepositarCuenta(Cuenta cuenta, Double monto) {
        String url = "http://localhost:8081/cuenta/depositar/" + cuenta.getNumeroCuenta() + "/" + monto;
        String requestBody = "";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
        String responseBody = response.getBody();
        return responseBody;
    }
}
