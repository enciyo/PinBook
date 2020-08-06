package com.enciyo.pinbook.domain.model


data class Book(
    val id:String,
    val name: String,
    val category: Category,
    val author: String,
    val pageOfCount: String,
    val rating: String,
    val reviewsCount: String,
    val imageUrl:String
)