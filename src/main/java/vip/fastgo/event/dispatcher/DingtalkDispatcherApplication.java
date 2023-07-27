package vip.fastgo.event.dispatcher;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("vip.fastgo.event.dispatcher.mapper")
@SpringBootApplication
public class DingtalkDispatcherApplication {

    public static void main(String[] args) {
        SpringApplication.run(DingtalkDispatcherApplication.class, args);
    }

}
