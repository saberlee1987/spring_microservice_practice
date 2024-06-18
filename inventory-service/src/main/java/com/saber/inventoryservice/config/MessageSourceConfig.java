package com.saber.inventoryservice.config;

import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.context.MessageSourceProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
@Configuration
public class MessageSourceConfig extends MessageSourceAutoConfiguration {
    @Bean
    public MessageSource messageSource(MessageSourceProperties properties){
        ReloadableResourceBundleMessageSource messageSource =
                                new ReloadableResourceBundleMessageSource();
        if (StringUtils.hasText(properties.getBasename())) {
            for (String baseName : StringUtils.commaDelimitedListToSet(properties.getBasename())) {
                messageSource.addBasenames("classpath:" + baseName);
            }
        }
        if (properties.getEncoding() != null) {
            messageSource.setDefaultEncoding(properties.getEncoding().name());
        }else {
            messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        }

        if (properties.getCacheDuration() != null) {
            messageSource.setCacheMillis(properties.getCacheDuration().toMillis());
        }else {
            messageSource.setCacheMillis(-1);
        }
        messageSource.setFallbackToSystemLocale(properties.isFallbackToSystemLocale());
        messageSource.setAlwaysUseMessageFormat(properties.isAlwaysUseMessageFormat());
        messageSource.setUseCodeAsDefaultMessage(properties.isUseCodeAsDefaultMessage());
        return messageSource;
    }
}
