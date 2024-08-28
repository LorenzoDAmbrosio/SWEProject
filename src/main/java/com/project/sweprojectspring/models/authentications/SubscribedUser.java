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
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="Subscribed_User")
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "User_Id")
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class SubscribedUser extends User implements Serializable  {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "subscription_id", referencedColumnName = "id")
    private Subscription subscription;

    @OneToMany(mappedBy="subscribedUser",fetch = FetchType.EAGER)
    private Set<Wishlist> wishlists=new HashSet<>();

    @OneToMany(mappedBy="subscribedUser")
    private Set<CallToAction> callToActions=new HashSet<>();


    public SubscribedUser(String Username, String password){
        super(Username,password);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubscribedUser that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(this.getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subscription);
    }
}