# Estructura de AuthCares

Esta guía explica dónde vive cada parte de la aplicación y cómo agregar funciones nuevas sin desordenar el proyecto.

## Mapa principal

```text
com.choque.authcares2/
├── MainActivity.kt              Entrada de Android y permisos del teléfono
├── app/
│   └── AuthCaresApp.kt          Estado general y barras de navegación
├── core/
│   └── model/                   Datos compartidos entre varias funciones
├── features/
│   ├── alerts/                  Alertas, emergencias y sus pantallas
│   ├── assistant/               Asistente de IA, reglas y pantalla
│   ├── auth/                    Inicio de sesión y registro
│   ├── home/                    Inicio y listado de niños
│   ├── monitoring/              Lectura del reloj y sensores
│   ├── profile/                 Perfiles
│   ├── settings/                Configuraciones
│   ├── share/                   Compartir información
│   └── stats/                   Estadísticas
├── navigation/
│   ├── AuthCaresScreen.kt       Lista de destinos
│   └── AuthCaresNavGraph.kt     Conexión entre pantallas
└── ui/
    ├── components/              Piezas visuales reutilizables
    └── theme/                   Colores, tipografía y tema
```

## Cómo agregar una función

1. Crear una carpeta dentro de `features` con un nombre claro.
2. Guardar sus pantallas dentro de una subcarpeta `ui`.
3. Mantener sus datos y su lógica dentro de la misma función.
4. Mover a `core/model` únicamente los datos usados por dos o más funciones.
5. Registrar la pantalla nueva en `navigation/AuthCaresScreen.kt`.
6. Conectarla en `navigation/AuthCaresNavGraph.kt`.
7. Comprobar que la aplicación compile antes de subir el cambio.

## Reglas para mantener el orden

- `MainActivity` solo inicia Android y solicita permisos.
- Una función no debe guardar archivos dentro de la carpeta de otra función.
- Los componentes visuales compartidos van en `ui/components`.
- Los colores y estilos globales van en `ui/theme`.
- No colocar conexiones con Firebase directamente dentro de una pantalla.
- Los cambios deben ser pequeños, con mensajes de commit claros.

## Entrega a otro grupo

Antes de entregar una función:

- Indicar cuál carpeta de `features` le pertenece.
- Explicar qué rutas añadió o modificó.
- Confirmar qué datos compartidos utiliza.
- Entregar el proyecto compilando correctamente.
- Evitar incluir archivos personales, claves o configuraciones locales.
