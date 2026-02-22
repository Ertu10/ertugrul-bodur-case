package com.insider.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

// Petstore Pet endpoint'leri icin CRUD ve negatif test senaryolari
public class PetStoreApiTest {

    private static final String BASE_URL = "https://petstore.swagger.io/v2";
    private long petId;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = BASE_URL;
        petId = System.currentTimeMillis();
    }

    // ==================== POSITIVE TESTS ====================

    @Test(priority = 1, description = "Create a new pet with valid data")
    public void testCreatePet() {
        Map<String, Object> category = new HashMap<>();
        category.put("id", 1);
        category.put("name", "Dogs");

        Map<String, Object> tag = new HashMap<>();
        tag.put("id", 1);
        tag.put("name", "friendly");

        Map<String, Object> pet = new HashMap<>();
        pet.put("id", petId);
        pet.put("category", category);
        pet.put("name", "TestDog");
        pet.put("photoUrls", new String[] { "https://example.com/photo.jpg" });
        pet.put("tags", new Map[] { tag });
        pet.put("status", "available");

        given()
                .contentType(ContentType.JSON)
                .body(pet)
                .when()
                .post("/pet")
                .then()
                .statusCode(200)
                .body("id", equalTo(petId))
                .body("name", equalTo("TestDog"))
                .body("status", equalTo("available"))
                .body("category.name", equalTo("Dogs"));

        System.out.println("CREATE: Pet olusturuldu, ID: " + petId);
    }

    @Test(priority = 2, dependsOnMethods = "testCreatePet", description = "Get pet by valid ID")
    public void testGetPetById() {
        given()
                .pathParam("petId", petId)
                .when()
                .get("/pet/{petId}")
                .then()
                .statusCode(200)
                .body("id", equalTo(petId))
                .body("name", equalTo("TestDog"))
                .body("status", equalTo("available"));

        System.out.println("READ: Pet basariyla getirildi, ID: " + petId);
    }

    @Test(priority = 3, dependsOnMethods = "testGetPetById", description = "Update existing pet with new name and status")
    public void testUpdatePet() {
        Map<String, Object> category = new HashMap<>();
        category.put("id", 1);
        category.put("name", "Dogs");

        Map<String, Object> pet = new HashMap<>();
        pet.put("id", petId);
        pet.put("category", category);
        pet.put("name", "UpdatedTestDog");
        pet.put("photoUrls", new String[] { "https://example.com/updated_photo.jpg" });
        pet.put("status", "sold");

        given()
                .contentType(ContentType.JSON)
                .body(pet)
                .when()
                .put("/pet")
                .then()
                .statusCode(200)
                .body("id", equalTo(petId))
                .body("name", equalTo("UpdatedTestDog"))
                .body("status", equalTo("sold"));

        System.out.println("UPDATE: Pet guncellendi, ID: " + petId);
    }

    @Test(priority = 4, dependsOnMethods = "testUpdatePet", description = "Verify pet was updated correctly")
    public void testGetUpdatedPet() {
        given()
                .pathParam("petId", petId)
                .when()
                .get("/pet/{petId}")
                .then()
                .statusCode(200)
                .body("name", equalTo("UpdatedTestDog"))
                .body("status", equalTo("sold"));

        System.out.println("READ (update sonrasi): Guncelleme dogrulandi");
    }

    @Test(priority = 5, description = "Find pets by status 'available'")
    public void testFindPetsByStatus() {
        Response response = given()
                .queryParam("status", "available")
                .when()
                .get("/pet/findByStatus")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .extract().response();

        System.out.println("FIND BY STATUS: " + response.jsonPath().getList("$").size() + " available pet bulundu");
    }

    @Test(priority = 6, dependsOnMethods = "testGetUpdatedPet", description = "Delete pet by valid ID")
    public void testDeletePet() {
        given()
                .pathParam("petId", petId)
                .when()
                .delete("/pet/{petId}")
                .then()
                .statusCode(200);

        System.out.println("DELETE: Pet silindi, ID: " + petId);
    }

    @Test(priority = 7, dependsOnMethods = "testDeletePet", description = "Verify deleted pet returns 404")
    public void testGetDeletedPetReturns404() {
        given()
                .pathParam("petId", petId)
                .when()
                .get("/pet/{petId}")
                .then()
                .statusCode(404);

        System.out.println("VERIFY DELETE: Silinen pet 404 dondu");
    }

    // ==================== NEGATIVE TESTS ====================

    @Test(priority = 8, description = "Get pet with non-existing ID returns 404 or 200")
    public void testGetPetWithInvalidId() {
        Response response = given()
                .pathParam("petId", 999999999L)
                .when()
                .get("/pet/{petId}")
                .then()
                .statusCode(anyOf(is(200), is(404)))
                .extract().response();

        int statusCode = response.getStatusCode();
        if (statusCode == 404) {
            System.out.println("NEGATIVE: Gecersiz pet ID 404 dondu");
        } else {
            // Petstore paylasimli demo API - baska kullanicilar bu ID ile pet olusturmus
            // olabilir
            System.out.println("NEGATIVE: Gecersiz pet ID " + statusCode + " dondu (paylasimli demo API)");
        }
    }

    @Test(priority = 9, description = "Get pet with string ID returns error")
    public void testGetPetWithStringId() {
        Response response = given()
                .when()
                .get("/pet/invalidStringId")
                .then()
                .statusCode(anyOf(is(400), is(404)))
                .extract().response();

        System.out.println("NEGATIVE: String ID status: " + response.getStatusCode());
    }

    @Test(priority = 10, description = "Create pet without required 'name' field")
    public void testCreatePetWithoutRequiredField() {
        Map<String, Object> pet = new HashMap<>();
        pet.put("id", petId + 100);
        pet.put("photoUrls", new String[] { "https://example.com/photo.jpg" });
        pet.put("status", "available");

        Response response = given()
                .contentType(ContentType.JSON)
                .body(pet)
                .when()
                .post("/pet")
                .then()
                .extract().response();

        System.out.println("NEGATIVE: name olmadan olusturma - Status: " + response.getStatusCode());
    }

    @Test(priority = 11, description = "Find pets with invalid status value")
    public void testFindPetsByInvalidStatus() {
        Response response = given()
                .queryParam("status", "nonexistent_status")
                .when()
                .get("/pet/findByStatus")
                .then()
                .statusCode(200)
                .extract().response();

        int size = response.jsonPath().getList("$").size();
        Assert.assertEquals(size, 0,
                "Invalid status should return empty list, but got " + size + " results");

        System.out.println("NEGATIVE: Gecersiz status bos liste dondu");
    }

    @Test(priority = 12, description = "Delete a non-existing pet returns 404")
    public void testDeleteNonExistingPet() {
        given()
                .pathParam("petId", 999999998L)
                .when()
                .delete("/pet/{petId}")
                .then()
                .statusCode(404);

        System.out.println("NEGATIVE: Var olmayan pet silme 404 dondu");
    }
}
