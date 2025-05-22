import os
import pathlib


def rename(file_name: str) -> str:
    return f"TFT13_{file_name.split("_")[1].split(".")[0]}.png"


root = pathlib.Path.cwd()

covers_path = os.path.join(
    root, "src", "main", "resources", "assets", "champions", "covers"
)

for file_name in os.listdir(covers_path):
    os.rename(
        os.path.join(covers_path, file_name),
        os.path.join(covers_path, rename(file_name)),
    )
