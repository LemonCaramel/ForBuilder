package moe.caramel.forbuilder.command;

import com.destroystokyo.paper.brigadier.BukkitBrigadierCommandSource;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import moe.caramel.daydream.brigadier.AbstractCommand;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public final class MainCommand extends AbstractCommand {

    private final Plugin plugin;

    public MainCommand(@NotNull Plugin plugin) {
        this.plugin = plugin;
        this.setPermission("caramel.command.fb");
        this.setDescription("ForBuilder Main command");
        this.setAliases("fb");
    }

    @Override
    public void createCommand(LiteralArgumentBuilder<BukkitBrigadierCommandSource> builder) {

        // "reload" Sub command
        builder.then(this.literal("reload").executes(context -> {
            plugin.reloadConfig();
            context.getSource().getBukkitSender().sendMessage("§c§l 설정 > §f리로드가 완료되었습니다.");
            return 0;
        }));
    }
}
