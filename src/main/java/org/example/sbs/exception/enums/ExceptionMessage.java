package org.example.sbs.exception.enums;

import lombok.Getter;

@Getter
public enum ExceptionMessage {
    ENTITY_NOT_FOUND("Entity not found"),
    FORBIDDEN("You do not have permission to access this resource"),
    ALREADY_HAVE_SUBSCRIPTION("You already have a subscription"),
    USERS_LIMIT_REACHED("The limit of users in the subscription has been reached"),
    FORBIDDEN_ROLE("You do not have permission to set/unset this role");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String generateNotFoundEntityMessage(String entityName, Long id) {
        return entityName + " with id: " + id + " not found";
    }
}
