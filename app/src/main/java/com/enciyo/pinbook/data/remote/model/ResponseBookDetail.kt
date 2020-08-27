package com.enciyo.pinbook.data.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@kotlinx.serialization.Serializable
data class ResponseBookDetail(
    @SerialName("bookAuthor")
    val mBookAuthor: String? = null, // Jose Saramago
    @SerialName("bookCategory")
    val mBookCategory: ResponseCategories,
    @SerialName("bookComments")
    val mBookComments: List<BookComment>? = null,
    @SerialName("bookName")
    val mBookName: String? = null, // Körlük
    @SerialName("bookPageOfCount")
    val mBookPageOfCount: String? = null, // 336
    @SerialName("bookPublisher")
    val mBookPublisher: BookPublisher? = null,
    @SerialName("bookRating")
    val mBookRating: Double? = null, // 1.0
    @SerialName("booksImage")
    val mBooksImage: String? = null, // https://i.idefix.com/cache/500x400-0/originals/0001704045001-1.jpg
    @SerialName("id")
    val mId: Int? = null, // 2
    @SerialName("information")
    val mInformation: String? = null // Distopik eserlere ilgi duyanların elinden düşürmediği Körlük, yayınlandığı günden bu yana adından söz ettirmeye devam ediyor. Portekiz’li yazar José Saramago’ya 1998’de Nobel Edebiyat Ödülü’nü kazandıran eser, konusuyla olduğu kadar zekice kurgulanmış karakterleriyle de dikkat çekiyor. Dönemin liberal demokrasi anlayışına bir eleştiri mahiyetinde kaleme alınan roman, insanların gittikçe bencilleşip olaylar karşısında duyarsızlaşmasını bir körlük metaforu etrafında işliyor.
) {

    @kotlinx.serialization.Serializable
    data class BookComment(
        @SerialName("comment")
        val mComment: String? = null, // asdasdas
        @SerialName("commentAuthor")
        val mCommentAuthor: CommentAuthor? = null,
        @SerialName("id")
        val mId: Int? = null // 1
    ) {
        @kotlinx.serialization.Serializable
        data class CommentAuthor(
            @SerialName("displayName")
            val mDisplayName: String? = null, // Mustafa Kılıç
            @SerialName("email")
            val mEmail: String? = null, // m@hotmail.com
            @SerialName("id")
            val mId: Int? = null, // 1
            @SerialName("password")
            val mPassword: String? = null, // 1234567
            @SerialName("username")
            val mUsername: String? = null // enciyo
        )
    }

    @Serializable
    data class BookPublisher(
        @SerialName("id")
        val mId: Int? = null, // 2
        @SerialName("publisherName")
        val mPublisherName: String? = null // Kırmızı Kedi
    )
}