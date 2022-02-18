package com.hirese.service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "USERS")
public class User {
    @Id
    private UUID userId;
    @OneToMany(mappedBy ="user")
    @JsonIgnore
    private List<BookingOrder> orders;
    private String userName;
}
