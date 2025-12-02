package org.slimecraft.mobswap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.slimecraft.bedrock.annotation.plugin.Plugin;
import org.slimecraft.bedrock.event.EventNode;
import org.slimecraft.bedrock.task.Task;
import org.slimecraft.bedrock.util.Ticks;
import org.slimecraft.funmands.paper.PaperFunmandsManager;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

@Plugin("mob-swap")
public class MobSwapPlugin extends JavaPlugin {
    private static final List<EntityType> TYPE_LIST = new ArrayList<>();
    private static final Random RANDOM = new Random();
    public static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    public static final Map<UUID, EntityType> PLAYERS_ENTITY_TO_KILL = new HashMap<>();

    static {
        TYPE_LIST.add(EntityType.ALLAY);
        TYPE_LIST.add(EntityType.ARMADILLO);
        TYPE_LIST.add(EntityType.AXOLOTL);
        TYPE_LIST.add(EntityType.BAT);
        TYPE_LIST.add(EntityType.CAMEL);
        TYPE_LIST.add(EntityType.CAT);
        TYPE_LIST.add(EntityType.CHICKEN);
        TYPE_LIST.add(EntityType.COD);
        TYPE_LIST.add(EntityType.COPPER_GOLEM);
        TYPE_LIST.add(EntityType.COW);
        TYPE_LIST.add(EntityType.DONKEY);
        TYPE_LIST.add(EntityType.FROG);
        TYPE_LIST.add(EntityType.GLOW_SQUID);
        TYPE_LIST.add(EntityType.HAPPY_GHAST);
        TYPE_LIST.add(EntityType.HORSE);
        TYPE_LIST.add(EntityType.MOOSHROOM);
        TYPE_LIST.add(EntityType.MULE);
        TYPE_LIST.add(EntityType.OCELOT);
        TYPE_LIST.add(EntityType.PARROT);
        TYPE_LIST.add(EntityType.PIG);
        TYPE_LIST.add(EntityType.RABBIT);
        TYPE_LIST.add(EntityType.SALMON);
        TYPE_LIST.add(EntityType.SHEEP);
        TYPE_LIST.add(EntityType.SNIFFER);
        TYPE_LIST.add(EntityType.SNOW_GOLEM);
        TYPE_LIST.add(EntityType.SQUID);
        TYPE_LIST.add(EntityType.STRIDER);
        TYPE_LIST.add(EntityType.TADPOLE);
        TYPE_LIST.add(EntityType.TROPICAL_FISH);
        TYPE_LIST.add(EntityType.TURTLE);
        TYPE_LIST.add(EntityType.VILLAGER);
        TYPE_LIST.add(EntityType.WANDERING_TRADER);

        TYPE_LIST.add(EntityType.SKELETON_HORSE);
        TYPE_LIST.add(EntityType.ZOMBIE_HORSE);

        TYPE_LIST.add(EntityType.BEE);
        TYPE_LIST.add(EntityType.DOLPHIN);
        TYPE_LIST.add(EntityType.FOX);
        TYPE_LIST.add(EntityType.GOAT);
        TYPE_LIST.add(EntityType.IRON_GOLEM);
        TYPE_LIST.add(EntityType.LLAMA);
        TYPE_LIST.add(EntityType.PANDA);
        TYPE_LIST.add(EntityType.POLAR_BEAR);
        TYPE_LIST.add(EntityType.PUFFERFISH);
        TYPE_LIST.add(EntityType.TRADER_LLAMA);
        TYPE_LIST.add(EntityType.WOLF);

        TYPE_LIST.add(EntityType.CAVE_SPIDER);
        TYPE_LIST.add(EntityType.DROWNED);
        TYPE_LIST.add(EntityType.ENDERMAN);
        TYPE_LIST.add(EntityType.PIGLIN);
        TYPE_LIST.add(EntityType.SPIDER);
        TYPE_LIST.add(EntityType.ZOMBIFIED_PIGLIN);

        TYPE_LIST.add(EntityType.BLAZE);
        TYPE_LIST.add(EntityType.BOGGED);
        TYPE_LIST.add(EntityType.BREEZE);
        TYPE_LIST.add(EntityType.CREAKING);
        TYPE_LIST.add(EntityType.CREEPER);
        TYPE_LIST.add(EntityType.ELDER_GUARDIAN);
        TYPE_LIST.add(EntityType.ENDERMITE);
        TYPE_LIST.add(EntityType.EVOKER);
        TYPE_LIST.add(EntityType.GHAST);
        TYPE_LIST.add(EntityType.GUARDIAN);
        TYPE_LIST.add(EntityType.HOGLIN);
        TYPE_LIST.add(EntityType.HUSK);
        TYPE_LIST.add(EntityType.MAGMA_CUBE);
        TYPE_LIST.add(EntityType.PHANTOM);
        TYPE_LIST.add(EntityType.PIGLIN_BRUTE);
        TYPE_LIST.add(EntityType.PILLAGER);
        TYPE_LIST.add(EntityType.RAVAGER);
        TYPE_LIST.add(EntityType.SHULKER);
        TYPE_LIST.add(EntityType.SILVERFISH);
        TYPE_LIST.add(EntityType.SKELETON);
        TYPE_LIST.add(EntityType.SLIME);
        TYPE_LIST.add(EntityType.STRAY);
        TYPE_LIST.add(EntityType.VEX);
        TYPE_LIST.add(EntityType.VINDICATOR);
        TYPE_LIST.add(EntityType.WARDEN);
        TYPE_LIST.add(EntityType.WITCH);
        TYPE_LIST.add(EntityType.WITHER_SKELETON);
        TYPE_LIST.add(EntityType.ZOGLIN);
        TYPE_LIST.add(EntityType.ZOMBIE);
        TYPE_LIST.add(EntityType.ZOMBIE_VILLAGER);

    }

    @Override
    public void onEnable() {
        final PaperFunmandsManager commandManager = new PaperFunmandsManager(getLifecycleManager());
        commandManager.registerCommand(new MobSwapCommand());
        EventNode.global().addListener(EntityDamageByEntityEvent.class, event -> {
            if (!(event.getDamager() instanceof final Player player))
                return;
            if (!(event.getEntity() instanceof final LivingEntity livingEntity))
                return;
            if (livingEntity.getHealth() - event.getFinalDamage() > 0)
                return;
            final UUID id = player.getUniqueId();
            final EntityType type = livingEntity.getType();
            if (PLAYERS_ENTITY_TO_KILL.get(id) != type)
                return;
            PLAYERS_ENTITY_TO_KILL.remove(id);
            Bukkit.getOnlinePlayers().forEach(online -> {
                online.sendMessage(MINI_MESSAGE.deserialize("<green><player> has killed their mob!", TagResolver.resolver("player", Tag.selfClosingInserting(player.displayName()))));
            });
            if (!PLAYERS_ENTITY_TO_KILL.isEmpty())
                return;
            assignTasksToPlayers();
            MobSwapCommand.doCountdownTask();
        });
    }

    public static void assignTasksToPlayers() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            final EntityType randomType = TYPE_LIST.get(RANDOM.nextInt(TYPE_LIST.size()));
            PLAYERS_ENTITY_TO_KILL.put(player.getUniqueId(), randomType);
            player.sendMessage(MobSwapPlugin.MINI_MESSAGE.deserialize("<red>Your mob is: <mob>",
                    TagResolver.resolver("mob", Tag.selfClosingInserting(
                            Component.text(randomType.key().asMinimalString())))));
        });
    }
}
