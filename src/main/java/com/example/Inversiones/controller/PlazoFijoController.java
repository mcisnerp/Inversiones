package com.example.Inversiones.controller;

import com.example.Inversiones.config.StatusExceptionHandler;
import com.example.Inversiones.entity.Cuenta;
import com.example.Inversiones.entity.PlazoFijo;
import com.example.Inversiones.exception.ExceptionNotFound;
import com.example.Inversiones.exception.Exceptions;
import com.example.Inversiones.service.PlazoFijoService;
import net.bytebuddy.implementation.bytecode.assign.TypeCasting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.util.List;

@RestController
@RequestMapping("/plazofijo")
public class PlazoFijoController {
    @Autowired
    PlazoFijoService plazoFijoService;

    @PostMapping("/solicituddeplazofijo/{numeroCuenta}")
    public ResponseEntity<PlazoFijo> solicitudPlazoFijo(@RequestBody PlazoFijo plazoFijo, @PathVariable Integer numeroCuenta) throws Exceptions {
        Cuenta cuenta = plazoFijoService.buscarCuenta(numeroCuenta);
        if (cuenta == null) {
            throw new Exceptions("Fallido: No se encuentra el numero de cuenta");
        }
        plazoFijoService.solicitudPlazoFijo(plazoFijo, cuenta);
        return ResponseEntity.ok(plazoFijo);
    }

    @GetMapping("/consultatasasplazofijo")
    public ResponseEntity<String> getTasasVigentes() {
        return ResponseEntity.ok(plazoFijoService.getTasasVigentes());
    }

    @GetMapping("/listadoplazofijo/{numeroCuenta}")
    public ResponseEntity<List<PlazoFijo>> getListaPlazoFijo(@PathVariable Integer numeroCuenta) throws Exceptions, Exception {
        List<PlazoFijo> list = plazoFijoService.listadoPlazoFijo(numeroCuenta);
        if (list.isEmpty()) {
            throw new Exceptions("No cuenta con ningun plazo fijo");
        }
        return ResponseEntity.ok(list);
    }

    @PutMapping("/cancelarplazofijo/{idPlazoFijo}")
    public ResponseEntity<String> cancelarPlazoFijo(@PathVariable Integer idPlazoFijo) throws Exceptions {
        PlazoFijo plazoFijo = plazoFijoService.getPlazoFijo(idPlazoFijo);
        if (plazoFijo != null && plazoFijo.getStatus().equalsIgnoreCase("Activo")) {
            PlazoFijo cancelado = plazoFijoService.cancelarPlazoFijo(plazoFijo);
            if (cancelado != null) {
                return ResponseEntity.ok("Plazo Fijo Cancelado");
            } else {
                throw new Exceptions("No cuenta con ningun plazo fijo");
            }
        } else {
            throw new Exceptions("No cuenta con ningun plazo fijo");
        }
    }

    @GetMapping("/detalleplazofijo/{idPlazoFijo}")
    public ResponseEntity<PlazoFijo> detallePlazofijo(@PathVariable Integer idPlazoFijo) throws ExceptionNotFound {
        PlazoFijo plazoFijo = plazoFijoService.getPlazoFijo(idPlazoFijo);
        if (plazoFijo != null) {
            return ResponseEntity.ok(plazoFijo);
        } else {
            throw new ExceptionNotFound("No se encuentra el plazo fijo");
        }
    }
    //List<PlazoFijo> list = plazoFijoService.listadoPlazoFijo(numeroCuenta);


    @GetMapping("/compradolares/{cuentaOrigen}/{cuentaDestino}/{cantidad}")
    public ResponseEntity<String> compraDolares(@PathVariable Integer cuentaOrigen, @PathVariable Integer cuentaDestino, @PathVariable Double cantidad) {
        Cuenta cuentaOri = plazoFijoService.buscarCuenta(cuentaOrigen);
        Cuenta cuentaDes = plazoFijoService.buscarCuenta(cuentaDestino);
        if (cuentaOri.getMonedaCuenta().equalsIgnoreCase("Dolares") && cuentaDes.getMonedaCuenta().equalsIgnoreCase("pesos")) {
            return ResponseEntity.ok(plazoFijoService.comprarDolares(cuentaOri, cuentaDes, cantidad));
        } else {
            return ResponseEntity.ok("problema con las cuentas");
        }
    }


    @GetMapping("/ventadolares/{cuentaOrigen}/{cuentaDestino}/{cantidad}")
    public ResponseEntity<String> ventaDolares(@PathVariable Integer cuentaOrigen, @PathVariable Integer cuentaDestino, @PathVariable Double cantidad) {
        Cuenta cuentaOri = plazoFijoService.buscarCuenta(cuentaOrigen);
        Cuenta cuentaDes = plazoFijoService.buscarCuenta(cuentaDestino);
        if (cuentaOri.getMonedaCuenta().equalsIgnoreCase("Pesos") && cuentaDes.getMonedaCuenta().equalsIgnoreCase("Dolares")) {
            return ResponseEntity.ok(plazoFijoService.venderDolares(cuentaOri, cuentaDes, cantidad));
        } else {
            return ResponseEntity.ok("problema con las cuentas");
        }
    }
}