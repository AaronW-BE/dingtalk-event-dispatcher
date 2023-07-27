package vip.fastgo.event.dispatcher.configuration;

import vip.fastgo.event.dispatcher.interceptor.DispatcherInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfiguration implements WebMvcConfigurer {

    AppConfiguration appConfiguration;

    public MvcConfiguration(AppConfiguration appConfiguration) {
        this.appConfiguration = appConfiguration;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new DispatcherInterceptor(appConfiguration)).addPathPatterns("/push");
    }
}
