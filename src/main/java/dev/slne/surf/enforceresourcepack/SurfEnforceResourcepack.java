package dev.slne.surf.enforceresourcepack;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.slne.surf.enforceresourcepack.commands.CommandManager;
import dev.slne.surf.enforceresourcepack.config.ConfigManager;
import dev.slne.surf.enforceresourcepack.config.EnforceResourcepackConfig;
import dev.slne.surf.enforceresourcepack.listener.ListenerManager;
import java.nio.file.Path;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.slf4j.Logger;

@Getter
@Plugin(
    id = "surf-enforce-resourcepack",
    name = "surf-enforce-resourcepack",
    version = BuildConstants.VERSION,
    description = "TODO",
    url = "https://server.castcrafter.de/",
    authors = {"Twisti", "Keviro"}
)
public class SurfEnforceResourcepack {

  @Getter
  private static SurfEnforceResourcepack instance;

  @Inject
  private Logger logger;

  @Inject
  private ProxyServer server;
  private final ConfigManager<EnforceResourcepackConfig> configManager;

  @Inject
  public SurfEnforceResourcepack(@DataDirectory Path path) {
    instance = this;
    configManager = ConfigManager.create(path, "config.yml", EnforceResourcepackConfig.class);
    configManager.reloadConfig();
  }

  @Subscribe
  public void onEnable(ProxyInitializeEvent ignoredEvent) {
    logger.info("Enabling SurfEnforceResourcepack");

    ListenerManager.INSTANCE.registerListeners();
    CommandManager.INSTANCE.registerCommands();

    logger.info("Enabled SurfEnforceResourcepack");
  }

  @Contract(pure = true)
  public static ProxyServer getServer() {
    return instance.server;
  }

  @Contract(pure = true)
  public static Logger getLogger() {
    return instance.logger;
  }
}
