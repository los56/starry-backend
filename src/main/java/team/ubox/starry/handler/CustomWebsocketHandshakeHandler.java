package team.ubox.starry.handler;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@Component
public class CustomWebsocketHandshakeHandler extends DefaultHandshakeHandler {
    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        //return super.determineUser(request, wsHandler, attributes);
        return new TestPrincipal(UUID.randomUUID().toString());
    }

    public static class TestPrincipal implements Principal {
        private final String name;

        TestPrincipal(String name) {
            this.name = name;
        }
        @Override
        public String getName() {
            return name;
        }
    }
}
