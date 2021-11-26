package com.example.Inversiones.service;

import com.example.Inversiones.config.TasasConfig;
import com.example.Inversiones.entity.Cuenta;
//import com.example.Cuenta.entity.Cuenta;
import com.example.Inversiones.entity.PlazoFijo;
import com.example.Inversiones.repository.PlazoFijoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import com.example.*;

import java.util.List;


@Service
public class PlazoFijoService {
    @Autowired
    PlazoFijoRepository plazoFijoRepository;

    @Autowired
    TasasConfig tasasConfig;

    public PlazoFijo solicitudPlazoFijo(PlazoFijo plazoFijo) {
        plazoFijo.setStatus("Activo");
        if (plazoFijo.getMonto() > 100000) {
            plazoFijo.setTasa(tasasConfig.getTasa100());
        } else if (plazoFijo.getMonto() > 50000) {
            plazoFijo.setTasa(tasasConfig.getTasa50());
        } else if (plazoFijo.getMonto() > 10000) {
            plazoFijo.setTasa(tasasConfig.getTasa10());
        }
        plazoFijo.setIntereses(plazoFijo.getMonto() * plazoFijo.getTasa());
        plazoFijo.setMontoTotal(plazoFijo.getMonto() + plazoFijo.getIntereses());
        return plazoFijoRepository.save(plazoFijo);
    }

    public String getTasasVigentes() {
        return tasasConfig.toString();
    }

    public List<PlazoFijo> listadoPlazoFijo(Integer numeroCuenta) {
        Cuenta cuenta = buscarCuenta(numeroCuenta);
        return plazoFijoRepository.findAllPlazoFijoByIdCuenta(cuenta.getIdCuenta());
    }

    public PlazoFijo cancelarPlazoFijo(PlazoFijo plazoFijo) {
            plazoFijo.setStatus("Cancelado");
            plazoFijoRepository.save(plazoFijo);
            return plazoFijo;
    }


    public PlazoFijo getPlazoFijo(Integer idPlazoFijo) {
        return plazoFijoRepository.findByIdPlazoFijo(idPlazoFijo);
    }

    public Cuenta buscarCuenta(Integer numeroCuenta) {
        return plazoFijoRepository.findCuentaByNumeroCuenta(numeroCuenta);
    }

    public String comprarDolares(Cuenta cuentaOri, Cuenta cuentaDes, Double cantidad) {
        Double importe=cantidad*19;
        cuentaOri.setBalance(cuentaOri.getBalance()-importe);
        cuentaDes.setBalance(cuentaDes.getBalance()+importe);
        return "finalizada: cuenta origen "+cuentaOri.getNumeroCuenta()+", cuenta destino "+
                cuentaDes.getNumeroCuenta()+", importe pesos "+ importe+ ", importe dolares "+
                cantidad+", tipo operacion compra de dolares, balance cuenta origen "+cuentaOri.getBalance()+
                ", balance cuenta destino "+ cuentaDes.getBalance();
    }
}
