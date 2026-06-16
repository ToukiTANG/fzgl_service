package com.ruoyi.framework.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * @author Touki
 */
@Configuration
public class JacksonConfig {

    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {

        return builder -> {

            // =========================
            // 1. Long -> String（防精度丢失）
            // =========================
            SimpleModule longModule = new SimpleModule();
            longModule.addSerializer(Long.class, ToStringSerializer.instance);
            longModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
            builder.modules(longModule);

            // =========================
            // 2. 时间格式化（LocalDateTime / LocalDate）
            // =========================
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATETIME_FORMAT);
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

            builder.serializers(
                    new LocalDateTimeSerializer(dateTimeFormatter),
                    new LocalDateSerializer(dateFormatter)
            );

            builder.deserializers(
                    new LocalDateTimeDeserializer(dateTimeFormatter),
                    new LocalDateDeserializer(dateFormatter)
            );

            // =========================
            // 3. 默认日期格式（Date类型兜底）
            // =========================
            builder.simpleDateFormat(DATETIME_FORMAT);

            // =========================
            // 4. 时区（非常重要）
            // =========================
            builder.timeZone(TimeZone.getTimeZone("GMT+8"));
        };
    }
}