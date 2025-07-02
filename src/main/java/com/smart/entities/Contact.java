package com.smart.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "contacts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contact {

    @Id
    private String cid;

    private String name;
    private String secondName;
    private String email;
    private String work;
    private String phone;

    private String desc;   // Removed @Column(length = 1000)
    private String image;

    @DBRef(lazy = true)
    @JsonIgnore
    private User user;
}
