package com.example.flickerexample.network

enum class PhotoSortItem(@PhotoSort val sortText: String) {
    DATE_POSTED_ASC(PhotoSort.DATE_POSTED_ASC),
    DATE_POSTED_DESC(PhotoSort.DATE_POSTED_DESC),
    DATE_TAKEN_ASC(PhotoSort.DATE_TAKEN_ASC),
    DATE_TAKEN_DESC(PhotoSort.DATE_TAKEN_DESC),
    INTERESTING_DESC(PhotoSort.INTERESTING_DESC),
    INTERESTING_ASC(PhotoSort.INTERESTING_ASC),
    RELEVANCE(PhotoSort.RELEVANCE)
}