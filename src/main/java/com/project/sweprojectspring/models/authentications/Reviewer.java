package com.project.sweprojectspring.models.authentications;

import com.project.sweprojectspring.models.resources.Review;
import com.project.sweprojectspring.models.resources.Wishlist;
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
@Table(name="Reviewer")
@PrimaryKeyJoinColumn(name = "Subscribed_User_Id")
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class Reviewer extends SubscribedUser implements Serializable  {


    @OneToMany(mappedBy="reviewer",fetch = FetchType.EAGER)
    private Set<Review> Reviews=new HashSet<>();

    public Reviewer(String Username, String password){
        super(Username,password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reviewer reviewer)) return false;
        if (!super.equals(o)) return false;
        return this.getId()==reviewer.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }
}