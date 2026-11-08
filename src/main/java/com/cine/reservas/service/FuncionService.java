
package com.cine.reservas.service;

import com.cine.reservas.entity.Funcion;
import com.cine.reservas.repository.FuncionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class FuncionService {

    private final FuncionRepository funcionRepository;

    public FuncionService(FuncionRepository funcionRepository) {
        this.funcionRepository = funcionRepository;
    }

    @Transactional(readOnly = true)
    public List<Funcion> listarFunciones() {
        return funcionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Funcion buscarPorId(Long id) {
        return funcionRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "No existe una función con ID: " + id
                        )
                );
    }


    @Transactional
    public Funcion crearFuncion(Funcion funcion) {

        validarDatos(funcion);


        funcion.setId(null);


        funcion.setAsientosDisponibles(
                funcion.getCapacidad()
        );

        return funcionRepository.save(funcion);
    }


    @Transactional
    public Funcion actualizarFuncion(Long id, Funcion datos) {

        validarDatos(datos);

        Funcion funcion = funcionRepository
                .buscarPorIdParaActualizar(id)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "No existe una función con ID: " + id
                        )
                );

        int asientosOcupados =
                funcion.getCapacidad()
                        - funcion.getAsientosDisponibles();

        if (datos.getCapacidad() < asientosOcupados) {
            throw new IllegalArgumentException(
                    "La nueva capacidad no puede ser menor " +
                            "que los asientos ocupados"
            );
        }

        funcion.setPelicula(datos.getPelicula());
        funcion.setFecha(datos.getFecha());
        funcion.setHora(datos.getHora());
        funcion.setSala(datos.getSala());
        funcion.setPrecioEntrada(datos.getPrecioEntrada());
        funcion.setCapacidad(datos.getCapacidad());

        funcion.setAsientosDisponibles(
                datos.getCapacidad() - asientosOcupados
        );

        return funcionRepository.save(funcion);
    }


    @Transactional
    public void eliminarFuncion(Long id) {

        Funcion funcion = funcionRepository
                .buscarPorIdParaActualizar(id)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "No existe una función con ID: " + id
                        )
                );

        if (!funcion.getReservas().isEmpty()) {
            throw new IllegalStateException(
                    "No se puede eliminar una función " +
                            "que tiene reservas registradas"
            );
        }

        funcionRepository.delete(funcion);
    }


    @Transactional(readOnly = true)
    public List<Funcion> buscarPorFecha(LocalDate fecha) {
        return funcionRepository.findByFecha(fecha);
    }


    @Transactional(readOnly = true)
    public List<Funcion> funcionesDisponibles() {
        return funcionRepository
                .findByAsientosDisponiblesGreaterThan(0);
    }


    @Transactional(readOnly = true)
    public double calcularOcupacion(Long id) {

        Funcion funcion = buscarPorId(id);

        int ocupados = funcion.getCapacidad()
                - funcion.getAsientosDisponibles();

        return (ocupados * 100.0)
                / funcion.getCapacidad();
    }


    private void validarDatos(Funcion funcion) {

        if (funcion == null) {
            throw new IllegalArgumentException(
                    "Los datos de la función son obligatorios"
            );
        }

        if (funcion.getPelicula() == null ||
                funcion.getPelicula().isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre de la película es obligatorio"
            );
        }

        if (funcion.getFecha() == null) {
            throw new IllegalArgumentException(
                    "La fecha es obligatoria"
            );
        }

        if (funcion.getHora() == null) {
            throw new IllegalArgumentException(
                    "La hora es obligatoria"
            );
        }

        if (funcion.getSala() == null ||
                funcion.getSala().isBlank()) {
            throw new IllegalArgumentException(
                    "La sala es obligatoria"
            );
        }

        if (funcion.getCapacidad() == null ||
                funcion.getCapacidad() <= 0) {
            throw new IllegalArgumentException(
                    "La capacidad debe ser mayor que cero"
            );
        }

        if (funcion.getPrecioEntrada() == null ||
                funcion.getPrecioEntrada()
                        .compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "El precio debe ser mayor que cero"
            );
        }
    }
}
