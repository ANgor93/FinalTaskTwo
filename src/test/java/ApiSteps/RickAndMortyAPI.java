package ApiSteps;

import io.cucumber.java.ru.Дано;
import io.cucumber.java.ru.Затем;
import io.cucumber.java.ru.Тогда;
import io.qameta.allure.Allure;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;


import static ApiSpecification.ApiSpecification.getRequestSpecificationRickAndMorty;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class RickAndMortyAPI {
    public static String characterId;
    public static String mortyLocation;
    public static String mortySpecies;
    public static String lastCharRace;
    public static String lastCharLoc;
    public static int lastEpisode;
    public static int lastCharacter;

    @Дано("информация о персонаже с ID {string}")
    public static void getCharacterInfo(String id) {
        Response gettingCharacter = given()
                .spec(getRequestSpecificationRickAndMorty())
                .filter(new AllureRestAssured())
                .when()
                .get("/character/" + id)
                .then()
                .extract()
                .response();

        characterId = new JSONObject(gettingCharacter.getBody().asString()).get("id").toString();
        mortyLocation = new JSONObject(gettingCharacter.getBody().asString()).getJSONObject("location").get("name").toString();
        mortySpecies = new JSONObject(gettingCharacter.getBody().asString()).get("species").toString();

        Allure.addAttachment("ID персонажа", characterId);
        Allure.addAttachment("Местонахождение Морти", mortyLocation);
        Allure.addAttachment("Раса Морти", mortySpecies);
    }

    @Затем("получить номер последнего эпизода, в котором появился персонаж")
    public static void getLastEpisodeNumber() {
        Response getLastEpisode = given()
                .spec(getRequestSpecificationRickAndMorty())
                .filter(new AllureRestAssured())
                .when()
                .get("/character/" + characterId)
                .then()
                .extract()
                .response();

        int episode = (new JSONObject(getLastEpisode.getBody().asString()).getJSONArray("episode").length() - 1);
        lastEpisode = Integer.parseInt(new JSONObject(getLastEpisode.getBody().asString())
                .getJSONArray("episode").get(episode).toString().replaceAll("[^0-9]", ""));

        Allure.addAttachment("Номер последнего эпизода", String.valueOf(lastEpisode));
    }

    @Затем("получить идентификатор последнего персонажа из последнего эпизода")
    public static void getLastCharacterId() {
        Response gettingLastChar = given()
                .spec(getRequestSpecificationRickAndMorty())
                .filter(new AllureRestAssured())
                .when()
                .get("/episode/" + lastEpisode)
                .then()
                .extract()
                .response();

        int lastCharIndex = (new JSONObject(gettingLastChar.getBody().asString()).getJSONArray("characters").length() - 1);
        lastCharacter = Integer.parseInt(new JSONObject(gettingLastChar.getBody().asString())
                .getJSONArray("characters").get(lastCharIndex).toString().replaceAll("[^0-9]", ""));

        Allure.addAttachment("ID последнего персонажа", String.valueOf(lastCharacter));
    }

    @Затем("получить информацию о последнем персонаже")
    public static void getLastCharacterInfo() {
        Response lastCharInfo = given()
                .spec(getRequestSpecificationRickAndMorty())
                .filter(new AllureRestAssured())
                .when()
                .get("/character/" + lastCharacter)
                .then()
                .extract()
                .response();

        lastCharRace = new JSONObject(lastCharInfo.getBody().asString()).get("species").toString();
        lastCharLoc = new JSONObject(lastCharInfo.getBody().asString()).getJSONObject("location").get("name").toString();

        Allure.addAttachment("Информация о последнем персонаже", lastCharInfo.getBody().asString());
    }

    @Тогда("проверить совпадение местонахождения")
    public void locationAssert() {
        String message = String.format("Местонахождение последнего персонажа: %s; Местонахождение Морти: %s", lastCharLoc, mortyLocation);
        Allure.addAttachment("Детали проверки совпадения местонахождения", message);
        assertEquals(mortyLocation, lastCharLoc);
    }


    @Тогда("проверить совпадение расы")
    public static void raceAssert() {
        String message = String.format("Раса последнего персонажа: %s; Раса Морти: %s", lastCharRace, mortySpecies);
        Allure.addAttachment("Проверка совпадения расы", message);
        Assertions.assertEquals(mortySpecies, lastCharRace);
    }


}
