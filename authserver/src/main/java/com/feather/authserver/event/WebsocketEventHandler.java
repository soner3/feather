package com.feather.authserver.event;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.feather.authserver.model.Profile;
import com.feather.authserver.model.User;
import com.feather.authserver.repository.ProfileRepository;
import com.feather.authserver.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebsocketEventHandler {

    private final UserService userService;
    private final ProfileRepository profileRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void handleSessionConnectEvent(SessionConnectEvent event) {
        String username = event.getUser().getName();
        User user = userService.loadUserByUsernameOrEmail(username);
        Profile profile = user.getProfile();
        profile.setOnline(true);
        profileRepository.save(profile);
        messagingTemplate.convertAndSend("/topic/greeting", username);
    }

    @EventListener
    public void handleSessionDisconnectEvent(SessionDisconnectEvent event) {
        String username = event.getUser().getName();
        User user = userService.loadUserByUsernameOrEmail(username);
        Profile profile = user.getProfile();
        profile.setOnline(false);
        profileRepository.save(profile);
        messagingTemplate.convertAndSend("/topic/greeting", username);
    }

}
