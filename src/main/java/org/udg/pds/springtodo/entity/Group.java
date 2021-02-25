package org.udg.pds.springtodo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.checkerframework.checker.units.qual.A;
import org.udg.pds.springtodo.serializer.JsonTagSerializer;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity(name = "usergroup")
public class Group implements Serializable{

    public Group() {
    }

    public Group(String name, String description) { //es passa un array list de users
        this.name=name;
        this.description=description;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="fk_owner")
    private User owner;

    @Column(name = "fk_owner", insertable = false, updatable = false)
    private Long ownerId;

    private String description;

    private String name;

    @JsonView(Views.Private.class)
    public Long getId() {
        return id;
    }

    @JsonView(Views.Private.class)
    public String getName() {
        return name;
    }

    @JsonView(Views.Public.class)
    public String getDescription() {
        return description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonIgnore
    public User getUser() {
        return owner;
    }

    public void setUser(User user) {
        this.owner = user;
    }

}
