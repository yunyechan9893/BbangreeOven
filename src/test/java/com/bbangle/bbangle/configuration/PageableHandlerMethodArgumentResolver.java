package com.bbangle.bbangle.configuration;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class PageableHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return Pageable.class.equals(methodParameter.getParameterType());
    }

    @Override
    public Pageable resolveArgument(MethodParameter methodParameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        int page = Integer.parseInt(webRequest.getParameter("page") != null ? webRequest.getParameter("page") : "0");
        int size = Integer.parseInt(webRequest.getParameter("size") != null ? webRequest.getParameter("size") : "1");
        return PageRequest.of(page, size);
    }

}
