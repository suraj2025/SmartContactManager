package com.smart.entities;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document // Optional: specify collection name
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String uid;  // Use String instead of int, or store ObjectId

    private String name;
    private String email;
    private String password;
    private String role;
    private boolean enabled;
    private String imageUrl;
    private String about;

}
