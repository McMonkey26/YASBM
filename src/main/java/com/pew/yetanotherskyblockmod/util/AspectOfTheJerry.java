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
		if (!YASBM.client.isInSingleplayer()) return msg;
		switch (msg) {
			case "I am a loser":
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
			case "meow":
				try {
					YASBM.client.player.giveItemStack(ItemStack.fromNbt(StringNbtReader.parse("""
					{
						"id": "minecraft:player_head",
						"Count": 1,
						"tag": {
							"HideFlags": 254,
							"SkullOwner": {
							"Id": "7e3ed445-3545-3c76-993b-8f292ea576c6",
							"Properties": {
								"textures": [{
									"Value": {
										"textures": {
											"SKIN": {
												"url": "http://textures.minecraft.net/texture/38ff473bd52b4db2c06f1ac87fe1367bce7574fac330ffac7956229f82efba1"
											}
										}
									}
								}]
							}
						},
						"display": {
							"Lore": [
								'{\"text\": \"§8Foraging Pet\"}',
								'{\"text\": \"\"}',
								'{\"text\": \"§7Ferocity: §a+4\"}',
								'{\"text\": \"§7Strength: §a+80\"}',
								'{\"text\": \"§7Speed: §a+22\"}',
								'{\"text\": \"\"}',
								'{\"text\": \"§6Primal Force\"}',
								'{\"text\": \"§7§7Adds §c+13.35 §c❁ Damage §7and\"}',
								'{\"text\": \"§7§c+13.35 §c❁ Strength §7to your\"}',
								'{\"text\": \"§7weapons.\"}',
								'{\"text\": \"\"}',
								'{\"text\": \"§6First Pounce\"}',
								'{\"text\": \"§7§7First Strike§7,\"}',
								'{\"text\": \"§7Triple-Strike§7, and §d§lCombo\"}',
								'{\"text\": \"§d§l§7are §a89% §7more effective.\"}',
								'{\"text\": \"\"}',
								'{\"text\": \"§6Held Item: §5Antique Remedies\"}',
								'{\"text\": \"§7§7Increases the pet0027s §c❁\"}',
								'{\"text\": \"§cStrength §7by §a80%\"}',
								'{\"text\": \"\"}',
								'{\"text\": \"§7§eRight-click to add this pet to\"}',
								'{\"text\": \"§eyour pet menu!\"}',
								'{\"text\": \"\"}',
								'{\"text\": \"§5§lEPIC\"}',
							],
							"Name": '{\"text\": \"§7[Lvl 89] §5Lion\"}'
						},
						"ExtraAttributes": {
							"petInfo": '{\"type\":\"LION\",\"active\":false,\"exp\":8097426.606629732,\"tier\":\"EPIC\",\"hideInfo\":false,\"heldItem\":\"ANTIQUE_REMEDIES\",\"candyUsed\":0,\"uuid\":\"c5e20f76-deef-463f-a544-d8dacb4c507e\",\"hideRightClick\":false}',
							"id": "PET",
							"uuid": "c5e20f76-deef-463f-a544-d8dacb4c507e",
							"timestamp": "7/18/22 11:45 PM"
						}
						},
						"Damage": 3
					}
					""")));
					return "nya ;3";
				} catch (CommandSyntaxException e) {
					e.printStackTrace();
					return "I am an idiot";
				}
			default:
				return msg;
		}
		//{"display":"{Lore:['{\"text\":\"book lore\"}'],Name:'{\"text\":\"book name\"}'}"}
	}
}
