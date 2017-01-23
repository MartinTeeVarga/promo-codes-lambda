package com.czequered.promocodes.lambda;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

import static com.amazonaws.util.StringUtils.isNullOrEmpty;

public class CodeRepository {
    private static final String BAD_REQUEST = "400";
    private static final String NOT_FOUND = "404";
    private static final String GONE = "410";
    private DynamoDBMapper mapper;

    public CodeRepository(DynamoDBMapper mapper) {
        this.mapper = mapper;
    }


    public Code find(Code codeRequest) {
        if (isNullOrEmpty(codeRequest.getCode()) || isNullOrEmpty(codeRequest.getGame()) || isNullOrEmpty(codeRequest.getCode())) {
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
