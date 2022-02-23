package io.reisub.unethicalite.utils;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;

import javax.inject.Inject;

@PluginDescriptor(
		name = "Chaos Utils",
		description = "Utilities for Chaos scripts"
)
@Slf4j
@Extension
public class Utils extends Plugin {
	@Inject
	private Client client;

	@Inject
	private ChatMessageManager chatMessageManager;

	@Override
	public void startUp() {
		log.info(this.getName() + " started");
	}

	@Override
	protected void shutDown() {
		log.info(this.getName() + " stopped");
	}

	public void sendGameMessage(String message) {
		String chatMessage = new ChatMessageBuilder()
				.append(ChatColorType.HIGHLIGHT)
				.append(message)
				.build();

		chatMessageManager
				.queue(QueuedMessage.builder()
						.type(ChatMessageType.CONSOLE)
						.runeLiteFormattedMessage(chatMessage)
						.build());
	}
}
