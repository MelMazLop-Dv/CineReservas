
package com.cine.reservas.service;

import com.cine.reservas.entity.Funcion;
import com.cine.reservas.entity.Reserva;
import com.cine.reservas.repository.FuncionRepository;
import com.cine.reservas.repository.ReservaRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final FuncionRepository funcionRepository;

    public ReservaService(
            ReservaRepository reservaRepository,
            FuncionRepository funcionRepository) {

        this.reservaRepository = reservaRepository;
        this.funcionRepository = funcionRepository;
    }


    @Transactional(readOnly = true)
    public List<Reserva> listarReservas() {
        return reservaRepository.findAll();
    }


    @Transactional(readOnly = true)
    public Reserva buscarPorId(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "No existe una reserva con ID: " + id
                        )
                );
    }

    @Transactional
    public Reserva crearReserva(
            Long funcionId,
            String nombreCliente,
            Integer cantidadEntradas) {


        if (nombreCliente == null ||
                nombreCliente.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre del cliente es obligatorio"
            );
        }


        if (cantidadEntradas == null ||
                cantidadEntradas <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad de entradas debe ser mayor que cero"
            );
        }


        Funcion funcion = funcionRepository
                .buscarPorIdParaActualizar(funcionId)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "No existe una función con ID: " + funcionId
                        )
                );


        if (cantidadEntradas >
                funcion.getAsientosDisponibles()) {
            throw new IllegalArgumentException(
                    "No hay suficientes asientos disponibles"
            );
        }


        BigDecimal precioTotal = funcion.getPrecioEntrada()
                .multiply(BigDecimal.valueOf(cantidadEntradas));


        Reserva reserva = new Reserva();

        reserva.setNombreCliente(nombreCliente.trim());
        reserva.setCantidadEntradas(cantidadEntradas);
        reserva.setPrecioTotal(precioTotal);
        reserva.setEstado(Reserva.EstadoReserva.CONFIRMADA);
        reserva.setFuncion(funcion);


        funcion.setAsientosDisponibles(
                funcion.getAsientosDisponibles()
                        - cantidadEntradas
        );


        funcionRepository.save(funcion);

        return reservaRepository.save(reserva);
    }


    @Transactional
    public Reserva cancelarReserva(Long reservaId) {


        Reserva reservaInicial = reservaRepository
                .findById(reservaId)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "No existe una reserva con ID: " + reservaId
                        )
                );

        Long funcionId = reservaInicial.getFuncion().getId();


        Funcion funcion = funcionRepository
                .buscarPorIdParaActualizar(funcionId)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "No existe la función asociada"
                        )
                );


        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "No existe una reserva con ID: " + reservaId
                        )
                );


        if (reserva.getEstado() ==
                Reserva.EstadoReserva.CANCELADA) {
            throw new IllegalStateException(
                    "Esta reserva ya fue cancelada"
            );
        }


        funcion.setAsientosDisponibles(
                funcion.getAsientosDisponibles()
                        + reserva.getCantidadEntradas()
        );


        reserva.setEstado(Reserva.EstadoReserva.CANCELADA);

        funcionRepository.save(funcion);

        return reservaRepository.save(reserva);
    }


    @Transactional(readOnly = true)
    public List<Reserva> buscarPorFuncion(Long funcionId) {
        return reservaRepository.findByFuncionId(funcionId);
    }


    @Transactional(readOnly = true)
    public List<Reserva> buscarPorEstado(
            Reserva.EstadoReserva estado) {
        return reservaRepository.findByEstado(estado);
    }


    @Transactional(readOnly = true)
    public List<Reserva> buscarPorFuncionYEstado(
            Long funcionId,
            Reserva.EstadoReserva estado) {

        return reservaRepository
                .findByFuncionIdAndEstado(funcionId, estado);
    }


    @Transactional
    public Reserva actualizarReserva(
            Long reservaId,
            String nombreCliente,
            Integer cantidadEntradas) {


        if (nombreCliente == null || nombreCliente.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre del cliente es obligatorio");
        }

        if (cantidadEntradas == null || cantidadEntradas <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad de entradas debe ser mayor que cero");
        }


        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new NoSuchElementException(
                        "No existe una reserva con ID: " + reservaId));

        Long funcionId = reserva.getFuncion().getId();


        Funcion funcion = funcionRepository
                .buscarPorIdParaActualizar(funcionId)
                .orElseThrow(() -> new NoSuchElementException(
                        "No existe una función con ID: " + funcionId));



        reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new NoSuchElementException(
                        "No existe una reserva con ID: " + reservaId));


        if (reserva.getEstado() == Reserva.EstadoReserva.CANCELADA) {
            throw new IllegalStateException(
                    "No se puede modificar una reserva cancelada");
        }


        int diferencia = cantidadEntradas - reserva.getCantidadEntradas();


        if (diferencia > funcion.getAsientosDisponibles()) {
            throw new IllegalArgumentException(
                    "No hay suficientes asientos disponibles");
        }


        funcion.setAsientosDisponibles(
                funcion.getAsientosDisponibles() - diferencia);


        reserva.setNombreCliente(nombreCliente.trim());
        reserva.setCantidadEntradas(cantidadEntradas);


        reserva.setPrecioTotal(
                funcion.getPrecioEntrada().multiply(
                        BigDecimal.valueOf(cantidadEntradas)));


        funcionRepository.save(funcion);

        return reservaRepository.save(reserva);
    }

}
