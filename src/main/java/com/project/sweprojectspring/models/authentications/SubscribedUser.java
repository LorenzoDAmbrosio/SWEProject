package com.project.sweprojectspring.models.authentications;

import com.project.sweprojectspring.models.actions.CallToAction;
import com.project.sweprojectspring.models.authentications.User;
import com.project.sweprojectspring.models.billings.Subscription;
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
@Table(name="SubscribedUser")
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "UserId")
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class SubscribedUser extends User implements Serializable  {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "subscription_id", referencedColumnName = "id")
    private Subscription subscription;

    @OneToMany(mappedBy="subscribedUser")
    private Set<Wishlist> wishlists=new HashSet<>();

    @OneToMany(mappedBy="subscribedUser")
    private Set<CallToAction> callToActions=new HashSet<>();


    public SubscribedUser(String Username, String password){
        super(Username,password);
    }
}