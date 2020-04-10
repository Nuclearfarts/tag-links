# tag-links
adds a way for datapacks to "link" tags, menaing they will share their contents

json files go in `data/whatever_namespace/taglinks/<tag_path>.json` (For a vanilla item tag, this is `data/whatever_namespace/taglinks/tags/items.json`.) They take the format of a 2-dimensional array of strings, where each row is a set of tags to merge: ```
[
  ["minecraft:anvil", "minecraft:planks"]
]```

(This would merge `minecraft:anvil` and `minecraft:planks` to have the same contents.)
