package br.com.productapims.config.interceptor;

import br.com.productapims.config.exception.ValidationException;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
public class FeignClientAuthInterceptor implements RequestInterceptor {
    private static final String AUTHORIZATION = "Authorization";

    @Override
    public void apply(RequestTemplate template) {
        var currentRequest = getCurrentRequest();

        template.header(AUTHORIZATION, currentRequest.getHeader(AUTHORIZATION));
    }

    private HttpServletRequest getCurrentRequest(){
        try{

            return (((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        }catch (Exception e){
            e.printStackTrace();
            throw new ValidationException("The current request could not be processed.");
        }
    }
}
