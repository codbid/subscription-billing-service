package org.example.sbs.kafka.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.sbs.enums.SubscriptionStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionStatusUpdateEvent {
    private Long subscriptionId;

    private SubscriptionStatus status;
}
