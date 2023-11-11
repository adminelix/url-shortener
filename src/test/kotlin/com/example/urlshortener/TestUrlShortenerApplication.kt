package com.example.urlshortener

import org.springframework.boot.fromApplication
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.with

@TestConfiguration(proxyBeanMethods = false)
class TestUrlShortenerApplication

fun main(args: Array<String>) {
	fromApplication<UrlShortenerApplication>().with(TestUrlShortenerApplication::class).run(*args)
}
