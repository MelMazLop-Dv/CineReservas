
"use strict";



const API = "/api";

let funciones = [];
let reservas = [];
let temporizadorNotificacion = null;


function elemento(id) {
    return document.getElementById(id);
}



async function solicitarAPI(ruta, opciones = {}) {
    let respuesta;

    try {
        respuesta = await fetch(API + ruta, {
            ...opciones,
            headers: {
                "Content-Type": "application/json",
                ...opciones.headers
            }
        });
    } catch (error) {
        throw new Error(
            "No se pudo conectar con el servidor. " +
            "Verifica que Spring Boot esté ejecutándose."
        );
    }

    if (!respuesta.ok) {
        let mensaje = "Error HTTP " + respuesta.status;

        try {
            const datos = await respuesta.json();
            mensaje = datos.mensaje || mensaje;
        } catch (error) {
            // Se mantiene el mensaje HTTP original.
        }

        throw new Error(mensaje);
    }

    if (respuesta.status === 204) {
        return null;
    }

    return await respuesta.json();
}



function notificar(mensaje, tipo = "exito") {
    const caja = elemento("notificacion");

    clearTimeout(temporizadorNotificacion);

    caja.textContent = mensaje;
    caja.className = "notificacion " + tipo;
    caja.hidden = false;

    temporizadorNotificacion = setTimeout(() => {
        caja.hidden = true;
    }, 4500);
}

function mostrarError(error) {
    console.error(error);
    notificar(error.message || "Ocurrió un error", "error");
}



function formatoMoneda(valor) {
    return new Intl.NumberFormat("es-GT", {
        style: "currency",
        currency: "GTQ"
    }).format(valor);
}

function formatoFecha(fecha) {
    if (!fecha) return "";

    const partes = fecha.split("-");

    return partes[2] + "/" + partes[1] + "/" + partes[0];
}

function formatoHora(hora) {
    return hora ? hora.substring(0, 5) : "";
}

// Crear elementos de texto de forma segura
function celdaTexto(fila, valor) {
    const celda = document.createElement("td");
    celda.textContent = String(valor ?? "");
    fila.appendChild(celda);
    return celda;
}

function crearBoton(texto, clase, accion) {
    const boton = document.createElement("button");

    boton.type = "button";
    boton.className = "btn btn-small " + clase;
    boton.textContent = texto;
    boton.addEventListener("click", accion);

    return boton;
}



async function cargarFunciones(ruta = "/funciones") {
    funciones = await solicitarAPI(ruta);
    mostrarFunciones();
    await actualizarEstadisticas();
}

async function cargarReservas(ruta = "/reservas") {
    reservas = await solicitarAPI(ruta);
    mostrarReservas();
    await actualizarEstadisticas();
}

async function actualizarEstadisticas() {
    const todasFunciones = await solicitarAPI("/funciones");
    const todasReservas = await solicitarAPI("/reservas");

    elemento("totalFunciones").textContent =
        todasFunciones.length;

    elemento("totalReservas").textContent =
        todasReservas.filter(
            reserva => reserva.estado === "CONFIRMADA"
        ).length;

    elemento("totalAsientos").textContent =
        todasFunciones.reduce(
            (total, funcion) =>
                total + funcion.asientosDisponibles,
            0
        );
}

async function actualizarTodo() {
    const todasFunciones = await solicitarAPI("/funciones");
    const todasReservas = await solicitarAPI("/reservas");

    funciones = todasFunciones;
    reservas = todasReservas;

    mostrarFunciones();
    mostrarReservas();

    elemento("totalFunciones").textContent =
        todasFunciones.length;

    elemento("totalReservas").textContent =
        todasReservas.filter(
            reserva => reserva.estado === "CONFIRMADA"
        ).length;

    elemento("totalAsientos").textContent =
        todasFunciones.reduce(
            (total, funcion) =>
                total + funcion.asientosDisponibles,
            0
        );
}



