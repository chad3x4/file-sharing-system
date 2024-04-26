package com.chad3x4.myproject.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank(message = "Username is mandatory")
    @Column(unique = true)
    private String username;

    @Email(message = "Email shoud be valid")
    private String email;

    private String department;

    private String phone;

    @NotBlank(message = "Roles is mandatory")
    private String roles;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 6, message = "Password should be at least 6 characters long")
    private String password;

    @OneToMany(
        mappedBy = "author",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<FileInfo> files = new ArrayList<>();

    public void addFile(FileInfo file) {
        files.add(file);
        file.setAuthor(this);
    }

    public void removeComment(FileInfo file) {
        files.remove(file);
        file.setAuthor(null);
    }
    
}
