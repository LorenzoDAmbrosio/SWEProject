package com.project.sweprojectspring.models.authentications;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.velocity.tools.generic.ClassTool;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="Customer")
@PrimaryKeyJoinColumn(name = "Subscribed_User_Id")
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class Customer extends SubscribedUser implements Serializable  {

    private Date BornDate;

}