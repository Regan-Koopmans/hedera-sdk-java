package com.hedera.hashgraph.sdk;

import com.hedera.hashgraph.sdk.proto.CryptoGetAccountRecordsQuery;
import com.hedera.hashgraph.sdk.proto.CryptoServiceGrpc;
import com.hedera.hashgraph.sdk.proto.Query;
import com.hedera.hashgraph.sdk.proto.QueryHeader;
import com.hedera.hashgraph.sdk.proto.Response;
import com.hedera.hashgraph.sdk.proto.ResponseHeader;
import io.grpc.MethodDescriptor;

import java.util.ArrayList;
import java.util.List;

/**
 * Get all the records for an account for any transfers into it and out of it,
 * that were above the threshold, during the last 25 hours.
 */
public final class AccountRecordsQuery extends QueryBuilder<List<TransactionRecord>, AccountRecordsQuery> {
    private final CryptoGetAccountRecordsQuery.Builder builder;

    public AccountRecordsQuery() {
        this.builder = CryptoGetAccountRecordsQuery.newBuilder();
    }

    /**
     * Sets the account ID for which the records should be retrieved.
     *
     * @return {@code this}
     */
    public AccountRecordsQuery setAccountId(AccountId accountId) {
        builder.setAccountID(accountId.toProtobuf());
        return this;
    }

    @Override
    void onMakeRequest(Query.Builder queryBuilder, QueryHeader header) {
        queryBuilder.setCryptoGetAccountRecords(builder.setHeader(header));
    }

    @Override
    ResponseHeader mapResponseHeader(Response response) {
        return response.getCryptoGetAccountRecords().getHeader();
    }

    @Override
    QueryHeader mapRequestHeader(Query request) {
        return request.getCryptoGetAccountRecords().getHeader();
    }

    @Override
    List<TransactionRecord> mapResponse(Response response) {
        var rawTransactionRecords = response.getCryptoGetAccountRecords().getRecordsList();
        var transactionRecords = new ArrayList<TransactionRecord>(rawTransactionRecords.size());

        for (var record : rawTransactionRecords) {
            transactionRecords.add(TransactionRecord.fromProtobuf(record));
        }

        return transactionRecords;
    }

    @Override
    MethodDescriptor<Query, Response> getMethodDescriptor() {
        return CryptoServiceGrpc.getGetAccountRecordsMethod();
    }
}
