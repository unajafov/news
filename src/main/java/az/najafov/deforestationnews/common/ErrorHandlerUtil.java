package az.najafov.deforestationnews.common;

import az.najafov.deforestationnews.exception.BaseErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;


@Slf4j
public class ErrorHandlerUtil {

    private static final ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();

    public static void buildHttpErrorResponse(ServletResponse response, BaseErrorResponse baseErrorResponse,
                                              Integer code) throws IOException {
        log.error("buildHttpErrorResponse : {}", baseErrorResponse);
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        GenericResponse<BaseErrorResponse> errorResponse = GenericResponse.failure(code,
                "EXCEPTION", baseErrorResponse);
        byte[] responseToSend = mapper.writeValueAsString(errorResponse).getBytes();
        httpServletResponse.setHeader("Content-Type", "application/json");
        httpServletResponse.setStatus(code);
        response.getOutputStream().write(responseToSend);
    }

}