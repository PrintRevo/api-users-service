package com.printrevo.tech.userservice.entities.core.products.models;

import com.printrevo.tech.platform.db.BaseAuditable;
import com.printrevo.tech.platform.db.Transferable;
import com.printrevo.tech.userservice.entities.core.products.dto.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product extends BaseAuditable implements Transferable<ProductDto> {

    @Column(name = "product_name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "auth_group")
    private String authGroup;

    @Column(name = "base_url")
    private String baseUrl;

    @Column(name = "is_public")
    private boolean isPublic;

    @Override
    public ProductDto toDTO() {
        return new ProductDto(name, description, authGroup, baseUrl, isPublic);
    }
}
