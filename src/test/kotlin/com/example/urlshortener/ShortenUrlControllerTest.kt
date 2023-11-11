package com.example.urlshortener

import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.notNullValue
import org.junit.ClassRule
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.containers.PostgreSQLContainer
import java.net.URL


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = [ShortenUrlControllerTest.Initializer::class])
class ShortenUrlControllerTest {

    @Autowired
    lateinit var repository: ShortenUrlRepository
    @LocalServerPort
    var port = 0

    companion object {
        @ClassRule
        var postgreSQLContainer = PostgreSQLContainer("postgres:11.1")
            .withDatabaseName("integration-tests-db")
            .withUsername("sa")
            .withPassword("sa")

    }


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
        val created = repository.save(ShortenUrl(URL("https://foo.bar")))
        RestAssured.get("{id}", "${created.id}")
            .then().assertThat()
            .statusCode(200)
            .body("url", equalTo(created.url.toString()))
            .body("id", equalTo(created.id.toString()))
    }

    @Test
    fun `GIVEN valid request WHEN get shortenUrl THEN redirect to original url`() {
        val created = repository.save(ShortenUrl(URL("https://foo.bar")))
        RestAssured.given().redirects().follow(false).param("redirect", true)
            .get("{id}", "${created.id}")
            .then().assertThat()
            .statusCode(303)
            .header("Location", equalTo(created.url.toString()))
    }


    internal class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
        override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
            postgreSQLContainer.start()
            TestPropertyValues.of(
                "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                "spring.datasource.password=" + postgreSQLContainer.getPassword()
            ).applyTo(configurableApplicationContext.environment)
        }
    }

}