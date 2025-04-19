package com.saif.pfe.models;

import com.saif.pfe.models.ennum.ERole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "roles")
@Builder
@AllArgsConstructor
public class Role extends Base{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private ERole name;

  public Role() {

  }

  public Role(ERole name) {
    this.name = name;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public ERole getName() {
    return name;
  }

  public void setName(ERole name) {
    this.name = name;
  }

  // In Role.java
  public static final Role ADMIN = new Role(ERole.ROLE_ADMIN);
  public static final Role USER = new Role(ERole.ROLE_USER);
  public static final Role MODERATOR = new Role(ERole.ROLE_MODERATOR);

}