function mostrarFunciones() {
    const tabla = elemento("tablaFunciones");
    tabla.replaceChildren();

    if (funciones.length === 0) {
        const fila = document.createElement("tr");
        const celda = celdaTexto(
            fila,
            "No se encontraron funciones."
        );

        celda.colSpan = 9;
        celda.className = "empty-message";
        tabla.appendChild(fila);
        return;
    }

    funciones.forEach(funcion => {
        const fila = document.createElement("tr");

        celdaTexto(fila, funcion.id);
        celdaTexto(fila, funcion.pelicula);
        celdaTexto(fila, formatoFecha(funcion.fecha));
        celdaTexto(fila, formatoHora(funcion.hora));
        celdaTexto(fila, funcion.sala);
        celdaTexto(
            fila,
            formatoMoneda(funcion.precioEntrada)
        );

        celdaTexto(
            fila,
            funcion.asientosDisponibles +
            " / " + funcion.capacidad
        );

        celdaTexto(
            fila,
            Number(funcion.porcentajeOcupacion).toFixed(2) + "%"
        );

        const acciones = document.createElement("td");

        acciones.appendChild(
            crearBoton("Editar", "btn-secondary", () => {
                editarFuncion(funcion.id);
            })
        );

        acciones.appendChild(
            crearBoton("Eliminar", "btn-danger", () => {
                eliminarFuncion(funcion.id);
            })
        );

        fila.appendChild(acciones);
        tabla.appendChild(fila);
    });
}

// ==========================================
// FORMULARIO DE FUNCIONES
// ==========================================

function nuevaFuncion() {
    elemento("formFuncion").reset();
    elemento("funcionId").value = "";

    elemento("tituloFormularioFuncion").textContent =
        "Registrar nueva función";

    elemento("panelFuncion").hidden = false;

    elemento("panelFuncion").scrollIntoView({
        behavior: "smooth"
    });
}

function editarFuncion(id) {
    const funcion = funciones.find(f => f.id === id);

    if (!funcion) {
        notificar("No se encontró la función.", "error");
        return;
    }

    elemento("funcionId").value = funcion.id;
    elemento("pelicula").value = funcion.pelicula;
    elemento("fecha").value = funcion.fecha;
    elemento("hora").value = formatoHora(funcion.hora);
    elemento("sala").value = funcion.sala;
    elemento("capacidad").value = funcion.capacidad;
    elemento("precioEntrada").value = funcion.precioEntrada;

    elemento("tituloFormularioFuncion").textContent =
        "Modificar función";

    elemento("panelFuncion").hidden = false;

    elemento("panelFuncion").scrollIntoView({
        behavior: "smooth"
    });
}

async function guardarFuncion(evento) {
    evento.preventDefault();

    const id = elemento("funcionId").value;

    const datos = {
        pelicula: elemento("pelicula").value.trim(),
        fecha: elemento("fecha").value,
        hora: elemento("hora").value + ":00",
        sala: elemento("sala").value.trim(),
        capacidad: Number(elemento("capacidad").value),
        precioEntrada: Number(
            elemento("precioEntrada").value
        )
    };

    try {
        await solicitarAPI(
            id ? "/funciones/" + id : "/funciones",
            {
                method: id ? "PUT" : "POST",
                body: JSON.stringify(datos)
            }
        );

        notificar(
            id
                ? "Función actualizada correctamente."
                : "Función registrada correctamente."
        );

        elemento("panelFuncion").hidden = true;
        elemento("formFuncion").reset();

        await actualizarTodo();

    } catch (error) {
        mostrarError(error);
    }
}

async function eliminarFuncion(id) {
    const confirmar = confirm(
        "¿Deseas eliminar esta función?\n" +
        "No se puede eliminar si tiene reservas asociadas."
    );

    if (!confirmar) return;

    try {
        await solicitarAPI("/funciones/" + id, {
            method: "DELETE"
        });

        notificar("Función eliminada correctamente.");
        await actualizarTodo();

    } catch (error) {
        mostrarError(error);
    }
}



