package ru.renattele.admin95.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "application_properties")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationPropertyEntity {
    @Id
    private String id;

    @Column(name = "value", length = 500)
    private String value;
}
