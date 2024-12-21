package com.tango.auth_service.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "file")
public class File extends BaseEntity{

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String extension;

//    private String email;

    @Column(nullable = false)
    private String originalName;

    @Column(nullable = false)
    private String size;
}
