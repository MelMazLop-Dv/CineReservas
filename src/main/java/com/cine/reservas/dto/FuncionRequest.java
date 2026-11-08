
package com.cine.reservas.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class FuncionRequest {

    @NotBlank(message = "El nombre de la película es obligatorio")
    @Size(max = 150, message = "La película no puede superar 150 caracteres")
    private String pelicula;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotNull(message = "La hora es obligatoria")
    private LocalTime hora;

    @NotBlank(message = "La sala es obligatoria")
    @Size(max = 50, message = "La sala no puede superar 50 caracteres")
    private String sala;

    @NotNull(message = "La capacidad es obligatoria")
    @Positive(message = "La capacidad debe ser mayor que cero")
    private Integer capacidad;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01",
            message = "El precio debe ser mayor que cero")
    @Digits(integer = 8, fraction = 2,
            message = "El precio debe tener máximo 8 enteros y 2 decimales")
    private BigDecimal precioEntrada;

    public FuncionRequest() {
    }

    public String getPelicula() {
        return pelicula;
    }

    public void setPelicula(String pelicula) {
        this.pelicula = pelicula;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public BigDecimal getPrecioEntrada() {
        return precioEntrada;
    }

    public void setPrecioEntrada(BigDecimal precioEntrada) {
        this.precioEntrada = precioEntrada;
    }
}
