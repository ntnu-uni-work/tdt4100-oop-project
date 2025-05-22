import json

import requests

with open("cdragon_data/en_us.json", encoding="utf-8") as f:
    data = json.load(f)

traits_data: list = data["sets"]["13"]["traits"]

for trait in traits_data:
    url = f"https://raw.communitydragon.org/15.6/game/{trait["icon"].lower().replace(".tex", ".png")}"

    res = requests.get(url)

    if not res.ok:
        print(url, "failed")
        continue

    with open(
        f"src/main/resources/assets/traiticons/{trait["apiName"]}.png", "wb"
    ) as f:
        f.write(res.content)
