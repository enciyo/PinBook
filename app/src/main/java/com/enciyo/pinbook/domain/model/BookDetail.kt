package com.enciyo.pinbook.domain.model



data class BookDetail(
    val author:String,
    val category:Category,
    val name:String,
    val rating:String,
    val imageUrl:String,
    val mInformation: String?,
    val comments: List<Comments>
){
    companion object{
        fun empty() = BookDetail("", Category("",""),"","","","", listOf())
    }
}