package com.example.Inversiones.controller;

import com.example.Inversiones.config.StatusExceptionHandler;
import com.example.Inversiones.entity.Cuenta;
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

    @PostMapping("/SolicitudDePlazoFijo/{numeroCuenta}")
    public ResponseEntity<PlazoFijo> solicitudPlazoFijo(@RequestBody PlazoFijo plazoFijo, @PathVariable Integer numeroCuenta) throws Exceptions {
        Cuenta cuenta = plazoFijoService.buscarCuenta(numeroCuenta);
        if (cuenta == null) {
            throw new Exceptions("Fallido: No se encuentra el numero de cuenta");
        }
        plazoFijo.setIdCuenta(cuenta.getIdCuenta());
        plazoFijoService.solicitudPlazoFijo(plazoFijo);
        return ResponseEntity.ok(plazoFijo);
    }

    @GetMapping("/ConsultaTasasPlazoFijo")
    public ResponseEntity<String> getTasasVigentes() {
        return ResponseEntity.ok(plazoFijoService.getTasasVigentes());
    }

    @GetMapping("/ListadoPlazoFijo/{numeroCuenta}")
    public ResponseEntity<List<PlazoFijo>> getListaPlazoFijo(@PathVariable Integer numeroCuenta) throws Exceptions {
        List<PlazoFijo> list = plazoFijoService.listadoPlazoFijo(numeroCuenta);
        if (list.isEmpty()) {
            throw new Exceptions("No cuenta con ningun plazo fijo");
        }
        return ResponseEntity.ok(list);
    }

    @PutMapping("/CancelarPlazoFijo/{idPlazoFijo}")
    public ResponseEntity<String> cancelarPlazoFijo(@PathVariable Integer idPlazoFijo) throws Exceptions {
        PlazoFijo plazoFijo = plazoFijoService.getPlazoFijo(idPlazoFijo);
        if (plazoFijo != null && plazoFijo.getStatus().equalsIgnoreCase("Activo")) {
            plazoFijoService.cancelarPlazoFijo(plazoFijo);
            return ResponseEntity.ok("Plazo Fijo Cancelado");
        } else {
            throw new Exceptions("No cuenta con ningun plazo fijo");
        }
    }

    @GetMapping("/DetallePlazoFijo/{idPlazoFijo}")
    public ResponseEntity<PlazoFijo> detallePlazofijo(Integer idPlazoFijo) throws Exceptions {
        PlazoFijo plazoFijo = plazoFijoService.getPlazoFijo(idPlazoFijo);
        if (plazoFijo == null) {
            throw new Exceptions("No se encuentra el plazo fijo");
        }
        return ResponseEntity.ok(plazoFijo);
    }

    @GetMapping("/CompraDolares/{cuentaOrigen}/{cuentaDestino}/{cantidad}")
    public ResponseEntity<String> compraDolares(@PathVariable Integer cuentaOrigen, @PathVariable Integer cuentaDestino, @PathVariable Double cantidad) {
        Cuenta cuentaOri = plazoFijoService.buscarCuenta(cuentaOrigen);
        Cuenta cuentaDes = plazoFijoService.buscarCuenta(cuentaDestino);

        if (cuentaOri.getMonedaCuenta().equalsIgnoreCase("Dolares") && cuentaDes.getMonedaCuenta().equalsIgnoreCase("pesos")) {
            return ResponseEntity.ok(plazoFijoService.comprarDolares(cuentaOri,cuentaDes,cantidad));
        }else{
            return ResponseEntity.ok("prolema con las cuentas");
        }
    }

}