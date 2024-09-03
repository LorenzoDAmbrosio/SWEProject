package com.project.sweprojectspring.models.resources;

import com.project.sweprojectspring.models.actions.CallToAction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
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
    private String author;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return Objects.equals(id, film.id) && Objects.equals(title, film.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }

    public boolean isInWishlist(Wishlist wishlist) {
        if(wishlist == null) return false;
        if(wishlist.getFilms().isEmpty()) return  false;
        return wishlist.getFilms().contains(this);
    }
}