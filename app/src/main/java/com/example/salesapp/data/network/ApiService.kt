package com.example.salesapp.data.network

interface ApiService<T> {
    suspend fun getAll(): List<T>
    suspend fun get(identifier: String): T
    suspend fun add(t: T): Boolean
    suspend fun update(t: T): Boolean
    suspend fun delete(identifier: String): Boolean
}