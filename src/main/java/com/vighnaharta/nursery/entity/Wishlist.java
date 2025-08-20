package com.vighnaharta.nursery.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "wishlists")
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // One wishlist per user
    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    // Plants saved in wishlist
    @ManyToMany
    @JoinTable(
        name = "wishlist_items",
        joinColumns = @JoinColumn(name = "wishlist_id"),
        inverseJoinColumns = @JoinColumn(name = "plant_id")
    )
    private List<Plant> plants = new ArrayList<>();

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public List<Plant> getPlants() { return plants; }
    public void setPlants(List<Plant> plants) { this.plants = plants; }
}
