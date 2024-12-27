package org.example.finalprojectepamlabapplication.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    @ToString.Exclude
    private String password;

    @Column(nullable = false)
    private boolean isActive;

    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER)
    @ToString.Exclude
    private Trainee trainee;

    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER)
    @ToString.Exclude
    private Trainer trainer;
}
