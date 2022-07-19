package com.pew.yetanotherskyblockmod.util;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.pew.yetanotherskyblockmod.YASBM;
import com.pew.yetanotherskyblockmod.Features.Feature;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.StringNbtReader;

public class AspectOfTheJerry implements Feature {
	public static final Feature instance = new AspectOfTheJerry(); // Registry substitute

	@Override
	public void init() {
	}

	public String onMessageSent(String msg) {
		if (msg.equals("I am a loser")) {
			//{"display":"{Lore:['{\"text\":\"book lore\"}'],Name:'{\"text\":\"book name\"}'}"}
			try {
				YASBM.client.player.giveItemStack(ItemStack.fromNbt(StringNbtReader.parse("""
				{
					"id": "minecraft:diamond_axe",
					"Count": 1,
					"tag": {
						"ench": [],
						"Unbreakable": 1,
						"HideFlags": 254,
						"display": {
							"Lore": [
								'{\"text\":\"§7Gear Score: §d757 §8(2011)\"}',
								'{\"text\":\"§7Damage: §c+184 §e(+30) §8(+540.6)\"}',
								'{\"text\":\"§7Strength: §c+372.5 §e(+30) §6[+5] §9(+197) §d(+14) §8(+1,147.98)\"}',
								'{\"text\":\"§7Crit Damage: §c+70% §8(+222.6%)\"}',
								'{\"text\":\" §9[§d❁§9] §9[§d⚔§9]\"}',
								'{\"text\":\"\"}',
								'{\"text\":\"§d§l§d§lUltimate Wise V§9, §9Cleave V§9, §9Critical VI\"}',
								'{\"text\":\"§9Cubism V§9, §9Ender Slayer VI§9, §9Execute V\"}',
								'{\"text\":\"§9Experience IV§9, §9Fire Aspect II§9, §9First Strike IV\"}',
								'{\"text\":\"§9Giant Killer VI§9, §9Impaling III§9, §9Lethality VI\"}',
								'{\"text\":\"§9Looting IV§9, §9Luck VI§9, §9Mana Steal III\"}',
								'{\"text\":\"§9Scavenger IV§9, §9Smite VII§9, §9Thunderlord VI\"}',
								'{\"text\":\"§9Vampirism VI§9, §9Venomous V\"}',
								'{\"text\":\"\"}',
								'{\"text\":\"§2◆ Pestilence Rune III\"}',
								'{\"text\":\"\"}',
								'{\"text\":\"§7Heal §c50❤ §7per hit.\"}',
								'{\"text\":\"§7Deal §a+250% §7damage to Zombies§7.\"}',
								'{\"text\":\"§7§7Receive §a25% §7less damage\"}',
								'{\"text\":\"§7from Zombies§7 when held.\"}',
								'{\"text\":\"\"}',
								'{\"text\":\"§6Ability: Throw §e§lRIGHT CLICK\"}',
								'{\"text\":\"§7Throw your axe damaging all\"}',
								'{\"text\":\"§7enemies in its path dealing\"}',
								'{\"text\":\"§7§c10%§7 melee damage.\"}',
								'{\"text\":\"§7Consecutive throws stack §c2x\"}',
								'{\"text\":\"§c§7damage but cost §92x §7mana up\"}',
								'{\"text\":\"§7to 16x\"}',
								'{\"text\":\"§8Mana Cost: §320\"}',
								'{\"text\":\"\"}',
								'{\"text\":\"§fKills: §63,017\"}',
								'{\"text\":\"\"}',
								'{\"text\":\"§9Withered Bonus\"}',
								'{\"text\":\"§7Grants §a+1 §c❁ Strength §7per\"}',
								'{\"text\":\"§7§cCatacombs §7level.\"}',
								'{\"text\":\"\"}',
								'{\"text\":\"§8§l* §8Co-op Soulbound §8§l*\"}',
								'{\"text\":\"§d§l§ka§r §d§l§d§lMYTHIC DUNGEON SWORD §d§l§ka\"}'
							],
							"Name": '{\"text\": \"§dWithered Axe of the Shredded §6✪§6✪§6✪§6✪§6✪\"}'
						},
						"ExtraAttributes": {
							"rarity_upgrades": 1,
							"stats_book": 3017,
							"runes": {
								"ZOMBIE_SLAYER": 3
							},
							"modifier": "withered",
							"art_of_war_count": 1,
							"dungeon_item_level": 5,
							"originTag": "REAPER_SWORD",
							"enchantments": {
								"impaling": 3,
								"luck": 6,
								"critical": 6,
								"cleave": 5,
								"looting": 4,
								"smite": 7,
								"telekinesis": 1,
								"ender_slayer": 6,
								"scavenger": 4,
								"fire_aspect": 2,
								"vampirism": 6,
								"experience": 4,
								"execute": 5,
								"giant_killer": 6,
								"mana_steal": 3,
								"venomous": 5,
								"first_strike": 4,
								"thunderlord": 6,
								"ultimate_wise": 5,
								"cubism": 5,
								"lethality": 6
							},
							"uuid": "fcc7648c-d0a5-495b-8acc-8704482f3245",
							"hot_potato_count": 15,
							"gems": {
								"JASPER_0": "FINE",
								"COMBAT_0": "FINE",
								"COMBAT_0_gem": "JASPER"
							},
							"id": "AXE_OF_THE_SHREDDED",
							"donated_museum": 1,
							"timestamp": "4/23/21 9:38 PM"
						}
					},
					"Damage": 0
				}
				""")));
				return "hee hee hee haw";
			} catch (CommandSyntaxException e) {
				e.printStackTrace();
				return "I am an idiot";
			}
		}
		return msg;
	}
}
