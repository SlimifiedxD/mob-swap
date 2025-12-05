package org.slimecraft.mobswap;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.slimecraft.bedrock.task.Task;
import org.slimecraft.bedrock.util.Ticks;
import org.slimecraft.funmands.paper.PaperCommand;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public class MobSwapCommand extends PaperCommand {
    private static final int MINUTES_PER_SWAP = 1;
    private static Task CURRENT_COUNTDOWN_TASK = null;

    public MobSwapCommand() {
        super("mobswap");
        final AtomicBoolean started = new AtomicBoolean(false);

        addFormat("", ctx -> {
            if (started.get()) {
                ctx.getExecutor()
                        .sendMessage(MobSwapPlugin.MINI_MESSAGE.deserialize("<red>The game has already started!"));
                return;
            }
            started.set(true);
            MobSwapPlugin.assignTasksToPlayers();
            doCountdownTask();
        });
    };

    public static void doCountdownTask() {
        final AtomicInteger countdown = new AtomicInteger(60 * MINUTES_PER_SWAP);
        if (CURRENT_COUNTDOWN_TASK != null) {
            CURRENT_COUNTDOWN_TASK.cancel();
        }
        CURRENT_COUNTDOWN_TASK = Task.builder()
                .repeat(Ticks.seconds(1))
                .whenRan(task -> {
                    final int countdownAmount = countdown.get();
                    final StringBuilder countdownText = new StringBuilder();
                    countdownText.append(countdownAmount / 60).append(":")
                            .append(String.format("%02d", countdownAmount % 60));
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        if (!MobSwapPlugin.PLAYERS_ENTITY_TO_KILL.containsKey(player.getUniqueId()))
                            return;
                        player.sendActionBar(MobSwapPlugin.MINI_MESSAGE.deserialize("<red><time>",
                                TagResolver.resolver("time",
                                        Tag.selfClosingInserting(Component.text(countdownText.toString())))));
                    });
                    if (countdownAmount == 0) {
                        countdown.set(60 * MINUTES_PER_SWAP);
                        Bukkit.getOnlinePlayers().forEach(player -> {
                            final UUID id = player.getUniqueId();
                            if (!MobSwapPlugin.PLAYERS_ENTITY_TO_KILL.containsKey(id))
                                return;
                            for (int i = 0; i < 10; i++) {
                                Bukkit.getOnlinePlayers().forEach(online -> {
                                    online.getWorld().spawnEntity(online.getLocation(), MobSwapPlugin.PLAYERS_ENTITY_TO_KILL.get(id));
                                });
                            }
                            Bukkit.getOnlinePlayers().forEach(notifier -> {
                                notifier.sendMessage(MobSwapPlugin.MINI_MESSAGE
                                        .deserialize("<red><player> has failed to kill the mob in time!",
                                                TagResolver.resolver("player",
                                                        Tag.selfClosingInserting(player.displayName()))));
                            });
                        });
                        MobSwapPlugin.assignTasksToPlayers();
                        return;
                    }
                    countdown.decrementAndGet();
                })
                .run();
    }
}
