package com.example.telegramlogger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class TelegramLoggerPlugin extends JavaPlugin implements Listener {

    private TelegramNotifier notifier;
    private boolean logChat;
    private boolean logCommands;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        FileConfiguration config = getConfig();

        String token = config.getString("telegram.bot-token");
        String chatId = config.getString("telegram.chat-id");
        logChat = config.getBoolean("telegram.log-chat", true);
        logCommands = config.getBoolean("telegram.log-commands", true);

        if (token == null || chatId == null) {
            getLogger().warning("Telegram bot token or chat ID not set.");
            return;
        }

        notifier = new TelegramNotifier(token, chatId);
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("TelegramLogger enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("TelegramLogger disabled.");
    }

    @EventHandler
    public void onChat(PlayerChatEvent event) {
        if (!logChat) return;
        String message = "[CHAT] " + event.getPlayer().getName() + ": " + event.getMessage();
        notifier.sendMessage(message);
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (!logCommands) return;
        String message = "[CMD] " + event.getPlayer().getName() + ": " + event.getMessage();
        notifier.sendMessage(message);
    }
}
