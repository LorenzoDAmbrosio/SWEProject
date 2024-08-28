package com.project.sweprojectspring.models.billings;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Entity
@Table(name="Premium_Subscription")
@Data @NoArgsConstructor @ToString
public class PremiumSub extends Subscription implements Serializable  {

    @Override
    public int getMaximumNumberOfWishlist() {
        return Integer.MAX_VALUE;
    }
}