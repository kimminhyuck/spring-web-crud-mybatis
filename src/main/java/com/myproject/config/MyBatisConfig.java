package com.myproject.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.myproject.mapper")  // Mapper 인터페이스가 있는 패키지 스캔
public class MyBatisConfig {
}
