package com.bupware.wedraw.android.logic.retrofit.repository

import android.util.Log
import com.bupware.wedraw.android.logic.models.Message
import com.bupware.wedraw.android.logic.retrofit.api.RetrofitClient
import com.bupware.wedraw.android.logic.retrofit.services.MessageService
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object MessageRepository {
    private val messageService = RetrofitClient.getRetrofit().create(MessageService::class.java)

    suspend fun getMessageById(id:Long): List<Message>? = suspendCancellableCoroutine { continuation ->
        messageService.getMessageById(id).enqueue(object : Callback<List<Message>?> {
            override fun onResponse(call: Call<List<Message>?>, response: Response<List<Message>?>) {
                if (response.isSuccessful) {
                    continuation.resume(response.body(),null)
                }
            }

            override fun onFailure(call: Call<List<Message>?>, t: Throwable) {
                continuation.cancel()
            }
        })
    }

    suspend fun getMessageByUserGroupId(id:Long): List<Message>? = suspendCancellableCoroutine { continuation ->
        messageService.getMessageByUserGroupId(id).enqueue(object : Callback<List<Message>?> {
            override fun onResponse(call: Call<List<Message>?>, response: Response<List<Message>?>) {
                if (response.isSuccessful) {
                    continuation.resume(response.body(),null)
                }
            }

            override fun onFailure(call: Call<List<Message>?>, t: Throwable) {
                continuation.cancel()
            }
        })
    }

    suspend fun getMessagesFromDate(messageId:Long,groupId:Long): List<Message>? = suspendCancellableCoroutine { continuation ->
        messageService.getMessagesFromDate(groupId,messageId).enqueue(object : Callback<List<Message>?> {
            override fun onResponse(call: Call<List<Message>?>, response: Response<List<Message>?>) {
                if (response.isSuccessful) {
                    continuation.resume(response.body(),null)
                }
            }

            override fun onFailure(call: Call<List<Message>?>, t: Throwable) {
                continuation.resume(null,null)
            }
        })
    }

    suspend fun createMessage(message: Message): Long? = suspendCancellableCoroutine { continuation ->
        messageService.createMessage(message).enqueue(object : Callback<Long> {
            override fun onResponse(call: Call<Long>, response: Response<Long>) {
                if (response.isSuccessful) {
                    continuation.resume(response.body(),null)
                }
            }

            override fun onFailure(call: Call<Long>, t: Throwable) {
                continuation.resume(null,null)
            }
        })
    }

    suspend fun updateMessageStatus(messageId:Long,userId: String):Boolean = suspendCancellableCoroutine { continuation ->

        messageService.updateMessageStatus(messageId,userId).enqueue(object:Callback<Boolean>{
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful){
                    continuation.resume(true,null)
                } else {
                    continuation.resume(false,null)
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                if (t is java.io.EOFException) {
                    continuation.resume(true,null)
                }
                else {
                    continuation.resume(false,null)
                }
            }

        })

    }



}