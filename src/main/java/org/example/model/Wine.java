package org.example.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;

import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@Setter
@SQLDelete(sql = "UPDATE wines SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@Table(name = "wines")
@Accessors(chain = true)
public class Wine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String wineName;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    @Column(nullable = false)
    private String countryOfOrigin;
    @Column(nullable = false)
    private String wineType;
    @Column(nullable = false, precision = 3, scale = 2)
    private BigDecimal popularityRating;
    @NotEmpty
    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(columnDefinition = "text[]", nullable = false)
    private List<String> occasions;
    @Column(nullable = false)
    private String productImage;
    @Column(nullable = false)
    private boolean isDeleted = false;
}
