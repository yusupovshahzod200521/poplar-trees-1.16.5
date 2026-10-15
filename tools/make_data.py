import json, os

RES = "src/main/resources"
LEAVES = ["red", "orange", "yellow"]

def save(path, data):
    path = os.path.join(RES, path)
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "w") as f:
        json.dump(data, f, indent=2)
        f.write("\n")

save("assets/poplar/models/block/poplar_log.json", {
    "parent": "minecraft:block/cube_column",
    "textures": {"end": "poplar:block/poplar_log_top", "side": "poplar:block/poplar_log"}})
save("assets/poplar/models/block/poplar_planks.json", {
    "parent": "minecraft:block/cube_all",
    "textures": {"all": "poplar:block/poplar_planks"}})
save("assets/poplar/models/block/poplar_sapling.json", {
    "parent": "minecraft:block/cross",
    "textures": {"cross": "poplar:block/poplar_sapling"}})
for c in LEAVES:
    save("assets/poplar/models/block/poplar_leaves_%s.json" % c, {
        "parent": "minecraft:block/leaves",
        "textures": {"all": "poplar:block/poplar_leaves_%s" % c}})

for n in ["poplar_log", "poplar_planks"] + ["poplar_leaves_" + c for c in LEAVES]:
    save("assets/poplar/models/item/%s.json" % n, {"parent": "poplar:block/" + n})
save("assets/poplar/models/item/poplar_sapling.json", {
    "parent": "minecraft:item/generated",
    "textures": {"layer0": "poplar:block/poplar_sapling"}})

save("assets/poplar/lang/en_us.json", {
    "block.poplar.poplar_log": "Poplar Log",
    "block.poplar.poplar_planks": "Poplar Planks",
    "block.poplar.poplar_leaves_red": "Red Poplar Leaves",
    "block.poplar.poplar_leaves_orange": "Orange Poplar Leaves",
    "block.poplar.poplar_leaves_yellow": "Yellow Poplar Leaves",
    "block.poplar.poplar_sapling": "Poplar Sapling"})

save("data/poplar/recipes/poplar_planks.json", {
    "type": "minecraft:crafting_shapeless",
    "ingredients": [{"item": "poplar:poplar_log"}],
    "result": {"item": "poplar:poplar_planks", "count": 4}})
save("data/poplar/recipes/poplar_sapling.json", {
    "type": "minecraft:crafting_shaped",
    "pattern": ["SSS", " O ", " O "],
    "key": {"S": {"item": "minecraft:spruce_sapling"},
            "O": {"item": "minecraft:oak_sapling"}},
    "result": {"item": "poplar:poplar_sapling", "count": 1}})

SURVIVES = {"condition": "minecraft:survives_explosion"}
SHEARS = {"condition": "minecraft:match_tool", "predicate": {"item": "minecraft:shears"}}
SILK = {"condition": "minecraft:match_tool", "predicate": {"enchantments": [
    {"enchantment": "minecraft:silk_touch", "levels": {"min": 1}}]}}

for n in ["poplar_log", "poplar_planks", "poplar_sapling"]:
    save("data/poplar/loot_tables/blocks/%s.json" % n, {
        "type": "minecraft:block",
        "pools": [{"rolls": 1,
                   "entries": [{"type": "minecraft:item", "name": "poplar:" + n}],
                   "conditions": [SURVIVES]}]})

for c in LEAVES:
    n = "poplar_leaves_" + c
    save("data/poplar/loot_tables/blocks/%s.json" % n, {
        "type": "minecraft:block",
        "pools": [
            {"rolls": 1, "entries": [{"type": "minecraft:alternatives", "children": [
                {"type": "minecraft:item",
                 "conditions": [{"condition": "minecraft:alternative", "terms": [SHEARS, SILK]}],
                 "name": "poplar:" + n},
                {"type": "minecraft:item",
                 "conditions": [SURVIVES, {"condition": "minecraft:table_bonus",
                     "enchantment": "minecraft:fortune",
                     "chances": [0.05, 0.0625, 0.083333336, 0.1]}],
                 "name": "poplar:poplar_sapling"}]}]},
            {"rolls": 1,
             "conditions": [
                 {"condition": "minecraft:inverted",
                  "term": {"condition": "minecraft:alternative", "terms": [SHEARS, SILK]}},
                 {"condition": "minecraft:table_bonus", "enchantment": "minecraft:fortune",
                  "chances": [0.02, 0.022222223, 0.025, 0.033333335, 0.1]}],
             "entries": [{"type": "minecraft:item",
                "functions": [
                    {"function": "minecraft:set_count",
                     "count": {"type": "minecraft:uniform", "min": 1.0, "max": 2.0}},
                    {"function": "minecraft:explosion_decay"}],
                "name": "minecraft:stick"}]}]})

TAGS = {
    "leaves": ["poplar:poplar_leaves_" + c for c in LEAVES],
    "logs": ["poplar:poplar_log"],
    "logs_that_burn": ["poplar:poplar_log"],
    "planks": ["poplar:poplar_planks"],
    "saplings": ["poplar:poplar_sapling"],
}
for kind in ["blocks", "items"]:
    for name, vals in TAGS.items():
        save("data/minecraft/tags/%s/%s.json" % (kind, name), {"replace": False, "values": vals})

print("data ok")
