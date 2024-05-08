package com.project.sweprojectspring.models.authentications;

import com.project.sweprojectspring.models.authentications.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Entity
@Table(name="Administrator")
@PrimaryKeyJoinColumn(name = "UserId")
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class Administrator extends User implements Serializable  {

    @Column(unique = true)
    private String employeeCode;

    public Administrator(String Username, String password){
        super(Username,password);
    }
}