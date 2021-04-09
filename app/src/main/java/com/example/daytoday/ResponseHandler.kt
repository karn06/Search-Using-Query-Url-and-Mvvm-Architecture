package com.example.daytoday

interface ResponseHandler<T> {
    fun response(response: T)
}