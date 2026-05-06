package com.example.hitcapp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @POST("register")
    Call<UserResponse> register(@Body User user);

    @POST("login")
    Call<UserResponse> login(@Body User user);

    @PUT("user/update")
    Call<UserResponse> updateUser(@Body User user);

    @DELETE("user/delete/{id}")
    Call<UserResponse> deleteUser(@Path("id") int id);
}
