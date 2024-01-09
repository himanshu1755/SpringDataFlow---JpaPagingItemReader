package com.spring.data_flow_readers.JPAReader.models;

import javax.persistence.*;

@Entity
@Table(name = "my_entity")
public class MyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    // Constructors
    public MyEntity() {
    }

    public MyEntity(String description) {
        this.description = description;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // toString() for logging or debugging purposes
    @Override
    public String toString() {
        return "MyEntity{" +
                "id=" + id +
                ", description='" + description + '\'' +
                '}';
    }
}
