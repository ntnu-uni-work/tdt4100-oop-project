import json


def is_unit(champ) -> bool:
    return bool(champ["traits"]) or champ["name"] in {"Hextech Forge", "Sion"}


with open("cdragon_data/en_us.json", encoding="utf-8") as f:
    data = json.load(f)

champions_data: list = data["sets"]["13"]["champions"]

units = list(filter(lambda champ: is_unit(champ), champions_data))

champions = list(
    map(
        lambda champ: {
            "cost": champ["cost"],
            "name": champ["name"],
            "stats": champ["stats"],
            "traits": champ["traits"],
            "icon": champ["apiName"] + ".png",
        },
        units,
    )
)

with open("src/main/resources/data/champions.json", "w") as f:
    json.dump(champions, f)
