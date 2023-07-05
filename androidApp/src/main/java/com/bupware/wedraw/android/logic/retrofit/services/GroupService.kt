package com.bupware.wedraw.android.logic.retrofit.services

import com.bupware.wedraw.android.logic.models.Group
import com.bupware.wedraw.android.logic.models.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface GroupService {

    @GET("/weDraw/groups")
    fun getAllGroups(): Call<List<Group>>

    @GET("/weDraw/groups/userID/{userID}")
    fun getGroupByUserId(@Path("userID") userID: String): Call<List<Group>?>

    @GET("/weDraw/groups/id/{id}")
    fun getGroupById(@Path("id") id: Int): Call<List<Group>?>

    @POST("/weDraw/groups/{name}/{leaderId}")
    fun createGroup(@Path("name") name: String,@Path("leaderId") leaderId: String): Call<Boolean>

    @POST("/weDraw/groups/{userID}/{groupID}")
    fun insertUsertoUserGroup(@Path("userID") userID: String,@Body groupID: Int): Call<Boolean>

    @PUT("/weDraw/groups/{id}")
    fun updateGroup(@Path("id") id: Int,@Body group: Group): Call<Boolean>


}