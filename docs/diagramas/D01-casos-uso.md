# D01 - Diagrama de casos de uso

```mermaid
usecaseDiagram
actor "Usuario no autenticado" as U0
actor "Usuario autenticado" as U1
U0 --> (Registrarse)
U0 --> (Iniciar sesión)
U1 --> (Ver productos)
U1 --> (Ver detalle de producto)
U1 --> (Crear pedido)
U1 --> (Consultar pedidos)
U1 --> (Cerrar sesión)
U1 --> (Recibir mensaje de error)
```