async function filtrarFunciones() {
    const fecha = elemento("filtroFecha").value;

    if (!fecha) {
        notificar("Selecciona una fecha.", "error");
        return;
    }

    try {
        await cargarFunciones(
            "/funciones/fecha?fecha=" +
            encodeURIComponent(fecha)
        );
    } catch (error) {
        mostrarError(error);
    }
}



function mostrarReservas() {
    const tabla = elemento("tablaReservas");
    tabla.replaceChildren();

    if (reservas.length === 0) {
        const fila = document.createElement("tr");

        const celda = celdaTexto(
            fila,
            "No se encontraron reservas."
        );

        celda.colSpan = 7;
        celda.className = "empty-message";

        tabla.appendChild(fila);
        return;
    }

    reservas.forEach(reserva => {
        const fila = document.createElement("tr");

        celdaTexto(fila, reserva.id);
        celdaTexto(fila, reserva.nombreCliente);
        celdaTexto(fila, reserva.pelicula);
        celdaTexto(fila, reserva.cantidadEntradas);

        celdaTexto(
            fila,
            formatoMoneda(reserva.precioTotal)
        );

        const celdaEstado = document.createElement("td");
        const etiqueta = document.createElement("span");

        etiqueta.textContent = reserva.estado;
        etiqueta.className =
            "badge " +
            (reserva.estado === "CONFIRMADA"
                ? "badge-confirmada"
                : "badge-cancelada");

        celdaEstado.appendChild(etiqueta);
        fila.appendChild(celdaEstado);

        const acciones = document.createElement("td");

        if (reserva.estado === "CONFIRMADA") {
            acciones.appendChild(
                crearBoton("Editar", "btn-secondary", () => {
                    editarReserva(reserva.id);
                })
            );

            acciones.appendChild(
                crearBoton("Cancelar", "btn-danger", () => {
                    cancelarReserva(reserva.id);
                })
            );
        } else {
            acciones.textContent = "Sin acciones";
        }

        fila.appendChild(acciones);
        tabla.appendChild(fila);
    });
}



async function llenarSelectorFunciones() {
    const selector = elemento("reservaFuncion");
    selector.replaceChildren();

    const opcionInicial = document.createElement("option");
    opcionInicial.value = "";
    opcionInicial.textContent = "Seleccione una función";
    selector.appendChild(opcionInicial);

    const disponibles = await solicitarAPI(
        "/funciones/disponibles"
    );

    disponibles.forEach(funcion => {
        const opcion = document.createElement("option");

        opcion.value = funcion.id;

        opcion.textContent =
            funcion.pelicula +
            " | " + formatoFecha(funcion.fecha) +
            " | " + formatoHora(funcion.hora) +
            " | " + funcion.asientosDisponibles +
            " asientos | " +
            formatoMoneda(funcion.precioEntrada);

        selector.appendChild(opcion);
    });
}

async function nuevaReserva() {
    try {
        elemento("formReserva").reset();
        elemento("reservaId").value = "";

        await llenarSelectorFunciones();

        elemento("reservaFuncion").disabled = false;

        elemento("tituloFormularioReserva").textContent =
            "Registrar nueva reserva";

        elemento("panelReserva").hidden = false;

        elemento("panelReserva").scrollIntoView({
            behavior: "smooth"
        });

    } catch (error) {
        mostrarError(error);
    }
}

