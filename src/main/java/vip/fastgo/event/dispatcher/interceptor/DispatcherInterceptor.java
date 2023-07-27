package vip.fastgo.event.dispatcher.interceptor;

import vip.fastgo.event.dispatcher.configuration.AppConfiguration;
import vip.fastgo.event.dispatcher.core.Dispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
public class DispatcherInterceptor implements HandlerInterceptor {
    AppConfiguration appConfiguration;

    public DispatcherInterceptor(AppConfiguration appConfiguration) {
        this.appConfiguration = appConfiguration;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("DispatcherInterceptor.preHandle， url: {}", request.getRequestURI());
        return new Dispatcher(appConfiguration).handle(request, response);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }
}
