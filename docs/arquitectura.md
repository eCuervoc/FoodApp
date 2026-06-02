# Arquitectura de FoodApp Final

## Patrón elegido
Se utiliza MVVM + Repository porque permite separar interfaz, estado, lógica de datos y servicios externos.

## Estructura principal
```text
app/src/main/java/com/example/foodapp/
├── auth/              # Login, registro y AuthRepository
├── crash/             # Registro controlado de errores
├── data/
│   ├── local/         # Room: DAOs, entidades y base de datos
│   ├── remote/        # FirestoreDataSource
│   └── repository/    # Repositorios que coordinan local/remoto
├── model/             # Entidades del dominio
├── ui/                # Activities y adapters
├── utils/             # Estados de UI y proveedores
└── viewmodel/         # ViewModels y factories
```

## Justificación del stack
| Capa / decisión | Tecnología elegida | Alternativa considerada | Justificación técnica | Riesgos o limitaciones |
|---|---|---|---|---|
| Plataforma móvil | Android Kotlin | Flutter | Continúa el avance inicial y permite aplicar Android nativo | Requiere configuración correcta de Gradle/Firebase |
| UI | XML + Material | Jetpack Compose | Es más cercano al avance inicial y fácil de sustentar | Más archivos de layout |
| Estado | ViewModel + StateFlow | LiveData | Mantiene el estado separado de la pantalla | Requiere entender corrutinas |
| Persistencia local | Room | SharedPreferences | Permite guardar datos estructurados como productos y pedidos | Requiere entidades y DAOs |
| Persistencia remota | Firestore | API REST propia | Se integra bien con Firebase Auth y datos por usuario | Requiere reglas de seguridad |
| Auth | Firebase Auth | Auth propia | Evita guardar contraseñas manualmente | Requiere proyecto Firebase |
| Fallos | Crashlytics | Logcat | Permite observar errores fuera del dispositivo | Requiere conexión y configuración Firebase |

## Flujo de datos
1. La pantalla solicita datos al ViewModel.
2. El ViewModel llama al Repository.
3. El Repository intenta consultar Firestore.
4. Si Firestore responde correctamente, guarda copia en Room.
5. Si ocurre error, registra error no fatal en Crashlytics.
6. Si hay datos locales, la app los muestra como respaldo.
7. La UI muestra estado de carga, éxito, vacío o error.

## Dónde vive el estado
El estado de productos vive en `HomeViewModel` mediante `StateFlow<UiState<List<Product>>>`.
El estado de pedidos vive en `OrdersViewModel` mediante `StateFlow<UiState<List<Order>>>`.

## Manejo de errores
Los errores de repositorio se capturan con `try/catch`. Los errores relevantes se envían a `CrashLogger`, que usa Firebase Crashlytics para registrar excepciones no fatales.
