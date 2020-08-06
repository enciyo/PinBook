package com.enciyo.pinbook.domain.mapper

import com.enciyo.pinbook.data.db.entity.BookEntity
import com.enciyo.pinbook.data.db.entity.BookShowcaseEntity
import com.enciyo.pinbook.data.remote.model.ResponseBooks
import com.enciyo.pinbook.domain.model.Book
import com.enciyo.pinbook.domain.model.PopularBooks
import com.enciyo.pinbook.domain.model.ShowcaseBooks


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
        name = this.bookName)

fun ShowcaseBooks.toShowcaseBooks() = BookShowcaseEntity(
    bookId = bookId.toString(),
    imageUrl = this.imageUrl,
    bookName = this.name
)

fun BookShowcaseEntity.toShowCaseBook() = BookShowcaseEntity(
    bookId, imageUrl, bookName
)

