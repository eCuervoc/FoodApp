package com.example.foodapp.data.repository

import com.example.foodapp.crash.CrashLogger
import com.example.foodapp.data.local.ProductDao
import com.example.foodapp.data.local.toEntity
import com.example.foodapp.data.remote.FirestoreDataSource
import com.example.foodapp.model.Product

class ProductRepository(
    private val productDao: ProductDao,
    private val remote: FirestoreDataSource,
    private val crashLogger: CrashLogger
) {
    suspend fun getProducts(): Result<List<Product>> {
        val localProducts = productDao.getProducts().map { it.toDomain() }
        return try {
            val remoteProducts = remote.getProducts()
            if (remoteProducts.isNotEmpty()) {
                productDao.saveProducts(remoteProducts.map { it.toEntity() })
                Result.success(remoteProducts)
            } else {
                val samples = defaultProducts()
                productDao.saveProducts(samples.map { it.toEntity() })
                Result.success(samples)
            }
        } catch (e: Exception) {
            crashLogger.logNonFatalError("ProductRepository.getProducts", e)
            when {
                localProducts.isNotEmpty() -> Result.success(localProducts)
                else -> {
                    val samples = defaultProducts()
                    productDao.saveProducts(samples.map { it.toEntity() })
                    Result.success(samples)
                }
            }
        }
    }

    suspend fun getProductById(id: String): Product? = productDao.getProductById(id)?.toDomain()

    private fun defaultProducts(): List<Product> = listOf(
        Product("burger_classic", "Hamburguesa clásica", "Pan brioche, carne artesanal, queso, lechuga fresca, tomate y salsa de la casa.", 16000.0, "hamburguesas", "burger_classic", "Popular"),
        Product("burger_double", "Hamburguesa doble", "Doble carne, doble queso cheddar, tocineta crocante, cebolla caramelizada y papas pequeñas.", 24000.0, "hamburguesas", "burger_double", "Especial"),
        Product("burger_bbq", "Hamburguesa BBQ", "Carne a la parrilla, queso cheddar, tocineta, aros de cebolla y salsa BBQ ahumada.", 22000.0, "hamburguesas", "burger_bbq", "Nuevo"),
        Product("pizza_pepperoni", "Pizza pepperoni", "Pizza personal con queso mozzarella, salsa napolitana y rodajas de pepperoni.", 21000.0, "pizzas", "pizza_pepperoni", "Popular"),
        Product("pizza_hawaiana", "Pizza hawaiana", "Pizza personal con piña dulce, jamón, salsa napolitana y queso mozzarella.", 20000.0, "pizzas", "pizza_hawaiana", "Clásica"),
        Product("pizza_mixta", "Pizza mixta", "Pollo, champiñones, maíz tierno, pimentón, queso mozzarella y salsa de la casa.", 23000.0, "pizzas", "pizza_mixta", "Recomendada"),
        Product("salchipapa_especial", "Salchipapa especial", "Papa francesa, salchicha, pollo desmechado, queso rallado, papas ripio y salsas.", 18000.0, "salchipapas", "salchipapa_especial", "Popular"),
        Product("salchipapa_super", "Salchipapa súper", "Papa francesa, salchicha americana, carne, pollo, queso gratinado, maíz y papas ripio.", 26000.0, "salchipapas", "salchipapa_super", "Mega"),
        Product("perro_caliente", "Perro caliente", "Pan suave, salchicha americana, queso, papitas, cebolla, piña y salsas tradicionales.", 12000.0, "perros", "perro_caliente", "Económico"),
        Product("mazorcada_mixta", "Mazorcada mixta", "Maíz tierno, carne, pollo, queso gratinado, papas ripio y salsa especial.", 23000.0, "mazorcadas", "mazorcada_mixta", "Especial"),
        Product("combo_pareja", "Combo pareja", "Dos hamburguesas clásicas, dos porciones de papas y dos bebidas personales.", 39000.0, "combos", "combo_pareja", "Combo"),
        Product("combo_familiar", "Combo familiar", "Dos hamburguesas, una pizza personal, dos papas grandes y cuatro bebidas.", 52000.0, "combos", "combo_familiar", "Familiar"),
        Product("gaseosa", "Gaseosa personal", "Bebida fría personal para acompañar hamburguesas, pizzas, combos o salchipapas.", 5000.0, "bebidas", "gaseosa", "Bebida")
    )
}
