package com.example.onlinemall.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "stores")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String imageUrl;
    private int floor;

    @Enumerated(EnumType.STRING)
    private StoreCategory category;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
}
