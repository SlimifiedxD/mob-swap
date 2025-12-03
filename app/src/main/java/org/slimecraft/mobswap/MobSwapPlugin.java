package org.slimecraft.mobswap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.slimecraft.bedrock.annotation.plugin.Plugin;
import org.slimecraft.bedrock.event.EventNode;
import org.slimecraft.funmands.paper.PaperFunmandsManager;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

@Plugin("mob-swap")
public class MobSwapPlugin extends JavaPlugin {
    private static final Random RANDOM = new Random();
    public static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    public static final Map<UUID, EntityType> PLAYERS_ENTITY_TO_KILL = new HashMap<>();
    private static final Map<UUID, List<EntityType>> AVAILABLE_ENTITIES = new HashMap<>();

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
            final UUID id = player.getUniqueId();
            final List<EntityType> typeList = AVAILABLE_ENTITIES.getOrDefault(id, createTypeList());
            final EntityType randomType = typeList.get(RANDOM.nextInt(typeList.size()));
            typeList.remove(randomType);
            PLAYERS_ENTITY_TO_KILL.put(id, randomType);
            player.sendMessage(MobSwapPlugin.MINI_MESSAGE.deserialize("<red>Your mob is: <mob>",
                    TagResolver.resolver("mob", Tag.selfClosingInserting(
                            Component.text(randomType.key().asMinimalString())))));
        });
    }

    private static List<EntityType> createTypeList() {
        final List<EntityType> typeList = new ArrayList<>();
        typeList.add(EntityType.ALLAY);
        typeList.add(EntityType.ARMADILLO);
        typeList.add(EntityType.AXOLOTL);
        typeList.add(EntityType.BAT);
        typeList.add(EntityType.CAMEL);
        typeList.add(EntityType.CAT);
        typeList.add(EntityType.CHICKEN);
        typeList.add(EntityType.COD);
        typeList.add(EntityType.COPPER_GOLEM);
        typeList.add(EntityType.COW);
        typeList.add(EntityType.DONKEY);
        typeList.add(EntityType.FROG);
        typeList.add(EntityType.GLOW_SQUID);
        typeList.add(EntityType.HAPPY_GHAST);
        typeList.add(EntityType.HORSE);
        typeList.add(EntityType.MOOSHROOM);
        typeList.add(EntityType.MULE);
        typeList.add(EntityType.OCELOT);
        typeList.add(EntityType.PARROT);
        typeList.add(EntityType.PIG);
        typeList.add(EntityType.RABBIT);
        typeList.add(EntityType.SALMON);
        typeList.add(EntityType.SHEEP);
        typeList.add(EntityType.SNIFFER);
        typeList.add(EntityType.SNOW_GOLEM);
        typeList.add(EntityType.SQUID);
        typeList.add(EntityType.STRIDER);
        typeList.add(EntityType.TADPOLE);
        typeList.add(EntityType.TROPICAL_FISH);
        typeList.add(EntityType.TURTLE);
        typeList.add(EntityType.VILLAGER);
        typeList.add(EntityType.WANDERING_TRADER);

        typeList.add(EntityType.SKELETON_HORSE);
        typeList.add(EntityType.ZOMBIE_HORSE);

        typeList.add(EntityType.BEE);
        typeList.add(EntityType.DOLPHIN);
        typeList.add(EntityType.FOX);
        typeList.add(EntityType.GOAT);
        typeList.add(EntityType.IRON_GOLEM);
        typeList.add(EntityType.LLAMA);
        typeList.add(EntityType.PANDA);
        typeList.add(EntityType.POLAR_BEAR);
        typeList.add(EntityType.PUFFERFISH);
        typeList.add(EntityType.TRADER_LLAMA);
        typeList.add(EntityType.WOLF);

        typeList.add(EntityType.CAVE_SPIDER);
        typeList.add(EntityType.DROWNED);
        typeList.add(EntityType.ENDERMAN);
        typeList.add(EntityType.PIGLIN);
        typeList.add(EntityType.SPIDER);
        typeList.add(EntityType.ZOMBIFIED_PIGLIN);

        typeList.add(EntityType.BLAZE);
        typeList.add(EntityType.BOGGED);
        typeList.add(EntityType.BREEZE);
        typeList.add(EntityType.CREAKING);
        typeList.add(EntityType.CREEPER);
        typeList.add(EntityType.ELDER_GUARDIAN);
        typeList.add(EntityType.ENDERMITE);
        typeList.add(EntityType.EVOKER);
        typeList.add(EntityType.GHAST);
        typeList.add(EntityType.GUARDIAN);
        typeList.add(EntityType.HOGLIN);
        typeList.add(EntityType.HUSK);
        typeList.add(EntityType.MAGMA_CUBE);
        typeList.add(EntityType.PHANTOM);
        typeList.add(EntityType.PIGLIN_BRUTE);
        typeList.add(EntityType.PILLAGER);
        typeList.add(EntityType.RAVAGER);
        typeList.add(EntityType.SHULKER);
        typeList.add(EntityType.SILVERFISH);
        typeList.add(EntityType.SKELETON);
        typeList.add(EntityType.SLIME);
        typeList.add(EntityType.STRAY);
        typeList.add(EntityType.VEX);
        typeList.add(EntityType.VINDICATOR);
        typeList.add(EntityType.WARDEN);
        typeList.add(EntityType.WITCH);
        typeList.add(EntityType.WITHER_SKELETON);
        typeList.add(EntityType.ZOGLIN);
        typeList.add(EntityType.ZOMBIE);
        typeList.add(EntityType.ZOMBIE_VILLAGER);

        return typeList;
    }
}
