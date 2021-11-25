package com.example.Inversiones.repository;

import com.example.Inversiones.entity.PlazoFijo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlazoFijoRepository extends CrudRepository <PlazoFijo,Integer> {
    @Query(value = "Select pf from PlazoFijo pf where pf.idCuenta=:idCuenta and pf.status='Activo'")
    List<PlazoFijo> findAllByIdCuenta(Integer idCuenta);

    //boolean findAllByIdPazoFijo(Integer idPlazoFijo);

    PlazoFijo findByIdPlazoFijo(Integer idPlazoFijo);
}
