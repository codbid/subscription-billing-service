package org.example.sbs.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSubscriptionRequest {
    private Long ownerId;

    private Long planId;
}
