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
    val nregistro: String?,
    val nombre: String?,
    val labtitular: String?,
    val viasAdministracion: List<ViaAdministracion>?,
    val formaFarmaceutica: FormaFarmaceutica?
)

data class FormaFarmaceutica(
    val codigo: Int?,
    val nombre: String?
)

data class ViaAdministracion(
    val codigo: Int,
    val nombre: String
)

data class RespuestaCima(
    val resultados: List<MedicamentoCIMA> = emptyList()
)

interface CimaApiService {

    // Buscar medicamentos (lista)
    @GET("medicamentos?")
    suspend fun buscarMedicamentos(
        @Query("nombre") nombre: String
    ): Response<RespuestaCima>
}

object CimaApi {
    private const val BASE_URL = "https://cima.aemps.es/cima/rest/"

    private val gson = GsonBuilder()
        .setLenient()
        .disableHtmlEscaping()
        .create()

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val http = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val service: CimaApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(http)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(CimaApiService::class.java)
    }
}
