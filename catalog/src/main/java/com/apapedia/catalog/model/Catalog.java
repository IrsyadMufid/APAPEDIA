package com.apapedia.catalog.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;
// import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "catalog")
@JsonIgnoreProperties(value = "category", allowSetters = true) 
public class Catalog {
    @Id
    @GeneratedValue
    private UUID id;
    
    @Column(name = "seller_id")
    private UUID sellerId;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "product_name", nullable = false, length = 50)
    private String productName;

    @Column(name = "product_description", length = 255)
    private String productDescription;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @JsonManagedReference
    private Category category;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name = "image")
    @JsonIgnore
    private byte[] image;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
}

