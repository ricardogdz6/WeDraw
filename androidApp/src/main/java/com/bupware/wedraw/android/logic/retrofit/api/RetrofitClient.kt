package com.bupware.wedraw.android.logic.retrofit.api

import com.google.gson.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.sql.Time
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object RetrofitClient {

    //TODO QUITAR ESTA LINEA DEL MANIFEST android:usesCleartextTraffic="true"

    //private const val BASE_URL = "http://ec2-13-40-218-75.eu-west-2.compute.amazonaws.com:5000/"
    //private const val BASE_URL = "http://ec2-18-130-254-216.eu-west-2.compute.amazonaws.com:5000/"
    private const val BASE_URL = "http://192.168.1.126:8080/"

    var gson = GsonBuilder()
        .setLenient()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        .registerTypeAdapter(Time::class.java, TimeDeserializer())
        .registerTypeAdapter(Time::class.java, TimeSerializer())
        .create()

    //private val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    //private val client = OkHttpClient.Builder().addInterceptor(logging).build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        //.client(client)
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
