package com.ruoyi.touki.config;

import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.ruoyi.touki.mapper")
public class MybatisPlusConfig {

     /**
     * 逻辑删除配置（核心）
     */
    @Bean
    public ISqlInjector sqlInjector() {
        return new DefaultSqlInjector();
    }

    /**
     * 全局配置（逻辑删除在这里）
     */
    @Bean
    public GlobalConfig globalConfig() {

        GlobalConfig globalConfig = new GlobalConfig();

        GlobalConfig.DbConfig dbConfig = new GlobalConfig.DbConfig();

        // 逻辑删除字段
        dbConfig.setLogicDeleteField("deleted");

        // 未删除值
        dbConfig.setLogicNotDeleteValue("0");

        // 已删除值
        dbConfig.setLogicDeleteValue("1");

        globalConfig.setDbConfig(dbConfig);

        return globalConfig;
    }
}
