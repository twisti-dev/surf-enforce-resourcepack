package dev.slne.surf.enforceresourcepack.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import dev.slne.surf.enforceresourcepack.SurfEnforceResourcepack;
import java.util.function.Consumer;

public interface VelocityCommand {

  LiteralCommandNode<CommandSource> buildCommand(SurfEnforceResourcepack plugin);

  Consumer<CommandMeta.Builder> buildMeta(SurfEnforceResourcepack plugin);
}
