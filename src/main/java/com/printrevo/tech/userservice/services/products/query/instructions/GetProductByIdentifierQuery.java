package com.printrevo.tech.userservice.services.products.query.instructions;

import com.printrevo.tech.commonsecurity.dto.keycloak.SecurityGroup;
import com.printrevo.tech.platform.db.TransactionFilter;
import com.printrevo.tech.platform.services.Query;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetProductByIdentifierQuery implements Query {
    private TransactionFilter transactionFilter;
}
