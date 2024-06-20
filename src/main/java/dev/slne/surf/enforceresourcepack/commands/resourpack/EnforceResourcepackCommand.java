package dev.slne.surf.enforceresourcepack.commands.resourpack;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandMeta.Builder;
import com.velocitypowered.api.command.CommandSource;
import dev.slne.surf.enforceresourcepack.SurfEnforceResourcepack;
import dev.slne.surf.enforceresourcepack.commands.VelocityCommand;
import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class EnforceResourcepackCommand implements VelocityCommand {
  public static final EnforceResourcepackCommand INSTANCE = new EnforceResourcepackCommand();
  public static final String COMMAND_PERMISSION = "surf.enforceresourcepack.command.enforceresourcepack";

  private EnforceResourcepackCommand() {
  }

  private static final Component PREFIX = Component.text(">> ", NamedTextColor.DARK_GRAY)
      .append(Component.text("SurfEnforceResourcepack", NamedTextColor.AQUA))
      .append(Component.text(" | ", NamedTextColor.DARK_GRAY));

  @Override
  public LiteralCommandNode<CommandSource> buildCommand(SurfEnforceResourcepack plugin) {
    return BrigadierCommand.literalArgumentBuilder("enforceresourcepack")
        .requires(commandSource -> commandSource.hasPermission(COMMAND_PERMISSION))
        .then(BrigadierCommand.literalArgumentBuilder("reload")
            .executes(context -> {
              final boolean success = SurfEnforceResourcepack.getInstance().getConfigManager().reloadConfig();

              if (success) {
                context.getSource().sendMessage(PREFIX.append(Component.text("Config reloaded.", NamedTextColor.GREEN)));
                return 0;
              } else {
                context.getSource().sendMessage(PREFIX.append(Component.text("Failed to reload config.", NamedTextColor.RED)));
                return Command.SINGLE_SUCCESS;
              }
            }))
        .build();
  }

  @Override
  public Consumer<Builder> buildMeta(SurfEnforceResourcepack plugin) {
    return builder -> {
    };
  }
}
