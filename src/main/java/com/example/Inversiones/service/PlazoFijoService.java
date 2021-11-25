package com.example.Inversiones.service;

import com.example.Inversiones.config.TasasConfig;
import com.example.Inversiones.entity.PlazoFijo;
import com.example.Inversiones.repository.PlazoFijoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<PlazoFijo> listadoPlazoFijo(Integer idCuenta) {
        return plazoFijoRepository.findAllByIdCuenta(idCuenta);
    }

    public PlazoFijo cancelarPlazoFijo(Integer idPlazoFijo) {
        PlazoFijo plazoFijo = getPlazoFijo(idPlazoFijo);
        if (plazoFijo != null && plazoFijo.getStatus().equals("Activo")) {
            plazoFijo.setStatus("Cancelado");
            plazoFijoRepository.save(plazoFijo);
        }
        return plazoFijo;
    }


    public PlazoFijo getPlazoFijo(Integer idPlazoFijo) {
        return plazoFijoRepository.findByIdPlazoFijo(idPlazoFijo);
    }
}
