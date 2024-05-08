package com.project.sweprojectspring.models.billings;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

@Entity
@Table(name="Subscription")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public abstract class Subscription implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    private Date subscriptionStart;
    private Time subscriptionDuration;


    public abstract int getMaximumNumberOfWishlist();

    public Subscription(Date subscriptionStart, Time subscriptionDuration) {
        this.subscriptionStart = subscriptionStart;
        this.subscriptionDuration = subscriptionDuration;
    }

    public Subscription(Long id) {
        this.id=id;
    }

    /**
     * Checks if the subscription is expired.
     * @return true if the subscription is expired, otherwise false.
     */
    public boolean isExpired() {
        Date currentDate = new Date();
        long currentTime = currentDate.getTime();
        long expirationTime = subscriptionStart.getTime() + subscriptionDuration.getTime();
        return currentTime > expirationTime;
    }
}
