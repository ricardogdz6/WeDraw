package com.bupware.wedraw.android.logic.retrofit.api

import android.util.Log
import com.google.gson.*
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.lang.reflect.Type
import java.sql.Time
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object RetrofitClient {

    //TODO QUITAR ESTA LINEA DEL MANIFEST android:usesCleartextTraffic="true"

    //private const val BASE_URL = "http://ec2-15-188-118-107.eu-west-3.compute.amazonaws.com:8080/"
    private const val BASE_URL = "http://192.168.1.126:8080/"

    var gson = GsonBuilder()
        .setLenient()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        .registerTypeAdapter(Time::class.java, TimeDeserializer())
        .registerTypeAdapter(Time::class.java, TimeSerializer())
        .registerTypeAdapter(ByteArray::class.java, ByteArrayDeserializer())
        .registerTypeAdapter(TimeZone::class.java, TimeZoneAdapter())
        .create()

    private val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val client = OkHttpClient.Builder().addInterceptor(logging).build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    private val retrofitWorldTime: Retrofit = Retrofit.Builder()
        .baseUrl("http://worldtimeapi.org")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    fun getRetrofit(): Retrofit = retrofit
    fun getRetrofitWorldTime() = retrofitWorldTime

}

class TimeDeserializer : JsonDeserializer<Time> {
    private val formatter = SimpleDateFormat("HH:mm:ss")

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Time {
        val date = formatter.parse(json?.asString)
        return Time(date.time)
    }
}

class TimeSerializer : JsonSerializer<Time> {
    private val formatter = SimpleDateFormat("HH:mm:ss")

    override fun serialize(
        src: Time?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(formatter.format(src))
    }
}

class TimeZoneAdapter : JsonSerializer<TimeZone>, JsonDeserializer<TimeZone> {
    override fun serialize(src: TimeZone?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src?.id)
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): TimeZone {
        return TimeZone.getTimeZone(json?.asString)
    }
}

class ByteArrayDeserializer : JsonDeserializer<ByteArray> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ByteArray {

        // Verificar si el JsonElement es nulo o no contiene datos
        if (json == null || json.isJsonNull || !json.isJsonPrimitive) {
            return ByteArray(0)
        }

        // Obtener el contenido de la cadena del JsonElement
        val base64String = json.asString

        // Decodificar la cadena Base64 a un ByteArray
        return android.util.Base64.decode(base64String, android.util.Base64.DEFAULT)
    }
}