"""
Remap spiderman.png using corrected Steve UVs.

Source: D:/james/downloads/spiderman.png (64x64) with UV layout per spiderman.bbmodel
Target: 64x64 Steve player skin with UV layout per PlayerModelTrue.bbmodel
"""
import json
from PIL import Image

SRC_BBMODEL = 'D:/james/downloads/spiderman.bbmodel'
DST_BBMODEL = 'D:/james/downloads/PlayerModelTrue.bbmodel'
SRC_TEXTURE = 'D:/james/downloads/spiderman.png'
DST_TEXTURE = 'E:/IdeaProjects/superhero/src/main/resources/assets/timeless/textures/suit/spiderman.png'

with open(SRC_BBMODEL, 'r', encoding='utf-8') as f:
    src_model = json.load(f)
with open(DST_BBMODEL, 'r', encoding='utf-8') as f:
    dst_model = json.load(f)

src_img = Image.open(SRC_TEXTURE).convert('RGBA')
src_tex_w, src_tex_h = src_img.size

src_uv_w = src_model['resolution']['width']
src_uv_h = src_model['resolution']['height']
dst_uv_w = dst_model['resolution']['width']
dst_uv_h = dst_model['resolution']['height']

dst_tex_w = dst_uv_w
dst_tex_h = dst_uv_h
dst_img = Image.new('RGBA', (dst_tex_w, dst_tex_h), (0, 0, 0, 0))

src_elements_by_name = {}
for e in src_model['elements']:
    src_elements_by_name.setdefault(e['name'], e)

FACE_NAMES = ['north', 'south', 'east', 'west', 'up', 'down']


def copy_face(src_uv, dst_uv):
    sx1, sy1, sx2, sy2 = src_uv
    dx1, dy1, dx2, dy2 = dst_uv

    sx1p = sx1 * src_tex_w / src_uv_w
    sy1p = sy1 * src_tex_h / src_uv_h
    sx2p = sx2 * src_tex_w / src_uv_w
    sy2p = sy2 * src_tex_h / src_uv_h

    dx1p = dx1 * dst_tex_w / dst_uv_w
    dy1p = dy1 * dst_tex_h / dst_uv_h
    dx2p = dx2 * dst_tex_w / dst_uv_w
    dy2p = dy2 * dst_tex_h / dst_uv_h

    src_flip_x = sx1p > sx2p
    src_flip_y = sy1p > sy2p
    dst_flip_x = dx1p > dx2p
    dst_flip_y = dy1p > dy2p

    sl, sr = sorted([sx1p, sx2p])
    st, sb = sorted([sy1p, sy2p])
    dl, dr = sorted([dx1p, dx2p])
    dt, db = sorted([dy1p, dy2p])

    sw = int(round(sr - sl))
    sh = int(round(sb - st))
    dw = int(round(dr - dl))
    dh = int(round(db - dt))

    if sw <= 0 or sh <= 0 or dw <= 0 or dh <= 0:
        return

    sli = int(round(sl))
    sti = int(round(st))
    rect = src_img.crop((sli, sti, sli + sw, sti + sh))

    if src_flip_x:
        rect = rect.transpose(Image.FLIP_LEFT_RIGHT)
    if src_flip_y:
        rect = rect.transpose(Image.FLIP_TOP_BOTTOM)

    rect = rect.resize((dw, dh), Image.NEAREST)

    if dst_flip_x:
        rect = rect.transpose(Image.FLIP_LEFT_RIGHT)
    if dst_flip_y:
        rect = rect.transpose(Image.FLIP_TOP_BOTTOM)

    dst_img.paste(rect, (int(round(dl)), int(round(dt))))


for dst_el in dst_model['elements']:
    name = dst_el['name']
    src_el = src_elements_by_name.get(name)
    if not src_el:
        print(f'skip: no source element "{name}"')
        continue
    src_faces = src_el.get('faces', {})
    dst_faces = dst_el.get('faces', {})
    for face in FACE_NAMES:
        if face not in src_faces or face not in dst_faces:
            continue
        copy_face(src_faces[face]['uv'], dst_faces[face]['uv'])

dst_img.save(DST_TEXTURE)
print(f'wrote {DST_TEXTURE} ({dst_img.size[0]}x{dst_img.size[1]})')
