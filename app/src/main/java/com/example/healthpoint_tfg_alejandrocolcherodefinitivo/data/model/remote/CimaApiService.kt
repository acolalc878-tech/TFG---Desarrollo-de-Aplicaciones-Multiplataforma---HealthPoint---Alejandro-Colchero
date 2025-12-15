package com.example.healthpoint_tfg_alejandrocolcherodefinitivo.data.model.remote

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class MedicamentoCIMA(
    // Número de registro del medicamento en CIMA. Se usa como identifador unico
    val nregistro: String?,
    // Nombre comercial del medicamento
    val nombre: String?,
    // Nombre del laboratorio titular del medicamento
    val labtitular: String?,
    // Lista de las vías disponible por donde se puede subministrar
    val viasAdministracion: List<ViaAdministracion>?,
    // Información detallada de la forma farmaceutica (comprimido, jarabe...)
    val formaFarmaceutica: FormaFarmaceutica?
)

data class FormaFarmaceutica(
    // Codigo interno de la forma farmaceutica
    val codigo: Int?,
    // Nombre descriptivo de la forma farmaceutica
    val nombre: String?
)

data class ViaAdministracion(
    // Codigo interno de la via de administracion
    val codigo: Int,
    // Nombre descriptivo de la via de administracion
    val nombre: String
)

data class RespuestaCima(
    // Respuesta de la API en forma de lista que almacena todos los medicamentos encontrados
    val resultados: List<MedicamentoCIMA> = emptyList()
)

interface CimaApiService {
    // Buscar medicamentos (lista)
    @GET("medicamentos?")
    suspend fun buscarMedicamentos(
        // El nombre del medicamento se añade como parametro de consulta
        @Query("nombre") nombre: String
    ): Response<RespuestaCima>
}

object CimaApi {
    // URL base del servicio REST de CIMA
    private const val BASE_URL = "https://cima.aemps.es/cima/rest/"

    // Gson para deserializar el JSON de forma más flexible
    private val gson = GsonBuilder()
        .setLenient() // Permite la flexibilidad en el formato JSON
        .disableHtmlEscaping()
        .create()

    // Interceptor de loggin para OkHttpClient. Útil para depuracion.
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Cliente Http principal con el interceptor añadido
    private val http = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    // Instancia de Retrofit inicializada con lazy
    // Solo se crea la primera vez que se accede a 'Cima.Api.service'
    val service: CimaApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL) // Establecemos la URL base
            .client(http) // Cliente OkHttp configurado
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(CimaApiService::class.java) // Creamos la implementación de la interfaz de servicio.
        }
    }
