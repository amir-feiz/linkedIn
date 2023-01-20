package com.example.linkedin.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Table
@Data
public class LastNumber {

    @Id
    private Long id;
}
