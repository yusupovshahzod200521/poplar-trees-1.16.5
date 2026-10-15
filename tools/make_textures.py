import os
import random
from PIL import Image

OUT = "src/main/resources/assets/poplar/textures/block"
os.makedirs(OUT, exist_ok=True)


def clamp(v):
    return max(0, min(255, int(v)))


def shade(c, f):
    return (clamp(c[0] * f), clamp(c[1] * f), clamp(c[2] * f), 255)


BARK = (146, 136, 126)
WOOD = (176, 166, 150)


def make_log_side():
    rnd = random.Random(11)
    img = Image.new("RGBA", (16, 16))
    col_f = [rnd.uniform(0.82, 1.08) for _ in range(16)]
    for x in range(16):
        for y in range(16):
            f = col_f[x] * rnd.uniform(0.94, 1.06)
            if x % 5 == 2 and rnd.random() < 0.7:
                f *= 0.72
            img.putpixel((x, y), shade(BARK, f))
    for _ in range(9):
        x, y = rnd.randrange(16), rnd.randrange(15)
        img.putpixel((x, y), shade(BARK, 0.55))
        img.putpixel((x, y + 1), shade(BARK, 0.62))
    return img


def make_log_top():
    rnd = random.Random(12)
    img = Image.new("RGBA", (16, 16))
    for x in range(16):
        for y in range(16):
            d = max(abs(x - 7.5), abs(y - 7.5))
            if d > 6.5:
                img.putpixel((x, y), shade(BARK, 0.78 * rnd.uniform(0.95, 1.05)))
            else:
                ring = int(d) % 2
                f = (1.0 if ring == 0 else 0.88) * rnd.uniform(0.96, 1.04)
                img.putpixel((x, y), shade(WOOD, f))
    return img


def make_planks():
    rnd = random.Random(13)
    img = Image.new("RGBA", (16, 16))
    for board in range(4):
        base = rnd.uniform(0.92, 1.06)
        for y in range(board * 4, board * 4 + 4):
            for x in range(16):
                f = base * rnd.uniform(0.96, 1.04)
                if y % 4 == 3:
                    f *= 0.78
                img.putpixel((x, y), shade(WOOD, f))
        sx = rnd.randrange(3, 13)
        for y in range(board * 4, board * 4 + 3):
            img.putpixel((sx, y), shade(WOOD, 0.8))
    return img


def make_leaves(base, seed):
    rnd = random.Random(seed)
    img = Image.new("RGBA", (16, 16))
    for x in range(16):
        for y in range(16):
            if rnd.random() < 0.12:
                img.putpixel((x, y), (0, 0, 0, 0))
                continue
            img.putpixel((x, y), shade(base, rnd.uniform(0.72, 1.18)))
    for _ in range(6):
        x, y = rnd.randrange(16), rnd.randrange(16)
        img.putpixel((x, y), shade(base, 1.3))
    return img


def make_sapling():
    img = Image.new("RGBA", (16, 16), (0, 0, 0, 0))
    stem = (112, 96, 80, 255)
    for y in range(7, 16):
        img.putpixel((7, y), stem)
        img.putpixel((8, y), shade(stem, 0.85))
    cols = [(178, 48, 38), (214, 112, 30), (222, 184, 44)]
    rnd = random.Random(14)
    blobs = [(7, 3, 2), (4, 6, 2), (11, 6, 2), (7, 7, 1)]
    for i, (cx, cy, r) in enumerate(blobs):
        c = cols[i % 3]
        for x in range(cx - r, cx + r + 1):
            for y in range(cy - r, cy + r + 1):
                if 0 <= x < 16 and 0 <= y < 16 and (x - cx) ** 2 + (y - cy) ** 2 <= r * r + 1:
                    img.putpixel((x, y), shade(c, rnd.uniform(0.85, 1.12)))
    return img


make_log_side().save(OUT + "/poplar_log.png")
make_log_top().save(OUT + "/poplar_log_top.png")
make_planks().save(OUT + "/poplar_planks.png")
make_leaves((178, 48, 38), 21).save(OUT + "/poplar_leaves_red.png")
make_leaves((214, 112, 30), 22).save(OUT + "/poplar_leaves_orange.png")
make_leaves((222, 184, 44), 23).save(OUT + "/poplar_leaves_yellow.png")
make_sapling().save(OUT + "/poplar_sapling.png")
print("textures ok")
