package com.example.demo.entity;

import jakarta.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "suites")
public class Suite implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String label;
    private String category;
    private Double rate;

    // Constructors
    public Suite() {}

    public Suite(String label, String category, Double rate) {
        this.label = label;
        this.category = category;
        this.rate = rate;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Double getRate() { return rate; }
    public void setRate(Double rate) { this.rate = rate; }
}
