# SRS - FoodApp Final

## 1. Introducción
### Propósito
Documentar los requerimientos, arquitectura y funcionamiento de FoodApp Final.

### Alcance
Aplicación móvil Android para consultar productos de comida, crear pedidos y revisar historial de pedidos mediante autenticación, persistencia local y persistencia remota.

### Público objetivo del documento
Docente, jurado evaluador, estudiantes desarrolladores y futuros mantenedores.

## 2. Descripción general
### Contexto del problema
Los pedidos de comida en pequeños negocios suelen recibirse por canales informales, lo que dificulta organizarlos y hacer seguimiento.

### Usuario objetivo
Clientes que desean realizar pedidos desde el celular y pequeños negocios que necesitan registrar solicitudes de forma organizada.

### Solución móvil
La app permite registrarse, iniciar sesión, consultar productos, crear pedidos y consultar el historial personal de pedidos.

### Plataforma elegida
Android nativo con Kotlin.

## 3. Requerimientos funcionales
| ID | Requerimiento funcional | Prioridad | Criterio de aceptación |
|---|---|---|---|
| RF-01 | La app debe permitir registrar usuario | Alta | Dado un correo y contraseña válidos, se crea una cuenta en Firebase Auth |
| RF-02 | La app debe permitir iniciar sesión | Alta | Dado un usuario válido, accede al Home |
| RF-03 | La app debe permitir cerrar sesión | Alta | Al cerrar sesión vuelve al Login |
| RF-04 | La app debe listar productos | Alta | Al abrir Home se muestran productos remotos o locales |
| RF-05 | La app debe mostrar detalle de producto | Alta | Al tocar un producto se abre su detalle |
| RF-06 | La app debe crear un pedido | Alta | Al confirmar cantidad se guarda el pedido local y remotamente |
| RF-07 | La app debe consultar pedidos del usuario | Alta | En Mis pedidos se muestran los pedidos asociados al usuario |
| RF-08 | La app debe persistir datos localmente | Alta | Al cerrar y reabrir conserva productos/pedidos locales |
| RF-09 | La app debe usar persistencia remota | Alta | Los pedidos se guardan en Firestore bajo el UID del usuario |
| RF-10 | La app debe manejar errores | Alta | Si falla la nube muestra error o datos locales sin caerse |
| RF-11 | La app debe registrar errores no fatales | Media | Los errores capturados se envían a Crashlytics |
| RF-12 | La app debe manejar sesión no autenticada | Alta | Si no hay usuario, bloquea acciones protegidas |

## 4. Requerimientos no funcionales
| Categoría | Requerimiento | Criterio verificable |
|---|---|---|
| Usabilidad | Interfaz clara para usuario no técnico | El usuario completa login y pedido sin explicación |
| Rendimiento | Operaciones largas muestran carga | Home muestra ProgressBar al consultar productos |
| Persistencia | Datos locales sobreviven al cierre | Room conserva productos y pedidos |
| Disponibilidad parcial | Manejo de error o sin internet | Si falla Firestore, se intenta mostrar datos locales |
| Seguridad | No se guardan contraseñas en texto plano | La autenticación se delega a Firebase Auth |
| Mantenibilidad | Código modularizado | Hay separación UI, ViewModel, Repository, DataSource y Room |
| Observabilidad | Registro de fallos relevantes | Crashlytics registra errores no fatales |
| Accesibilidad | Interfaz legible | Botones y textos tienen tamaño adecuado |

## 5. Reglas de negocio
- Un usuario solo puede consultar sus propios pedidos.
- No se puede crear pedido sin iniciar sesión.
- La cantidad mínima de un producto es 1.
- El total del pedido se calcula con precio por cantidad.
- No se deben registrar contraseñas ni datos sensibles en Crashlytics.

## 6. Modelo de datos
### Product
- id: String
- name: String
- description: String
- price: Double
- categoryId: String
- imageName: String

### Order
- id: String
- userId: String
- productId: String
- productName: String
- quantity: Int
- total: Double
- status: String
- createdAt: Long

### Category
- id: String
- name: String

## 7. Interfaces externas
- Firebase Authentication.
- Firebase Firestore.
- Firebase Crashlytics.
- Permiso de internet.

## 8. Criterios de aceptación del proyecto
- La app ejecuta en emulador o dispositivo.
- Login y registro funcionan.
- Cierre de sesión funciona.
- Productos se muestran en listado.
- Pedido se crea desde detalle.
- Pedido se guarda localmente y remotamente.
- Se registran errores no fatales en Crashlytics.
- Arquitectura se refleja en carpetas del proyecto.

