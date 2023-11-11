package com.example.urlshortener

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class ShortenUrlService(val repository: ShortenUrlRepository) {

    @Transactional
    fun create(shortenUrl: ShortenUrl): ShortenUrl {
        return repository.save(shortenUrl)
    }

    @Throws(NotFoundException::class)
    fun find(uuid: UUID): ShortenUrl {
        return repository.findById(uuid).orElseThrow { NotFoundException("no entry found for given UUID") }
    }
}