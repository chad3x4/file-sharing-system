package com.chad3x4.myproject.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Authority {
    @Id
    private Long id;

    @NotEmpty(message = "Must specify an user for this permission")
    private long userId;

    @NotEmpty(message = "Must specify a file for this permission")
    private long fileId;

    @NotEmpty(message = "Must specify actions for this permission")
    private List<String> actions = new ArrayList<>();

}
