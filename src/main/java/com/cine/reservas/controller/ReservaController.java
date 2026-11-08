
package com.cine.reservas.controller;

import com.cine.reservas.dto.ReservaRequest;
import com.cine.reservas.dto.ReservaResponse;
import com.cine.reservas.entity.Reserva;
import com.cine.reservas.service.ReservaService;
import com.cine.reservas.dto.ActualizarReservaRequest;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }


    @GetMapping
    public List<ReservaResponse> listarReservas(
            @RequestParam(required = false) Long funcionId,
            @RequestParam(required = false)
            Reserva.EstadoReserva estado) {

        List<Reserva> reservas;

        if (funcionId != null && estado != null) {
            reservas = reservaService
                    .buscarPorFuncionYEstado(funcionId, estado);

        } else if (funcionId != null) {
            reservas = reservaService.buscarPorFuncion(funcionId);

        } else if (estado != null) {
            reservas = reservaService.buscarPorEstado(estado);

        } else {
            reservas = reservaService.listarReservas();
        }

        return reservas.stream()
                .map(ReservaResponse::new)
                .toList();
    }


    @GetMapping("/{id}")
    public ReservaResponse buscarPorId(@PathVariable Long id) {

        Reserva reserva = reservaService.buscarPorId(id);

        return new ReservaResponse(reserva);
    }


    @PostMapping
    public ResponseEntity<ReservaResponse> crearReserva(
            @Valid @RequestBody ReservaRequest request) {

        Reserva reserva = reservaService.crearReserva(
                request.getFuncionId(),
                request.getNombreCliente(),
                request.getCantidadEntradas()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ReservaResponse(reserva));
    }


    @PatchMapping("/{id}/cancelar")
    public ReservaResponse cancelarReserva(
            @PathVariable Long id) {

        Reserva reserva = reservaService.cancelarReserva(id);

        return new ReservaResponse(reserva);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ReservaResponse> actualizarReserva(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarReservaRequest request) {

        Reserva reservaActualizada = reservaService.actualizarReserva(
                id,
                request.getNombreCliente(),
                request.getCantidadEntradas()
        );

        return ResponseEntity.ok(
                new ReservaResponse(reservaActualizada)
        );
    }

}