## 9. Matriz de trazabilidad
| RF | Pantalla | Capa/clase | Persistencia/API | Cómo se demuestra |
|---|---|---|---|---|
| RF-01 | RegisterActivity | AuthRepository | Firebase Auth | Registro de usuario |
| RF-02 | LoginActivity | AuthRepository | Firebase Auth | Login exitoso |
| RF-03 | ProfileActivity | AuthRepository | Firebase Auth | Cierre de sesión |
| RF-04 | HomeActivity | HomeViewModel/ProductRepository | Room + Firestore | Listado de productos |
| RF-05 | DetailActivity | ProductDao | Room | Detalle de producto |
| RF-06 | DetailActivity | OrderRepository | Room + Firestore | Crear pedido |
| RF-07 | OrdersActivity | OrdersViewModel/OrderRepository | Room + Firestore | Historial |
| RF-11 | Repositorios | CrashLogger | Crashlytics | Error no fatal |

## Actualización funcional: módulo de compra y carrito

Se agregó un apartado de compra para que el usuario autenticado pueda seleccionar productos ficticios de comida rápida, agregarlos a un carrito local y confirmar el pedido. El catálogo incluye hamburguesas, pizzas, salchipapas, perro caliente, mazorcada y combo familiar. El carrito permite aumentar cantidades, disminuir cantidades, quitar productos y calcular el total antes de crear el pedido.

### Requerimientos funcionales agregados

| ID | Requerimiento funcional | Prioridad | Criterio de aceptación |
|---|---|---|---|
| RF-13 | La app debe mostrar productos ficticios de comida rápida | Alta | Al abrir el catálogo se visualizan hamburguesas, pizzas, salchipapas y otros productos. |
| RF-14 | La app debe permitir agregar productos al carrito | Alta | Desde el detalle del producto, el usuario selecciona cantidad y agrega el producto al carrito. |
| RF-15 | La app debe permitir editar el carrito | Alta | El usuario puede aumentar, disminuir o quitar productos antes de confirmar. |
| RF-16 | La app debe calcular el total del pedido | Alta | El carrito muestra el total actualizado según cantidades y precios. |
| RF-17 | La app debe confirmar el pedido desde el carrito | Alta | Al tocar “Hacer pedido”, los productos del carrito se registran como pedidos del usuario. |

### Nueva entidad local

| Entidad | Descripción | Persistencia |
|---|---|---|
| CartItem | Producto seleccionado temporalmente por el usuario antes de hacer el pedido | Room, tabla `cart_items` |

## Actualización funcional: carrito, fragments y método de pago

La aplicación incorpora navegación interna mediante fragments dentro de la pantalla principal. El usuario autenticado puede navegar entre Productos, Carrito, Mis pedidos y Perfil sin salir del flujo principal.

También se agregó selección de método de pago en el carrito. Antes de confirmar el pedido, el usuario puede elegir entre:

- Efectivo
- Tarjeta

El método seleccionado se almacena junto con el pedido en la entidad `Order`, en la base de datos local Room y en Firestore.

## Actualización funcional: módulo de compras

La aplicación incorpora un módulo de compras para que el usuario autenticado pueda consultar comidas rápidas, filtrar por categoría, agregar productos al carrito local, seleccionar método de pago, registrar dirección de entrega y confirmar el pedido. Al confirmar, se crea un pedido en Firestore asociado al usuario autenticado y se conserva evidencia local en Room.

### Nuevos requerimientos funcionales

| ID | Requerimiento funcional | Prioridad | Criterio de aceptación |
|---|---|---|---|
| RF-13 | La app debe filtrar productos por categoría | Media | Al seleccionar una categoría, el listado muestra solo productos relacionados |
| RF-14 | La app debe permitir agregar productos al carrito | Alta | Al seleccionar cantidad y agregar, el producto aparece en el carrito |
| RF-15 | La app debe permitir seleccionar método de pago | Alta | El pedido se guarda con método efectivo o tarjeta |
| RF-16 | La app debe solicitar dirección de entrega | Alta | No se permite confirmar pedido sin dirección |
| RF-17 | La app debe permitir nota adicional | Media | La nota se guarda junto con el pedido si el usuario la escribe |
| RF-18 | La app debe mostrar confirmación antes del pedido | Media | Antes de guardar, se muestra total, método de pago y dirección |
| RF-19 | La app debe consultar historial de pedidos | Alta | El usuario ve sus pedidos con fecha, estado, total, dirección y método de pago |

### Estado del pedido

Los pedidos se crean con estado inicial **Pendiente**. Este estado permite explicar una evolución futura en la cual el negocio pueda actualizar el pedido a **En preparación**, **Entregado** o **Cancelado**.

## Actualización de interfaz y experiencia móvil

La versión mejorada incorpora un menú lateral desplegable, menú inferior con mini íconos, botón de retorno al inicio, transiciones entre secciones, imágenes diferenciadas por producto y descripciones ampliadas. Estas mejoras fortalecen la usabilidad móvil, la navegación y la presentación visual del catálogo.
