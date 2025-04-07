package com.saif.pfe.models;

import com.saif.pfe.models.ennum.Status;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Pfe extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Status status;
}
