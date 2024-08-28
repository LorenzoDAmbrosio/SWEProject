package com.project.sweprojectspring.models.actions;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Entity
@Table(name="Add_To_Wishlist_Action")
@Data @NoArgsConstructor @ToString
public class AddToWishlistAction extends CallToAction implements Serializable  {
    @Override
    public int getInteractionValue() {
        return 3;
    }
}