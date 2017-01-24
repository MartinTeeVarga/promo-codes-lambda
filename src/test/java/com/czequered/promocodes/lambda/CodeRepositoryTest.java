package com.czequered.promocodes.lambda;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.internal.matchers.Contains;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CodeRepositoryTest {
    DynamoDBMapper mapper;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        mapper = mock(DynamoDBMapper.class);
    }

    @Test
    public void handleRequestOk() throws Exception {
        Code codeRequest = createCodeRequest();

        Code returnCode = createCode("test", "PUB1", "2012-01-23T10:02:46+00:00", "2037-01-23T10:02:46+00:00", true, "Hello");
        when(mapper.load(codeRequest)).thenReturn(returnCode);

        CodeRepository cr = new CodeRepository(mapper);

        Code code = cr.find(codeRequest);
        assertEquals("test", code.getGame());
        assertEquals("PUB1", code.getCode());
        assertEquals("2012-01-23T10:02:46+00:00", code.getFrom());
        assertEquals("2037-01-23T10:02:46+00:00", code.getTo());
        assertEquals(true, code.getPub());
        assertEquals("Hello", code.getPayload());
    }

    @Test
    public void handleRequestNotFound() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(new Contains("[Not Found]"));

        Code codeRequest = createCodeRequest();

        when(mapper.load(codeRequest)).thenReturn(null);

        CodeRepository cr = new CodeRepository(mapper);

        cr.find(codeRequest);
    }

    @Test
    public void handleRequestNotFoundNotValidYet() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(new Contains("[Not Found]"));

        Code codeRequest = createCodeRequest();

        Code returnCode = createCode("test", "PUB1", "2036-01-23T10:02:46+00:00", "2037-01-23T10:02:46+00:00", true, "Hello");
        when(mapper.load(codeRequest)).thenReturn(returnCode);

        CodeRepository cr = new CodeRepository(mapper);

        cr.find(codeRequest);
    }


    @Test
    public void handleRequestExpired() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(new Contains("[Gone]"));

        Code codeRequest = createCodeRequest();

        Code returnCode = createCode("test", "PUB1", "2012-01-23T10:02:46+00:00", "2013-01-23T10:02:46+00:00", true, "Hello");
        when(mapper.load(codeRequest)).thenReturn(returnCode);

        CodeRepository cr = new CodeRepository(mapper);

        cr.find(codeRequest);
    }

    @Test
    public void handleRequestBadRequest() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(new Contains("[Bad Request]"));

        CodeRepository cr = new CodeRepository(mapper);

        cr.find(null);
    }


    private Code createCodeRequest() {
        Code codeRequest = new Code();
        codeRequest.setGame("test");
        codeRequest.setCode("PUB1");
        return codeRequest;
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