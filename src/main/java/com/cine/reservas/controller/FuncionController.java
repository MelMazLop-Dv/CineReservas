
package com.cine.reservas.controller;

import com.cine.reservas.dto.FuncionRequest;
import com.cine.reservas.dto.FuncionResponse;
import com.cine.reservas.entity.Funcion;
import com.cine.reservas.service.FuncionService;

import jakarta.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/funciones")
public class FuncionController {

    private final FuncionService funcionService;

    public FuncionController(FuncionService funcionService) {
        this.funcionService = funcionService;
    }


    @GetMapping
    public List<FuncionResponse> listarFunciones() {

        return funcionService.listarFunciones()
                .stream()
                .map(FuncionResponse::new)
                .toList();
    }


    @GetMapping("/disponibles")
    public List<FuncionResponse> funcionesDisponibles() {

        return funcionService.funcionesDisponibles()
                .stream()
                .map(FuncionResponse::new)
                .toList();
    }


    @GetMapping("/fecha")
    public List<FuncionResponse> buscarPorFecha(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fecha) {

        return funcionService.buscarPorFecha(fecha)
                .stream()
                .map(FuncionResponse::new)
                .toList();
    }


    @GetMapping("/{id}/ocupacion")
    public double calcularOcupacion(@PathVariable Long id) {

        return funcionService.calcularOcupacion(id);
    }


    @GetMapping("/{id}")
    public FuncionResponse buscarPorId(@PathVariable Long id) {

        Funcion funcion = funcionService.buscarPorId(id);

        return new FuncionResponse(funcion);
    }


    @PostMapping
    public ResponseEntity<FuncionResponse> crearFuncion(
            @Valid @RequestBody FuncionRequest request) {

        Funcion funcion = convertirAEntidad(request);

        Funcion nuevaFuncion =
                funcionService.crearFuncion(funcion);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new FuncionResponse(nuevaFuncion));
    }


    @PutMapping("/{id}")
    public FuncionResponse actualizarFuncion(
            @PathVariable Long id,
            @Valid @RequestBody FuncionRequest request) {

        Funcion datos = convertirAEntidad(request);

        Funcion actualizada =
                funcionService.actualizarFuncion(id, datos);

        return new FuncionResponse(actualizada);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarFuncion(
            @PathVariable Long id) {

        funcionService.eliminarFuncion(id);

        return ResponseEntity.noContent().build();
    }


    private Funcion convertirAEntidad(FuncionRequest request) {

        Funcion funcion = new Funcion();

        funcion.setPelicula(request.getPelicula().trim());
        funcion.setFecha(request.getFecha());
        funcion.setHora(request.getHora());
        funcion.setSala(request.getSala().trim());
        funcion.setCapacidad(request.getCapacidad());
        funcion.setPrecioEntrada(request.getPrecioEntrada());

        return funcion;
    }
}
