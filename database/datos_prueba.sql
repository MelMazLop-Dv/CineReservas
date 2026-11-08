
-- PROYECTO: CineReservas
-- Datos de prueba
-- Ejecutar en una base de datos de prueba vacía

INSERT INTO funciones (
    pelicula,
    fecha,
    hora,
    sala,
    capacidad,
    asientos_disponibles,
    precio_entrada
)
VALUES
    (
        'Minecraft: La película',
        '2026-11-16',
        '19:00:00',
        'Sala 2',
        80,
        75,
        40.00
    ),
    (
        'Intensamente 2',
        '2026-11-21',
        '17:00:00',
        'Sala 3',
        50,
        50,
        35.00
    );

-- Reserva de ejemplo para la primera función
INSERT INTO reservas (
    nombre_cliente,
    cantidad_entradas,
    precio_total,
    estado,
    funcion_id
)
SELECT
    'Andrea Morales',
    5,
    200.00,
    'CONFIRMADA',
    id
FROM funciones
WHERE pelicula = 'Minecraft: La película'
  AND fecha = '2026-11-16'
ORDER BY id
    LIMIT 1;