async function editarReserva(id) {
    const reserva = reservas.find(r => r.id === id);

    if (!reserva) {
        notificar("No se encontró la reserva.", "error");
        return;
    }

    if (reserva.estado === "CANCELADA") {
        notificar(
            "No se puede modificar una reserva cancelada.",
            "error"
        );
        return;
    }

    try {
        await llenarSelectorFunciones();

        const selector = elemento("reservaFuncion");

        // La función no se puede cambiar durante la edición.
        if (![...selector.options].some(
            opcion => opcion.value === String(reserva.funcionId)
        )) {
            const opcion = document.createElement("option");
            opcion.value = reserva.funcionId;
            opcion.textContent = reserva.pelicula;
            selector.appendChild(opcion);
        }

        elemento("reservaId").value = reserva.id;
        selector.value = reserva.funcionId;
        selector.disabled = true;

        elemento("nombreCliente").value =
            reserva.nombreCliente;

        elemento("cantidadEntradas").value =
            reserva.cantidadEntradas;

        elemento("tituloFormularioReserva").textContent =
            "Modificar reserva";

        elemento("panelReserva").hidden = false;

        elemento("panelReserva").scrollIntoView({
            behavior: "smooth"
        });

    } catch (error) {
        mostrarError(error);
    }
}

async function guardarReserva(evento) {
    evento.preventDefault();

    const id = elemento("reservaId").value;

    const datos = {
        nombreCliente:
            elemento("nombreCliente").value.trim(),

        cantidadEntradas:
            Number(elemento("cantidadEntradas").value)
    };

    if (!id) {
        datos.funcionId = Number(
            elemento("reservaFuncion").value
        );
    }

    try {
        await solicitarAPI(
            id ? "/reservas/" + id : "/reservas",
            {
                method: id ? "PUT" : "POST",
                body: JSON.stringify(datos)
            }
        );

        notificar(
            id
                ? "Reserva actualizada correctamente."
                : "Reserva registrada correctamente."
        );

        elemento("panelReserva").hidden = true;
        elemento("reservaFuncion").disabled = false;
        elemento("formReserva").reset();

        await actualizarTodo();

    } catch (error) {
        mostrarError(error);
    }
}

// ==========================================
// CANCELAR RESERVAS
// ==========================================

async function cancelarReserva(id) {
    const confirmar = confirm(
        "¿Deseas cancelar esta reserva?\n" +
        "Los asientos volverán a estar disponibles."
    );

    if (!confirmar) return;

    try {
        await solicitarAPI(
            "/reservas/" + id + "/cancelar",
            { method: "PATCH" }
        );

        notificar("Reserva cancelada correctamente.");
        await actualizarTodo();

    } catch (error) {
        mostrarError(error);
    }
}



async function filtrarReservas() {
    const estado = elemento("filtroEstado").value;

    try {
        await cargarReservas(
            estado
                ? "/reservas?estado=" +
                encodeURIComponent(estado)
                : "/reservas"
        );

    } catch (error) {
        mostrarError(error);
    }
}



document.addEventListener("DOMContentLoaded", async () => {

    elemento("btnNuevaFuncion").addEventListener(
        "click", nuevaFuncion
    );

    elemento("btnCancelarFuncion").addEventListener(
        "click", () => {
            elemento("panelFuncion").hidden = true;
        }
    );

    elemento("formFuncion").addEventListener(
        "submit", guardarFuncion
    );

    elemento("btnFiltrarFunciones").addEventListener(
        "click", filtrarFunciones
    );

    elemento("btnTodasFunciones").addEventListener(
        "click", async () => {
            elemento("filtroFecha").value = "";

            try {
                await cargarFunciones();
            } catch (error) {
                mostrarError(error);
            }
        }
    );

    elemento("btnNuevaReserva").addEventListener(
        "click", nuevaReserva
    );

    elemento("btnCancelarFormularioReserva").addEventListener(
        "click", () => {
            elemento("panelReserva").hidden = true;
            elemento("reservaFuncion").disabled = false;
        }
    );

    elemento("formReserva").addEventListener(
        "submit", guardarReserva
    );

    elemento("btnFiltrarReservas").addEventListener(
        "click", filtrarReservas
    );

    elemento("btnTodasReservas").addEventListener(
        "click", async () => {
            elemento("filtroEstado").value = "";

            try {
                await cargarReservas();
            } catch (error) {
                mostrarError(error);
            }
        }
    );


    try {
        await actualizarTodo();
    } catch (error) {
        mostrarError(error);
    }
});
