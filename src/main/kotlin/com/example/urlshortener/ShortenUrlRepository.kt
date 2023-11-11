package com.example.urlshortener

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ShortenUrlRepository: JpaRepository<ShortenUrl, UUID> {
}