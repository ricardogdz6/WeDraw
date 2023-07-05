package com.bupware.wedraw.android.logic.retrofit.repository

import android.util.Log
import com.bupware.wedraw.android.logic.models.Group
import com.bupware.wedraw.android.logic.models.User
import com.bupware.wedraw.android.logic.retrofit.api.RetrofitClient
import com.bupware.wedraw.android.logic.retrofit.services.GroupService
import com.bupware.wedraw.android.logic.retrofit.services.UserService
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.EOFException

object GroupRepository {
    private val groupService = RetrofitClient.getRetrofit().create(GroupService::class.java)

    suspend fun getAllGroups(): List<Group>? = suspendCancellableCoroutine { continuation ->
        groupService.getAllGroups().enqueue(object : Callback<List<Group>> {
            override fun onResponse(call: Call<List<Group>>, response: Response<List<Group>>) {
                if (response.isSuccessful) {
                    continuation.resume(response.body(),null)
                }
            }

            override fun onFailure(call: Call<List<Group>>, t: Throwable) {
                continuation.cancel()
            }
        })
    }

    suspend fun getGroupById(id:Int): List<Group>? = suspendCancellableCoroutine { continuation ->
        groupService.getGroupById(id).enqueue(object : Callback<List<Group>?> {
            override fun onResponse(call: Call<List<Group>?>, response: Response<List<Group>?>) {
                if (response.isSuccessful) {
                    continuation.resume(response.body(),null)
                }
            }

            override fun onFailure(call: Call<List<Group>?>, t: Throwable) {
                continuation.cancel()
            }
        })
    }

    suspend fun getGroupByUserId(userId:String): List<Group>? = suspendCancellableCoroutine { continuation ->
        groupService.getGroupByUserId(userId).enqueue(object : Callback<List<Group>?> {
            override fun onResponse(call: Call<List<Group>?>, response: Response<List<Group>?>) {
                if (response.isSuccessful) {
                    continuation.resume(response.body(),null)
                }
            }

            override fun onFailure(call: Call<List<Group>?>, t: Throwable) {
                continuation.cancel()
            }
        })
    }


    suspend fun createGroup(name:String,userId:String): Boolean = suspendCancellableCoroutine { continuation ->
        groupService.createGroup(name,userId).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful) {
                    continuation.resume(true,null)
                } else {
                    continuation.resume(false,null)
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                if (t is java.io.EOFException) {
                    continuation.resume(true,null)
                } else {
                    Log.i("error",t.toString())
                    continuation.resume(false,null)
                }
            }
        })
    }



    suspend fun insertUsertoUserGroup(userId:String,groupId:Int): Boolean = suspendCancellableCoroutine { continuation ->
        groupService.insertUsertoUserGroup(userId, groupId).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful) {
                    continuation.resume(true,null)
                } else {
                    continuation.resume(false,null)
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                if (t is java.io.EOFException) {
                    continuation.resume(true,null)
                } else {
                    Log.i("error",t.toString())
                    continuation.resume(false,null)
                }
            }
        })
    }

    suspend fun updateGroup(id:Int,group: Group):Boolean = suspendCancellableCoroutine { continuation ->

        groupService.updateGroup(id,group).enqueue(object:Callback<Boolean>{
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