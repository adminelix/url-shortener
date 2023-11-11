package com.example.urlshortener

import java.net.URL
import java.util.*

data class ShortenUrl(
    val id: UUID = UUID.randomUUID(),
    val url: URL
)
