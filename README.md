# FoodApp Final

## Descripción
FoodApp Final es una aplicación móvil Android para la gestión básica de pedidos de comida. Conserva la idea inicial del primer avance, que era un catálogo de productos, pero la amplía hacia una solución móvil completa con autenticación, persistencia local, persistencia remota, manejo de estados, arquitectura MVVM y registro de errores en Firebase Crashlytics.

## Problema que resuelve
Pequeños negocios de comida pueden recibir pedidos por medios desorganizados como llamadas o mensajes. Esto puede generar errores, pérdida de información y falta de seguimiento. La app permite al usuario iniciar sesión, consultar productos, crear pedidos y revisar su historial.

## Stack tecnológico
- Android nativo con Kotlin.
- XML + Material Components.
- MVVM + Repository.
- Firebase Authentication para login y registro.
- Firebase Firestore para persistencia remota.
- Firebase Crashlytics para registro de errores no fatales.
- Room para persistencia local.
- StateFlow para estados de interfaz.

## Funcionalidades principales
- Registro de usuario.
- Inicio de sesión.
- Cierre de sesión.
- Listado dinámico de productos.
- Detalle de producto.
- Creación de pedidos.
- Consulta de historial de pedidos.
- Persistencia local con Room.
- Persistencia remota con Firestore.
- Manejo de estados: cargando, vacío, error y sesión no autenticada.
- Registro de errores no fatales en Crashlytics.

## Arquitectura
El proyecto usa MVVM + Repository. La UI se comunica con ViewModels, los ViewModels solicitan datos a los repositorios y los repositorios coordinan datos locales de Room, datos remotos de Firestore y registro de errores con Crashlytics.

Ver: `docs/arquitectura.md`

## Cómo ejecutar localmente
1. Abrir el proyecto en Android Studio.
2. Crear un proyecto en Firebase.
3. Registrar una app Android con el paquete `com.example.foodapp`.
4. Descargar el archivo `google-services.json` real.
5. Reemplazar el archivo de ejemplo ubicado en `app/google-services.json`.
6. Activar en Firebase:
   - Authentication con correo/contraseña.
   - Firestore Database.
   - Crashlytics.
7. Sincronizar Gradle.
8. Ejecutar en emulador o dispositivo físico.

## Datos iniciales en Firestore
Puedes crear una colección `products` con documentos como:

```json
{
  "name": "Pizza personal",
  "description": "Pizza con queso, salsa y vegetales.",
  "price": 18000,
  "categoryId": "pizzas",
  "imageName": "pizza"
}
```

Si Firestore no tiene productos, la app usa productos de ejemplo y los guarda localmente.

## Servicios externos
- Firebase Auth: autenticación y sesión.
- Firestore: productos y pedidos por usuario.
- Crashlytics: errores no fatales.

## Documentación
- `docs/SRS.md`
- `docs/arquitectura.md`
- `docs/uso-ia.md`
- `docs/diagramas/`

## Nota importante
El archivo `google-services.json` incluido es solo una plantilla para que el proyecto abra. Debe reemplazarse por el archivo real de Firebase antes de la demo.

### Mejoras agregadas

- Navegación con fragments en la pantalla principal: Productos, Carrito, Mis pedidos y Perfil.
- Carrito de compras con productos de comida rápida.
- Selección de método de pago: efectivo o tarjeta.
- El método de pago queda guardado en los pedidos locales y remotos.
- Formato de precios en pesos colombianos con separador de miles.

## Mejoras agregadas en la versión con compras completas

Esta versión incluye un flujo más completo de compra:

- Catálogo de comida rápida con productos imaginarios.
- Categorías filtrables: todos, hamburguesas, pizzas, salchipapas, combos y bebidas.
- Etiquetas visuales como Popular, Nuevo, Combo o Familiar.
- Carrito local con Room.
- Dirección de entrega obligatoria.
- Nota adicional para el pedido.
- Método de pago: efectivo o tarjeta.
- Confirmación antes de registrar el pedido.
- Pedido único por carrito con resumen de productos.
- Historial de pedidos con estado, fecha, dirección, nota, método de pago y total.
- Perfil mejorado con correo, sesión activa y cierre de sesión.

## Mejoras visuales y de navegación agregadas

- Menú lateral desplegable desde la esquina superior izquierda.
- Botón superior para volver al menú inicial.
- Menú inferior con mini íconos para Inicio, Carrito, Pedidos y Perfil.
- Transiciones suaves al cambiar entre fragments.
- Catálogo con categorías de comida rápida y mini íconos.
- Imágenes propias para cada producto del catálogo.
- Descripciones ampliadas para hamburguesas, pizzas, salchipapas, perros, mazorcadas, combos y bebidas.
- Animación suave al cargar tarjetas de productos y carrito.
