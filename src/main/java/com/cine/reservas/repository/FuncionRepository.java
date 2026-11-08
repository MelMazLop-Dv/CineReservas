
package com.cine.reservas.repository;

import com.cine.reservas.entity.Funcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import jakarta.persistence.LockModeType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FuncionRepository extends JpaRepository<Funcion, Long> {

    List<Funcion> findByFecha(LocalDate fecha);

    List<Funcion> findByAsientosDisponiblesGreaterThan(Integer cantidad);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @org.springframework.data.jpa.repository.Query(
            "SELECT f FROM Funcion f WHERE f.id = :id"
    )
    Optional<Funcion> buscarPorIdParaActualizar(
            @org.springframework.data.repository.query.Param("id") Long id
    );
}
