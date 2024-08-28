package com.project.sweprojectspring.models.billings;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Entity
@Table(name="Standard_Subscription")
@Data @NoArgsConstructor @ToString
public class StandardSub extends Subscription implements Serializable  {

    @Override
    public int getMaximumNumberOfWishlist() {
        return 3;
    }
}