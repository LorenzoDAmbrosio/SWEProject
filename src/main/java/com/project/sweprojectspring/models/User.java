package com.project.sweprojectspring.models;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Entity
@Table(name="User")
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    public User(String username,String password){
        this.username=username;
        this.password=password;
    }

    public User(Long id) {
        this.id=id;
    }
}
