package com.mzkj.news;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.web.bind.annotation.Mapping;

//@MapperScan("com.mzkj.news.mapper")
@SpringBootApplication
@EnableScheduling
public class NewsApplication {
	public static void main(String[] args) {
		SpringApplication.run(NewsApplication.class, args);
	}
}
