package com.project.sweprojectspring.models.resources;

import com.project.sweprojectspring.models.authentications.SubscribedUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="Wishlist")
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class Wishlist implements Serializable  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String description;

    @ManyToMany(cascade = { CascadeType.ALL },fetch = FetchType.EAGER)
    @JoinTable(
            name = "Wishlist_Film",
            joinColumns = { @JoinColumn(name = "wishlist_id") },
            inverseJoinColumns = { @JoinColumn(name = "film_id") }
    )
    private Set<Film> films=new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="subscribedUser_id", nullable=false)
    private SubscribedUser subscribedUser;

    public Wishlist(long id, String name){
        this.id=id;
        this.name=name;
    }
    public Wishlist(String name){
        this.name=name;
    }

    public Wishlist(String name, String description, SubscribedUser user) {
        this.name=name;
        this.description=description;
        this.setSubscribedUser(user);
    }

    public Integer getNumberOfFilms() {
        if(films.isEmpty()) return  0;
        return films.size();
    }
    public Boolean containsFilm(Film film) {
        if(films.isEmpty()) return  false;
        return films.contains(film);
    }
}