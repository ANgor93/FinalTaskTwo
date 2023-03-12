package ApiSpecification;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import utils.Configuration;

public class ApiSpecification {

    private static final RequestSpecification REQUEST_SPECIFICATION_RICK_AND_MORTY = new RequestSpecBuilder()
            .setBaseUri(Configuration.getConfigValue("baseUrlRickAndMorty"))
            .build();

    public static RequestSpecification getRequestSpecificationRickAndMorty() {
        return REQUEST_SPECIFICATION_RICK_AND_MORTY;
    }

    private static final RequestSpecification REQUEST_SPECIFICATION_CREATE_USER = new RequestSpecBuilder()
            .setBaseUri(Configuration.getConfigValue("baseUrlCreateUser"))
            .build();

    public static RequestSpecification getRequestSpecificationCreateUser() {
        return REQUEST_SPECIFICATION_CREATE_USER;
    }
}

