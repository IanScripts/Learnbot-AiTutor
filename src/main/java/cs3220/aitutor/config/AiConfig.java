package cs3220.aitutor.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    // Spring auto-configures ChatClient.Builder for OpenAI.
    // We turn that into a ChatClient bean we can @Autowired.
    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder.build();
    }
}

