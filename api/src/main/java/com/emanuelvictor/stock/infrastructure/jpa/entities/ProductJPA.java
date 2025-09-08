package com.emanuelvictor.stock.infrastructure.jpa.entities;

import com.emanuelvictor.common.infrastructure.audit.AbstractJPAEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.util.UUID;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "product")
@Audited(withModifiedFlag = true)
public class ProductJPA extends AbstractJPAEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Integer quantityAvailable;

    public ProductJPA(UUID id, String name, Integer quantityAvailable) {
        this.id = id;
        this.name = name;
        this.quantityAvailable = quantityAvailable;
    }

}
