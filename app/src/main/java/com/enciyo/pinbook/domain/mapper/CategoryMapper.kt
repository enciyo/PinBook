package com.enciyo.pinbook.domain.mapper

import com.enciyo.pinbook.data.db.entity.BookCategoryEntity
import com.enciyo.pinbook.data.remote.model.ResponseCategories
import com.enciyo.pinbook.domain.model.Category


fun ResponseCategories?.toCategory() = Category(
    id = this?.mId.toString(),
    name = this?.mCategoryName.toString()
)

fun BookCategoryEntity?.toCategory() = Category(
    id = this?.categoryId.toString(),
    name = this?.categoryName.toString()
)

fun Category?.toCategoryEntity() = BookCategoryEntity(
    categoryId = this?.id.toString(),
    categoryName = this?.name.toString()
)

