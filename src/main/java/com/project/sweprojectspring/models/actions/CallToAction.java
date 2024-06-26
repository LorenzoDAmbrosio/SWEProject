package com.project.sweprojectspring.models.actions;
import com.project.sweprojectspring.models.authentications.SubscribedUser;
import com.project.sweprojectspring.models.resources.Film;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

@Entity
@Table(name="CallToAction")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public abstract class CallToAction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name="film_id", nullable=false)
    private Film film;
    @ManyToOne
    @JoinColumn(name="subscribedUser_id", nullable=false)
    private SubscribedUser subscribedUser;

    public abstract int getInteractionValue();
}
