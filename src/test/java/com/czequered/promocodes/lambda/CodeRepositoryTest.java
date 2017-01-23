package com.czequered.promocodes.lambda;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CodeRepositoryTest {
    DynamoDBMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = mock(DynamoDBMapper.class);
    }

    @Test
    public void handleRequestOk() throws Exception {
        Code returnCode = createCode("test", "PUB1", "2012-01-23T10:02:46+00:00", "2037-01-23T10:02:46+00:00", true, "Hello");

        CodeRepository cr = new CodeRepository(mapper);
        Code codeRequest = new Code();
        codeRequest.setGame("test");
        codeRequest.setCode("PUB1");

        when(mapper.load(codeRequest)).thenReturn(returnCode);

        Code code = cr.find(codeRequest);
        assertEquals("test", code.getGame());
        assertEquals("PUB1", code.getCode());
        assertEquals("2012-01-23T10:02:46+00:00", code.getFrom());
        assertEquals("2037-01-23T10:02:46+00:00", code.getTo());
        assertEquals(true, code.getPub());
        assertEquals("Hello", code.getPayload());
    }

    private Code createCode(String game, String code, String from, String to, boolean pub, String payload) {
        Code returnCode = new Code();
        returnCode.setGame(game);
        returnCode.setCode(code);
        returnCode.setFrom(from);
        returnCode.setTo(to);
        returnCode.setPub(pub);
        returnCode.setPayload(payload);
        return returnCode;
    }

}