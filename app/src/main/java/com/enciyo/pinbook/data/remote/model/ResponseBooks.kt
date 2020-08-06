package com.enciyo.pinbook.data.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ResponseBooks(
    @SerialName("bookAuthor")
    val mBookAuthor: String? = null, // Arthur Schopenhauer
    @SerialName("bookCategory")
    val mBookCategory: ResponseCategories? = null,
    @SerialName("bookComments")
    val mBookComments: List<BookComment?>? = null,
    @SerialName("bookName")
    val mBookName: String? = null, // Aşkın Metafiziği
    @SerialName("bookPageOfCount")
    val mBookPageOfCount: String? = null, // 80
    @SerialName("bookPublisher")
    val mBookPublisher: BookPublisher? = null,
    @SerialName("bookRating")
    val mBookRating: Double? = null, // 4.15
    @SerialName("booksImage")
    val mBooksImage:String,
    @SerialName("information")
    val mInformation:String,
    @SerialName("id")
    val mId: Int? = null // 1
) {

    @Serializable
    data class BookComment(
        @SerialName("comment")
        val mComment: String? = null, // asdasdas
        @SerialName("commentAuthor")
        val mCommentAuthor: CommentAuthor? = null,
        @SerialName("id")
        val mId: Int? = null // 1
    ) {
        @Serializable
        data class CommentAuthor(
            @SerialName("displayName")
            val mDisplayName: String? = null, // Mustafa Kılıç
            @SerialName("email")
            val mEmail: String? = null, // m@hotmail.com
            @SerialName("id")
            val mId: Int? = null, // 1
            @SerialName("password")
            val mPassword: String? = null, // 123456
            @SerialName("username")
            val mUsername: String? = null // enciyo
        )
    }

    @Serializable
    data class BookPublisher(
        @SerialName("id")
        val mId: Int? = null, // 1
        @SerialName("publisherName")
        val mPublisherName: String? = null // Ayrıntı Yayınları
    )
}