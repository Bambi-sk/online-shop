package com.example.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "country")
public class Countries {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="id")
    private Long id;


    @Column(name = "name",length = 200)
    private String name;


    @Column(name = "code",length = 200)
    private String code;
}
