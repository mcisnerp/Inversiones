package com.example.Inversiones.repository;

import com.example.Inversiones.entity.Cuenta;
import com.example.Inversiones.entity.PlazoFijo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlazoFijoRepository extends CrudRepository <PlazoFijo,Integer> {
    //@Query(value = "Select c from Cuenta c where c.numeroCuenta=:numeroCuenta")// and pf.status='Activo'")
    //Cuenta findCuentaByNumeroCuenta(Integer numeroCuenta);

    @Query(value = "Select pf from PlazoFijo pf where pf.idPlazoFijo=:idPlazoFijo")// and pf.status='Activo'")
    PlazoFijo findByIdPlazoFijo(Integer idPlazoFijo);

    //@Query(value = "Select pf from PlazoFijo pf join Cuenta c where c.numeroCuenta=:numeroCuenta")// and pf.status='Activo'")
   // List<PlazoFijo> findAllPlazoFijoByNumeroCuenta(Integer numeroCuenta);

    //@Query(value = "Select pf from PlazoFijo pf where pf.idCuenta=:idCuenta")// and pf.status='Activo'")
    List<PlazoFijo> findAllPlazoFijoByNumeroCuenta(Integer numeroCuenta);

}
