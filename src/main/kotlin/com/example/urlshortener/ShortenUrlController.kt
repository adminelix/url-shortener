package com.example.urlshortener

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.util.*

@RestController
class ShortenUrlController(val service: ShortenUrlService) {

    @PostMapping
    fun create(@RequestBody request: ShortenUrl): ResponseEntity<ShortenUrl> {
        val created = service.create(request)
        return ResponseEntity.created(URI("http://localhost/${created.id}")).body(created)

    }

    @GetMapping("/{uuid}")
    fun find(@PathVariable uuid: UUID, @RequestParam(required = false) redirect: Boolean): ResponseEntity<ShortenUrl> {

        val response = service.find(uuid)

        return if (redirect) {
            ResponseEntity.status(303)
                .header("Location", response.url.toString())
                .build()
        } else {
            ResponseEntity.ok(response)
        }

    }
}