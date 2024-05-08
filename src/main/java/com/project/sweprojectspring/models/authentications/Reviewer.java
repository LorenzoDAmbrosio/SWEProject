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
import java.util.Set;

@Entity
@Table(name="Reviewer")
@PrimaryKeyJoinColumn(name = "Subscribed_User_Id")
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class Reviewer extends SubscribedUser implements Serializable  {


    @OneToMany(mappedBy="reviewer")
    private Set<Review> Reviews=new HashSet<>();

    public Reviewer(String Username, String password){
        super(Username,password);
    }
}