package com.czequered.promocodes.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

/**
 * Created by Martin on 22/01/2017.
 */
public class CodeHandler implements RequestHandler<Code, Code> {

    @Override
    public Code handleRequest(Code codeRequest, Context context) {
        CodeRepository cr = new CodeRepository();
        return cr.find(codeRequest);
    }
}
