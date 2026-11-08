
package com.cine.reservas.dto;

import com.cine.reservas.entity.Reserva;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class ReservaResponse {

    private Long id;
    private Long funcionId;
    private String pelicula;
    private LocalDate fecha;
    private LocalTime hora;
    private String sala;
    private String nombreCliente;
    private Integer cantidadEntradas;
    private BigDecimal precioTotal;
    private String estado;

    public ReservaResponse() {
    }

    public ReservaResponse(Reserva reserva) {

        this.id = reserva.getId();

        this.funcionId =
                reserva.getFuncion().getId();

        this.pelicula =
                reserva.getFuncion().getPelicula();

        this.fecha =
                reserva.getFuncion().getFecha();

        this.hora =
                reserva.getFuncion().getHora();

        this.sala =
                reserva.getFuncion().getSala();

        this.nombreCliente =
                reserva.getNombreCliente();

        this.cantidadEntradas =
                reserva.getCantidadEntradas();

        this.precioTotal =
                reserva.getPrecioTotal();

        this.estado =
                reserva.getEstado().name();
    }

    public Long getId() {
        return id;
    }

    public Long getFuncionId() {
        return funcionId;
    }

    public String getPelicula() {
        return pelicula;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public String getSala() {
        return sala;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public Integer getCantidadEntradas() {
        return cantidadEntradas;
    }

    public BigDecimal getPrecioTotal() {
        return precioTotal;
    }

    public String getEstado() {
        return estado;
    }
}
