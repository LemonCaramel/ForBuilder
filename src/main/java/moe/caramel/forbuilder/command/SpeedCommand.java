package moe.caramel.forbuilder.command;

import com.destroystokyo.paper.brigadier.BukkitBrigadierCommandSource;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import moe.caramel.daydream.brigadier.AbstractCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.mojang.brigadier.arguments.FloatArgumentType.floatArg;
import static com.mojang.brigadier.arguments.FloatArgumentType.getFloat;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;

public final class SpeedCommand extends AbstractCommand {

    private static final Component TITLE = text(" 속도 > ", RED, BOLD);
    private static final float DEFAULT_WALK_SPEED = 0.2f;
    private static final float DEFAULT_FLY_SPEED = 0.1f;
    private static final String ARGUMENT_SPEED = "speed";

    public SpeedCommand() {
        this.setPermission("caramel.command.speed");
        this.setDescription("Speed command");
    }

    @Override
    public void createCommand(LiteralArgumentBuilder<BukkitBrigadierCommandSource> builder) {
        builder.executes(context -> {
            if (!(context.getSource().getBukkitSender() instanceof Player player)) return -1;
            player.setWalkSpeed(DEFAULT_WALK_SPEED);
            player.setFlySpeed(DEFAULT_FLY_SPEED);
            player.sendMessage(text().append(TITLE, text("비행 및 걷기 속도가 기본 값으로 변경되었습니다.")));
            return 0;
        }).then(this.argument(ARGUMENT_SPEED, floatArg(-1, 1)) // "-1" ~ "1"
            .executes(context -> {
                final var sender = context.getSource().getBukkitSender();
                if (!(sender instanceof Player player)) return -1;
                this.setSpeed(context, player.isFlying());
                return 0;
            }).then(this.literal("walk").executes(context -> {
                this.setSpeed(context, false);
                return 0;
            })).then(this.literal("fly").executes(context -> {
                this.setSpeed(context, true);
                return 0;
        })));
    }

    private void setSpeed(@NotNull CommandContext<BukkitBrigadierCommandSource> context, boolean fly) {
        if (!(context.getSource().getBukkitSender() instanceof Player player)) return;
        final float speed = getFloat(context, ARGUMENT_SPEED);
        if (fly) player.setFlySpeed(speed);
        else player.setWalkSpeed(speed);
        player.sendMessage(text().append(TITLE, text((fly ? "비행" : "걷기") + " 속도가 " + speed + "로 변경되었습니다.")));
    }
}
