package tw.liangze.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//處理跨域問題，允許所有本地端的源頭、請求方法、請求頭部
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*") // 允許所有源頭
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 允許的請求方法
                .allowedHeaders("*"); // 允許的請求頭部
    }
}
