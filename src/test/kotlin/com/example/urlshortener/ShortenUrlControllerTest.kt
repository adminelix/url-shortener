package com.example.urlshortener

import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import java.net.URL
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShortenUrlControllerTest {
    @LocalServerPort
    var port = 0

    @BeforeEach
    fun prepare() {
        RestAssured.port = port
    }
    @Test
    fun `GIVEN valid request WHEN create shortenUrl THEN return created shortenUrlObject`() {
        val requestBody = ShortenUrl(url = URL("https://google.de"))
        RestAssured.given().contentType(ContentType.JSON).body(requestBody).post()
            .then().assertThat()
            .statusCode(201)
            .body("url", equalTo(requestBody.url.toString()))
            .body("id", notNullValue())
    }

    @Test
    fun `GIVEN valid request WHEN get shortenUrl THEN return shortenUrlObject`() {
        val id = UUID.randomUUID()
        RestAssured.get("{id}","$id")
            .then().assertThat()
            .statusCode(200)
            .body("url", equalTo("https://foo.bar"))
            .body("id", equalTo(id.toString()))
    }

    @Test
    fun `GIVEN valid request WHEN get shortenUrl THEN redirect to original url`() {
        val id = UUID.randomUUID()
        RestAssured.given().redirects().follow(false).param("redirect", true)
            .get("{id}","$id")
            .then().assertThat()
            .statusCode(303)
            .header("Location", equalTo("https://foo.bar"))
    }
}