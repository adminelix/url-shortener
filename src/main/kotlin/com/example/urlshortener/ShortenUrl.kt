package com.example.urlshortener

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.net.URL
import java.util.*

@Entity
data class ShortenUrl(
    @Column
    val url: URL?,
    @Id
    @GeneratedValue
    var id: UUID? = null
) {
    constructor() : this(null)
}
