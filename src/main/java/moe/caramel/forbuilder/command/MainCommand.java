package moe.caramel.forbuilder.command;

import com.destroystokyo.paper.brigadier.BukkitBrigadierCommandSource;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import moe.caramel.daydream.brigadier.AbstractCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;

public final class MainCommand extends AbstractCommand {

    private static final Component RELOAD_COMPLETE = text().append(
        text(" 설정 > ", RED, BOLD),
        text("리로드가 완료되었습니다.")
    ).build();
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
            this.plugin.reloadConfig();
            context.getSource().getBukkitSender().sendMessage(RELOAD_COMPLETE);
            return 0;
        }));
    }
}
