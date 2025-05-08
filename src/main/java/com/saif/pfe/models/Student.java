package com.saif.pfe.models;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Student extends Base {

    @Id

    private Long id;
    private String name;
    private String email;
    private String phone;

    @ManyToOne
    private Classroom classroom;

    @MapsId
    @OneToOne
    @JoinColumn(name = "id")
    private User user;
}
