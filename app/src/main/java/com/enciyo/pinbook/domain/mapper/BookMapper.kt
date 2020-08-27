package com.enciyo.pinbook.domain.mapper

import com.enciyo.pinbook.data.db.entity.BookEntity
import com.enciyo.pinbook.data.db.entity.BookFavoriteEntity
import com.enciyo.pinbook.data.db.entity.BookShowcaseEntity
import com.enciyo.pinbook.data.remote.model.ResponseBookDetail
import com.enciyo.pinbook.data.remote.model.ResponseBooks
import com.enciyo.pinbook.domain.model.*


fun ResponseBooks.toBook(): Book = Book(
    id = mId.toString(),
    name = mBookName.toString(),
    category = this.mBookCategory.toCategory(),
    author = mBookAuthor.toString(),
    pageOfCount = mBookPageOfCount.toString(),
    rating = mBookRating.toString(),
    reviewsCount = mBookComments?.size.toString(),
    imageUrl = mBooksImage
)

fun ResponseBooks.toShowcaseBook(): ShowcaseBooks = ShowcaseBooks(
    bookId = mId.toString(),
    name = mBookName.toString(),
    imageUrl = mBooksImage
)


fun ResponseBookDetail.toBookDetail() = BookDetail(
    author = mBookAuthor.toString(),
    category = mBookCategory.toCategory(),
    name = mBookName.toString(),
    rating = mBookRating.toString(),
    imageUrl = mBooksImage.toString(),
    mInformation = mInformation.toString(),
    comments = mBookComments?.toComment()?: listOf()
)

fun ResponseBookDetail.BookComment.toComment() = Comments(
    comment = this.mComment.toString(),
    commentDisplayName = this.mCommentAuthor?.mDisplayName.toString()
)

fun List<ResponseBookDetail.BookComment>.toComment() = this.map { it?.toComment() }


fun BookEntity.toBook(): Book = Book(
    id = bookId,
    name = bookName,
    category = this.bookCategory.toCategory(),
    author = bookAuthor,
    pageOfCount = bookPageOfCount,
    rating = bookRating,
    reviewsCount = bookReviewsCount,
    imageUrl = bookImage
)

fun Book.toBookEntity(): BookEntity = BookEntity(
    bookId = this.id.toString(),
    bookName = this.name,
    bookCategory = this.category.toCategoryEntity(),
    bookAuthor = this.author,
    bookPageOfCount = this.pageOfCount,
    bookRating = this.rating,
    bookReviewsCount = this.reviewsCount,
    bookImage = this.imageUrl
)


fun Book.toPopularBook() = PopularBooks(
    id = this.id.toInt(),
    imageUrl = this.imageUrl,
    reviewCount = this.reviewsCount,
    author = this.author,
    rating = this.rating,
    name = this.name
)

fun Book.toShowcaseBooks() = ShowcaseBooks(
    bookId = id.toString(),
    imageUrl = this.imageUrl,
    name = this.name
)

fun BookShowcaseEntity.toShowcaseBooks() = ShowcaseBooks(
    bookId = bookId.toString(),
    imageUrl = this.imageUrl,
    name = this.bookName
)

fun ShowcaseBooks.toShowcaseBooks() = BookShowcaseEntity(
    bookId = bookId.toString(),
    imageUrl = this.imageUrl,
    bookName = this.name
)

fun BookShowcaseEntity.toShowCaseBook() = BookShowcaseEntity(
    bookId, imageUrl, bookName
)

fun BookFavoriteEntity.toBook() = PopularBooks(
    id = bookId.toInt(),
    name = bookName,
    author = bookAuthor,
    rating = bookRating,
    imageUrl = bookImage,
    reviewCount = bookReviewsCount
)


fun PopularBooks.toFavoriteBook() = BookFavoriteEntity(
    bookId = this.id.toString(),
    bookName = name,
    bookAuthor = author,
    bookPageOfCount = "",
    bookRating = rating,
    bookReviewsCount = reviewCount,
    bookImage = imageUrl
)


fun List<BookEntity>.toBook(): List<Book> =
    map { it.toBook() }

fun List<BookShowcaseEntity>.toShowcaseBooks(): List<ShowcaseBooks> =
    map { it.toShowcaseBooks() }

fun List<BookFavoriteEntity>.toFavoriteBook(): List<PopularBooks> =
    map { it.toBook() }

fun List<Book>.toPopularBooks(): List<PopularBooks> =
    map { it.toPopularBook() }
