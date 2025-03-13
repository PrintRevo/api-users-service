package com.printrevo.tech.userservice.services.users.query.instructions;

import com.printrevo.tech.platform.db.TransactionFilter;
import com.printrevo.tech.platform.services.Query;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetUserByIdentifierQuery implements Query {
    private TransactionFilter transactionFilter;
}
