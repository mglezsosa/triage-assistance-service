package tech.sosa.triage_assistance_service.applications.springframework.boot.controller;

import org.jboss.logging.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.config.annotation.*;
import tech.sosa.triage_assistance_service.identity_access.domain.model.AuthService;
import tech.sosa.triage_assistance_service.identity_access.domain.model.Credentials;

import java.util.LinkedList;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketNotificationsConfig implements WebSocketMessageBrokerConfigurer {

    private final AuthService authService;

    public WebSocketNotificationsConfig(AuthService authService) {
        this.authService = authService;
    }


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-connect").setAllowedOrigins("*");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/ws");
        registry.enableStompBrokerRelay("/topic/")
                .setRelayHost("localhost")
                .setRelayPort(61613)
                .setClientLogin("guest")
                .setClientPasscode("guest");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {

            @Override
            public Message preSend(Message message, MessageChannel channel) {

                if (message.getHeaders().get("stompCommand") == StompCommand.SUBSCRIBE) {

                    Map<String, LinkedList<String>> nativeHeaders =
                            (Map<String, LinkedList<String>>) message.getHeaders().get("nativeHeaders");

                    LinkedList<String> authorizations = nativeHeaders == null ? null : nativeHeaders.get("authorization");

                    String authorization = authorizations == null || authorizations.size() == 0 ? null
                            : authorizations.getFirst();

                    assert authorization != null;
                    assert authorization.split(" ")[0].equals("Bearer");

                    String token = authorization.split(" ")[1];

                    Logger.getLogger(WebSocketNotificationsConfig.class).info(nativeHeaders);

                    message = authService.authenticate(new Credentials(token)).isEmpty() ? null : message;

                }

                return message;
            }

        });

    }

}