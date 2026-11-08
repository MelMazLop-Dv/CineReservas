
package com.cine.reservas.repository;

import com.cine.reservas.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {


    List<Reserva> findByFuncionId(Long funcionId);


    List<Reserva> findByEstado(Reserva.EstadoReserva estado);


    List<Reserva> findByFuncionIdAndEstado(
            Long funcionId,
            Reserva.EstadoReserva estado
    );
}
