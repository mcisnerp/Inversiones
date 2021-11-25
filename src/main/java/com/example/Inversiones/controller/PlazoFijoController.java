package com.example.Inversiones.controller;

import com.example.Inversiones.entity.PlazoFijo;
import com.example.Inversiones.exception.Exceptions;
import com.example.Inversiones.service.PlazoFijoService;
import net.bytebuddy.implementation.bytecode.assign.TypeCasting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.util.List;

@RestController
@RequestMapping("/PlazoFijo")
public class PlazoFijoController {
    @Autowired
    PlazoFijoService plazoFijoService;

    @PostMapping("/SolicitudDePlazoFijo")
    public ResponseEntity<PlazoFijo> solicitudPlazoFijo(@RequestBody PlazoFijo plazoFijo) {
        //if (No esta autenticado o no tiene una cuenta) {
        //  throw new StatusException("Fallido");
        //} else {
        plazoFijoService.solicitudPlazoFijo(plazoFijo);
        return ResponseEntity.ok(plazoFijo);
        //}
    }

    @GetMapping("/ConsultaTasasPlazoFijo")
    public ResponseEntity<String> getTasasVigentes() {
        return ResponseEntity.ok(plazoFijoService.getTasasVigentes());
    }

    @GetMapping("/ListadoPlazoFijo/{idCuenta}")
    public ResponseEntity<List<PlazoFijo>> getListaPlazoFijo(@PathVariable Integer idCuenta) throws Exceptions {
        List<PlazoFijo> list = plazoFijoService.listadoPlazoFijo(idCuenta);
        if (list.isEmpty()) {
            throw new Exceptions("No cuenta con ningun plazo fijo");
        }
        return ResponseEntity.ok(list);
    }
    @PutMapping("/CancelarPlazoFijo/{idPlazoFijo}")
    public ResponseEntity<String> cancelarPlazoFijo(@PathVariable Integer idPlazoFijo)throws Exceptions{
        plazoFijoService.cancelarPlazoFijo(idPlazoFijo);
        if(plazoFijoService.cancelarPlazoFijo(idPlazoFijo)==null){
            throw new Exceptions("No cuenta con ningun plazo fijo");
        }
        return ResponseEntity.ok("Plazo Fijo Cancelado");
    }

}