package com.example.urlshortener

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.net.URL
import java.util.*

@RestController
class ShortenUrlController {

    @PostMapping
    fun create(@RequestBody request: ShortenUrl): ResponseEntity<ShortenUrl> {
        // TODO implement
        return ResponseEntity.created(URI("http://localhost/${request.id}")).body(request)

    }

    @GetMapping("/{uuid}")
    fun find(@PathVariable uuid: UUID, @RequestParam redirect: Boolean): ResponseEntity<ShortenUrl> {
        // TODO implement
        val response = ShortenUrl(uuid, URL("https://foo.bar"))

        if (redirect) {
            return ResponseEntity.status(303)
                .header("Location", response.url.toString())
                .build()
        } else {
            return ResponseEntity.ok(response)
        }

    }
}