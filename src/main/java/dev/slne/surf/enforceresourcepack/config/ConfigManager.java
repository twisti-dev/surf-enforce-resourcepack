package dev.slne.surf.enforceresourcepack.config;

import dev.slne.surf.enforceresourcepack.SurfEnforceResourcepack;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import space.arim.dazzleconf.ConfigurationFactory;
import space.arim.dazzleconf.ConfigurationOptions;
import space.arim.dazzleconf.error.ConfigFormatSyntaxException;
import space.arim.dazzleconf.error.InvalidConfigException;
import space.arim.dazzleconf.ext.snakeyaml.CommentMode;
import space.arim.dazzleconf.ext.snakeyaml.SnakeYamlConfigurationFactory;
import space.arim.dazzleconf.ext.snakeyaml.SnakeYamlOptions;
import space.arim.dazzleconf.helper.ConfigurationHelper;

public final class ConfigManager<C> {

  private final ConfigurationHelper<C> configHelper;
  private volatile C configData;

  private ConfigManager(ConfigurationHelper<C> configHelper) {
    this.configHelper = configHelper;
  }

  public static <C> ConfigManager<C> create(Path configFolder, String fileName, Class<C> configClass) {
    SnakeYamlOptions yamlOptions = new SnakeYamlOptions.Builder()
        .commentMode(CommentMode.alternativeWriter())
        .build();
    ConfigurationFactory<C> configFactory = SnakeYamlConfigurationFactory.create(
        configClass,
        ConfigurationOptions.defaults(),
        yamlOptions);
    return new ConfigManager<>(new ConfigurationHelper<>(configFolder, fileName, configFactory));
  }

  public boolean reloadConfig() {
    try {
      configData = configHelper.reloadConfigData();
      return true;
    } catch (IOException ex) {
      throw new UncheckedIOException(ex);

    } catch (ConfigFormatSyntaxException e) {
      configData = configHelper.getFactory().loadDefaults();
      SurfEnforceResourcepack.getLogger().error(
          "The yaml syntax in your configuration is invalid. Check your YAML syntax with a tool"
              + " such as https://yaml-online-parser.appspot.com/",
          e
      );
      return false;
    } catch (InvalidConfigException e) {
      configData = configHelper.getFactory().loadDefaults();
      SurfEnforceResourcepack.getLogger().error(
          "One of the values in your configuration is not valid. Check to make sure you have"
              + " specified the right data types.",
          e
      );
      return false;
    }
  }

  public C getConfigData() {
    C configData = this.configData;
    if (configData == null) {
      throw new IllegalStateException("Configuration has not been loaded yet");
    }
    return configData;
  }
}
