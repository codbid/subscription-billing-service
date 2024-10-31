package org.example.sbs.exception.enums;

import lombok.Getter;

@Getter
public enum ExceptionMessage {
    ENTITY_NOT_FOUND("Entity not found"),
    NO_PERMISSION_SEND_DOCUMENT("You have no permission to send this document"),
    COULD_NOT_ADD_DOCUMENT("Could not add document");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String generateNotFoundEntityMessage(String entityName, Long id) {
        return entityName + " with id: " + id + " not found";
    }
}
