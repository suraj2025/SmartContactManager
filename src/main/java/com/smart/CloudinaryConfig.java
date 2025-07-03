package com.smart;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "deyfwg9o9",
            "api_key", "338765869864588",
            "api_secret", "oPKvSVPF5qqm0xzLr_fYvoVghDc",
            "secure", true
        ));
    }
}

