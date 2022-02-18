package com.hirese.service.entity;

import com.hirese.service.entity.enums.CategoryName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "CATEGORIES")
public class Category {
    @Id
    @JsonIgnore
    private UUID categoryId;
    @Enumerated(EnumType.STRING)
    private CategoryName name;
    private BigDecimal pricePerDay;
    @OneToMany(mappedBy = "categoryOfVehicle" ,fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Vehicle> vehicles;
}
