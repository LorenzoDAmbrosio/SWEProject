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
@Table(name="AddRatingAction")
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class AddRatingAction extends CallToAction implements Serializable  {

    private int rating;

    @Override
    public int getInteractionValue() {
        return rating;
    }
}