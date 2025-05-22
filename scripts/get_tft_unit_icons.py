import json

import requests


def is_unit(champ) -> bool:
    return bool(champ["traits"]) or champ["name"] in {"Hextech Forge", "Sion"}


with open("cdragon_data/en_us.json", encoding="utf-8") as f:
    data = json.load(f)

champions_data: list = data["sets"]["13"]["champions"]

units = list(filter(lambda champ: is_unit(champ), champions_data))

unit_icon_paths = list(
    map(
        lambda champ: {
            "iconPath": champ["tileIcon"].lower().replace(".tex", ".png"),
            "iconName": champ["apiName"] + ".png",
        },
        units,
    )
)

for unit in unit_icon_paths:
    url = f"https://raw.communitydragon.org/15.6/game/{unit["iconPath"]}"

    res = requests.get(url)

    if not res.ok:
        print(url, "failed")
        continue

    with open(f"src/main/resources/assets/units/icons/{unit["iconName"]}", "wb") as f:
        f.write(res.content)
