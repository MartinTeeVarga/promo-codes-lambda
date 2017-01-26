package com.czequered.promocodes.lambda;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.czequered.promocodes.model.Code;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

import static com.amazonaws.util.StringUtils.isNullOrEmpty;

public class CodeRepository {
    private static final String BAD_REQUEST = "[Bad Request] Both game and code parameters must be specified.";
    private static final String NOT_FOUND = "[Not Found] The game or code does not exist.";
    private static final String GONE = "[Gone] The code is no longer valid.";
    private DynamoDBMapper mapper;

    public CodeRepository(DynamoDBMapper mapper) {
        this.mapper = mapper;
    }


    public Code find(Code codeRequest) {
        if (codeRequest == null || isNullOrEmpty(codeRequest.getGame()) || isNullOrEmpty(codeRequest.getCode())) {
            throw new RuntimeException(BAD_REQUEST);
        }

        Code loaded = mapper.load(codeRequest);

        if (loaded == null) {
            throw new RuntimeException(NOT_FOUND);
        }

        Instant now = Instant.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_DATE_TIME;

        TemporalAccessor taFrom = timeFormatter.parse(loaded.getFrom());
        Instant from = Instant.from(taFrom);
        if (now.compareTo(from) < 0) {
            throw new RuntimeException(NOT_FOUND);
        }

        TemporalAccessor taTo = timeFormatter.parse(loaded.getTo());
        Instant to = Instant.from(taTo);
        if (now.compareTo(to) > 0) {
            throw new RuntimeException(GONE);
        }

        return loaded;
    }
}
