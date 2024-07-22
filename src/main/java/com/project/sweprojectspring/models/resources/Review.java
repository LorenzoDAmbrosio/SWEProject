package com.project.sweprojectspring.models.resources;

import com.project.sweprojectspring.models.authentications.Reviewer;
import com.project.sweprojectspring.models.authentications.SubscribedUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="Review")
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class Review implements Serializable  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String description;
    private Date publishDate;

    @ManyToOne
    @JoinColumn(name="film_id", nullable=false)
    private Film film;

    @ManyToOne
    @JoinColumn(name="reviewer_id", nullable=false)
    private Reviewer reviewer;



    public Review(long id, String description){
        this.id=id;
        this.description=description;
    }
    public Review(String description){
        this.description=description;
    }
}