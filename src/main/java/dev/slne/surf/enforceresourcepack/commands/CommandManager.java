package dev.slne.surf.enforceresourcepack.commands;

import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandMeta;
import dev.slne.surf.enforceresourcepack.SurfEnforceResourcepack;
import dev.slne.surf.enforceresourcepack.commands.resourpack.EnforceResourcepackCommand;
import java.util.function.Consumer;

public final class CommandManager {

  public static final CommandManager INSTANCE = new CommandManager();

  private CommandManager() {
  }

  public void registerCommands() {
    register(EnforceResourcepackCommand.INSTANCE);
  }

  public void register(VelocityCommand command) {
    final SurfEnforceResourcepack plugin = SurfEnforceResourcepack.getInstance();
    com.velocitypowered.api.command.CommandManager commandManager = SurfEnforceResourcepack.getServer()
        .getCommandManager();

    final BrigadierCommand brigadierCommand = new BrigadierCommand(command.buildCommand(plugin));

    final CommandMeta.Builder meta = commandManager.metaBuilder(brigadierCommand).plugin(plugin);
    final Consumer<CommandMeta.Builder> metaBuilder = command.buildMeta(plugin);
    metaBuilder.accept(meta);
    final CommandMeta finalMeta = meta.build();

    commandManager.register(finalMeta, brigadierCommand);
  }
}
