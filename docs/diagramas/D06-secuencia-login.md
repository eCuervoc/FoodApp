# D06.1 - Secuencia login

```mermaid
sequenceDiagram
participant U as Usuario
participant L as LoginActivity
participant A as AuthRepository
participant F as FirebaseAuth

U->>L: Ingresa correo y contraseña
L->>A: login(email, password)
A->>F: signInWithEmailAndPassword
F-->>A: Usuario autenticado
A-->>L: Éxito
L-->>U: Abre HomeActivity
```
