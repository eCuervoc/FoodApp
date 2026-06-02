# D02 - Diagrama de navegación

```mermaid
flowchart TD
A[MainActivity] -->|Sin sesión| B[LoginActivity]
A -->|Con sesión| D[HomeActivity]
B --> C[RegisterActivity]
B --> D
C --> D
D --> E[DetailActivity]
E --> D
D --> F[OrdersActivity]
D --> G[ProfileActivity]
G -->|Cerrar sesión| B
D --> H[Estado: cargando/vacío/error]
```
