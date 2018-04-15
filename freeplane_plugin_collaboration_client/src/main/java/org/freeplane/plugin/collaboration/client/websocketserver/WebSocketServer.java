package org.freeplane.plugin.collaboration.client.websocketserver;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.freeplane.collaboration.event.json.Jackson;
import org.freeplane.collaboration.event.messages.ImmutableMapId;
import org.freeplane.collaboration.event.messages.MapCreateRequested;
import org.freeplane.collaboration.event.messages.MapCreated;
import org.freeplane.collaboration.event.messages.MapId;
import org.freeplane.collaboration.event.messages.MapUpdateProcessed.UpdateStatus;
import org.freeplane.collaboration.event.messages.MapUpdateRequested;
import org.freeplane.collaboration.event.messages.Message;
import org.freeplane.collaboration.event.messages.UpdateBlockCompleted;
import org.freeplane.core.util.LogUtils;
import org.freeplane.plugin.collaboration.client.server.Server;
import org.freeplane.plugin.collaboration.client.server.Subscription;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WebSocketServer implements Server{

	BasicClientEndpoint basicClientEndpoint;
	private final ObjectMapper objectMapper;
	final static String SERVER = "ws://localhost:8080/freeplane";
	
	CompletableFuture<MapId> requestedMapIdCompletableFuture;

	public WebSocketServer()
	{
		objectMapper = Jackson.objectMapper;
		connectToFreeplaneServer();
	}

	/*
	 * - client connects
	 * - client subscribes [and authenticates]
	 * - client sends update, receives status,
	 * - [client merges changes from others]
	 */

	private Consumer<UpdateBlockCompleted> consumer;

	@Override
	public CompletableFuture<MapId> createNewMap(MapCreateRequested request) {
		LogUtils.warn("WebSocketServer.createNewMap()");
		requestedMapIdCompletableFuture = new CompletableFuture<MapId>();
		try {
			basicClientEndpoint.sendMessage(objectMapper.writeValueAsString(request));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return requestedMapIdCompletableFuture;
	}

	@Override
	public CompletableFuture<UpdateStatus> update(MapUpdateRequested request) {
		/*
		final CompletableFuture<UpdateStatus> completableFuture = new CompletableFuture<>();
		try {
			String json = objectMapper.writeValueAsString(request.update());
			basicClientEndpoint.sendMessage(json);
			// call when MapUpdateAccepted comes with requestId() == request.messageId()
			completableFuture.complete(UpdateStatus.ACCEPTED);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			completableFuture.complete(UpdateStatus.REJECTED);
		}
		return completableFuture;
		*/
		return new CompletableFuture<UpdateStatus>();
	}

	// needed for receiving msgs from server!
	@Override
	public void subscribe(Subscription subscription) {
		consumer = subscription.consumer();
	}

	private void connectToFreeplaneServer() {
		try {
			if(basicClientEndpoint == null) {
				basicClientEndpoint = new BasicClientEndpoint();

				WebSocketContainer container =
						javax.websocket.ContainerProvider.getWebSocketContainer();
				container.connectToServer(basicClientEndpoint, new URI(SERVER));
			}
		} catch (URISyntaxException | DeploymentException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void unsubscribe(Subscription subscription) {
		consumer = null;
	}

	// TODO: use Encoder/Decoder to automatically convert JSON
	@ClientEndpoint
	public class BasicClientEndpoint
	{
		Session userSession = null;

	    @OnOpen
	    public void onOpen(Session userSession) {
	    	LogUtils.info("opening websocket");
	        this.userSession = userSession;
	    }

	    @OnClose
	    public void onClose(Session userSession, CloseReason reason) {
	    	LogUtils.info("closing websocket");
	        this.userSession = null;
	    }

	    /**
	     * Message is received!
	     *
	     * @param message
	     */
	    @OnMessage
	    public void onMessage(String message) {
	    	try {
				LogUtils.warn("Msg received from server: " + message);
				Message msg = objectMapper.readValue(message, Message.class);
				LogUtils.warn("Msg parsed: " + msg);
				if (msg instanceof MapCreated)
				{
					LogUtils.warn("MapCreated received!");
					MapCreated msgMapCreated = (MapCreated)msg;
					requestedMapIdCompletableFuture.complete(msgMapCreated.id());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }

	    public void sendMessage(String message) {
	        this.userSession.getAsyncRemote().sendText(message);
	    }
	}

}
