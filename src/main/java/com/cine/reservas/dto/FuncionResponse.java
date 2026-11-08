
package com.cine.reservas.dto;

import com.cine.reservas.entity.Funcion;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class FuncionResponse {

    private Long id;
    private String pelicula;
    private LocalDate fecha;
    private LocalTime hora;
    private String sala;
    private Integer capacidad;
    private Integer asientosDisponibles;
    private BigDecimal precioEntrada;
    private Double porcentajeOcupacion;

    public FuncionResponse() {
    }

    public FuncionResponse(Funcion funcion) {

        this.id = funcion.getId();
        this.pelicula = funcion.getPelicula();
        this.fecha = funcion.getFecha();
        this.hora = funcion.getHora();
        this.sala = funcion.getSala();
        this.capacidad = funcion.getCapacidad();
        this.asientosDisponibles =
                funcion.getAsientosDisponibles();
        this.precioEntrada = funcion.getPrecioEntrada();

        int asientosOcupados =
                funcion.getCapacidad()
                        - funcion.getAsientosDisponibles();

        this.porcentajeOcupacion =
                (asientosOcupados * 100.0)
                        / funcion.getCapacidad();
    }

    public Long getId() {
        return id;
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

    public Integer getCapacidad() {
        return capacidad;
    }

    public Integer getAsientosDisponibles() {
        return asientosDisponibles;
    }

    public BigDecimal getPrecioEntrada() {
        return precioEntrada;
    }

    public Double getPorcentajeOcupacion() {
        return porcentajeOcupacion;
    }
}
