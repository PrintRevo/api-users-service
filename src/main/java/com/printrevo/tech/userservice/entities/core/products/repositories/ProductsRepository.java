package com.printrevo.tech.userservice.entities.core.products.repositories;


import com.printrevo.tech.userservice.entities.core.products.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsRepository extends JpaRepository<Product, String>,
        JpaSpecificationExecutor<Product>, PagingAndSortingRepository<Product, String> {

}
