package moe.caramel.forbuilder.command;

import com.destroystokyo.paper.brigadier.BukkitBrigadierCommandSource;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import moe.caramel.daydream.brigadier.AbstractCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.word;
import static moe.caramel.daydream.brigadier.Arguments.DIMENSION;
import static moe.caramel.forbuilder.Main.WORLD_LIST;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;

public final class WorldCommand extends AbstractCommand {

    private static final Component TITLE = text(" 월드 > ", RED, BOLD);
    private static final String ARGUMENT_WORLD = "world";
    private final Plugin plugin;

    public WorldCommand(@NotNull Plugin plugin) {
        this.plugin = plugin;
        this.setPermission("caramel.command.world");
        this.setDescription("World command");
    }

    @Override
    public void createCommand(LiteralArgumentBuilder<BukkitBrigadierCommandSource> builder) {

        // "create" Sub command
        builder.then(this.literal("create").then(this.argument(ARGUMENT_WORLD, word()).executes(context -> {
            final var source = context.getSource().getBukkitSender();
            final var name = getString(context, ARGUMENT_WORLD);
            // Check
            if (Bukkit.getWorld(name) != null) {
                source.sendMessage(text().append(TITLE, text("이미 해당 이름의 세계가 존재합니다.")));
                return -1;
            }

            source.sendMessage(text().append(TITLE, text("\"" + name + "\" 세계가 생성 중입니다.")));

            // Creator
            final var creator = new WorldCreator(name);
            creator.type(WorldType.FLAT);
            creator.generateStructures(false);
            creator.environment(World.Environment.NORMAL);
            creator.generatorSettings("{\"structures\":{\"structures\":{}},\"layers\":[{\"block\":\"air\",\"height\":1}],\"biome\":\"minecraft:the_void\"}");

            // World
            final var world = creator.createWorld();
            if (world == null) return -1;
            final var list = plugin.getConfig().getStringList(WORLD_LIST);
            list.add(name);
            plugin.getConfig().set(WORLD_LIST, list);
            plugin.saveConfig();

            world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            world.setGameRule(GameRule.DO_FIRE_TICK, false);
            world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
            world.setGameRule(GameRule.RANDOM_TICK_SPEED, 0);
            world.setType(new Location(world, 0, 0, 0), Material.GLASS);
            world.setType(new Location(world, -1, 0, 0), Material.GLASS);
            world.setType(new Location(world, -1, 0, -1), Material.GLASS);
            world.setType(new Location(world, 0, 0, -1), Material.GLASS);
            world.setSpawnLocation(new Location(world, 0, 1, 0));

            final var border = world.getWorldBorder();
            border.setCenter(0.0D, 0.0D);
            border.setSize(500);

            // Etc
            source.sendMessage(text().append(TITLE, text("세계 생성이 완료되었습니다.")));
            if (source instanceof Player player) player.teleport(world.getSpawnLocation());
            return 0;
        })));

        // "move" Sub command
        builder.then(this.literal("move")
               .then(this.argument(ARGUMENT_WORLD, DIMENSION.get())
               .suggests(DIMENSION.getSuggestion())
               .executes(context -> {
                   final var world = DIMENSION.getData(context, ARGUMENT_WORLD);
                   final var source = context.getSource().getBukkitSender();
                   if (source instanceof Player player) {
                       player.teleport(world.getSpawnLocation());
                       player.sendMessage(text().append(TITLE, text("이동 되었습니다!")));
                   } else source.sendMessage(text().append(TITLE, text("당신은 플레이어가 아닙니다!")));
                   return 0;
               })
       ));
    }
}
