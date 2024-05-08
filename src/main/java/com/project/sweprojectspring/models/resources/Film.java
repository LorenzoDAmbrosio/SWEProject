package com.project.sweprojectspring.models.resources;

import com.project.sweprojectspring.models.actions.CallToAction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="Film")
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class Film implements Serializable  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String description;
    private int releaseDate;

    @OneToMany(mappedBy="film")
    private Set<Review> reviews=new HashSet<>();

    @OneToMany(mappedBy="film")
    private Set<CallToAction> callToActions=new HashSet<>();


    public Film(long id, String title,int releaseDate){
        this.id=id;
        this.title=title;
        this.releaseDate=releaseDate;
    }
    public Film(long id){
        this.id=id;
    }
}