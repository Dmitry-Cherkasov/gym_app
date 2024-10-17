package com.gym_app.core.dto;

import com.gym_app.core.enums.TrainingType;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@DiscriminatorValue("TRAINER")
@Inheritance(strategy = InheritanceType.JOINED)
public class Trainer extends User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRAINER_ID")
    private long id;
    @OneToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
    private User user;
    @Column(name = "SPECIALIZATION", nullable = false)
    @Enumerated(EnumType.STRING)
    private TrainingType specialization;

    public Trainer(String firstName, String lastName, String userName, String password, boolean isActive, TrainingType specialization) {
        super(firstName, lastName, userName, password, isActive);
        this.specialization = specialization;
    }

    public TrainingType getSpecialization() {
        return specialization;
    }

    public void setSpecialization(TrainingType specialization) {
        this.specialization = specialization;
    }


    @Override
    public String toString() {
        return "Trainer{" + super.toString() +
                " " + specialization + '\'' +
                '}';
    }


}
