"""
Remap SpiderNoir 128x128 texture to a 64x64 player skin using corrected UVs.

Source: D:/james/downloads/spidernoir.png (128x128) with UV layout per SpiderNoir.bbmodel
Target: 64x64 player skin with UV layout per PlayerModelTrue.bbmodel
"""
import json
from PIL import Image

SRC_BBMODEL = 'D:/james/downloads/SpiderNoir.bbmodel'
DST_BBMODEL = 'D:/james/downloads/PlayerModelTrue.bbmodel'
SRC_TEXTURE = 'D:/james/downloads/spidernoir.png'
DST_TEXTURE = 'E:/IdeaProjects/superhero/src/main/resources/assets/timeless/textures/suit/spiderman_spidernoir.png'

with open(SRC_BBMODEL, 'r', encoding='utf-8') as f:
    src_model = json.load(f)
with open(DST_BBMODEL, 'r', encoding='utf-8') as f:
    dst_model = json.load(f)

src_img = Image.open(SRC_TEXTURE).convert('RGBA')
src_tex_w, src_tex_h = src_img.size

# UV coord space (from resolution)
src_uv_w = src_model['resolution']['width']
src_uv_h = src_model['resolution']['height']
dst_uv_w = dst_model['resolution']['width']
dst_uv_h = dst_model['resolution']['height']

# Output dimensions: 64x128 — top 64x64 is the standard player skin, bottom 64x64 is fedora UVs.
# UV conversion in copy_face uses dst_uv_w/dst_uv_h (=64) so player-skin coords land in the top half.
dst_tex_w = dst_uv_w
dst_tex_h = dst_uv_h
dst_img = Image.new('RGBA', (dst_tex_w, dst_tex_h * 2), (0, 0, 0, 0))

src_elements_by_name = {}
for e in src_model['elements']:
    src_elements_by_name.setdefault(e['name'], e)

FACE_NAMES = ['north', 'south', 'east', 'west', 'up', 'down']

# Override the destination arm UVs to Alex (3-wide arm) layout. PlayerModelTrue is
# Steve (4-wide); we keep its head/body/legs but use Alex arm coords so the output
# matches AlexSuitModel.
ALEX_ARM_UVS = {
    'Right Arm': {
        'north': [44, 20, 47, 32],
        'east':  [40, 20, 44, 32],
        'south': [51, 20, 54, 32],
        'west':  [47, 20, 51, 32],
        'up':    [47, 20, 44, 16],
        'down':  [50, 16, 47, 20],
    },
    'Right Arm Layer': {
        'north': [44, 36, 47, 48],
        'east':  [40, 36, 44, 48],
        'south': [51, 36, 54, 48],
        'west':  [47, 36, 51, 48],
        'up':    [47, 36, 44, 32],
        'down':  [50, 32, 47, 36],
    },
    'Left Arm': {
        'north': [36, 52, 39, 64],
        'east':  [32, 52, 36, 64],
        'south': [43, 52, 46, 64],
        'west':  [39, 52, 43, 64],
        'up':    [39, 52, 36, 48],
        'down':  [42, 48, 39, 52],
    },
    'Left Arm Layer': {
        'north': [52, 52, 55, 64],
        'east':  [48, 52, 52, 64],
        'south': [59, 52, 62, 64],
        'west':  [55, 52, 59, 64],
        'up':    [55, 52, 52, 48],
        'down':  [58, 48, 55, 52],
    },
}


def copy_face(src_uv, dst_uv):
    sx1, sy1, sx2, sy2 = src_uv
    dx1, dy1, dx2, dy2 = dst_uv

    # UV coords -> texture pixel coords
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

    # Normalize source so that "corner1 logical" sits at top-left
    if src_flip_x:
        rect = rect.transpose(Image.FLIP_LEFT_RIGHT)
    if src_flip_y:
        rect = rect.transpose(Image.FLIP_TOP_BOTTOM)

    rect = rect.resize((dw, dh), Image.NEAREST)

    # Apply destination orientation
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
    alex_override = ALEX_ARM_UVS.get(name)
    for face in FACE_NAMES:
        if face not in src_faces or face not in dst_faces:
            continue
        dst_uv = alex_override[face] if alex_override and face in alex_override else dst_faces[face]['uv']
        copy_face(src_faces[face]['uv'], dst_uv)


# --- Fedora cubes (custom Spider-Noir geometry, not in PlayerModelTrue) ---
# Source faces are looked up by element uuid in SpiderNoir.bbmodel; destination UVs are
# laid out in the bottom half of the 64x128 output (V >= 64) using standard box_uv ordering.
def box_uv_faces(uoff, voff, w, h, d):
    return {
        'up':    [uoff + d + w, voff + d, uoff + d, voff],
        'down':  [uoff + d + w + w, voff, uoff + d + w, voff + d],
        'east':  [uoff, voff + d, uoff + d, voff + d + h],
        'north': [uoff + d, voff + d, uoff + d + w, voff + d + h],
        'west':  [uoff + d + w, voff + d, uoff + d + w + d, voff + d + h],
        'south': [uoff + d + w + d, voff + d, uoff + d + w + d + w, voff + d + h],
    }

FEDORA = [
    # uuid, (W, H, D), (uoff, voff)
    ('fe5c9ef0-6ab1-e6e0-4c5d-849cbcb51b2e', (8, 3, 8),  (0, 64)),  # crown
    ('a1c701d0-7baa-7c71-6509-2472420f169a', (8, 3, 8),  (32, 64)), # crown overlay
    ('42c6d339-27bf-8489-2174-dfe498233e98', (11, 1, 11), (0, 80)), # brim
]

src_elements_by_uuid = {e['uuid']: e for e in src_model['elements']}
for uuid, (w, h, d), (uoff, voff) in FEDORA:
    src_el = src_elements_by_uuid.get(uuid)
    if not src_el:
        print(f'skip fedora {uuid}: no source element')
        continue
    src_faces = src_el.get('faces', {})
    dst_face_uvs = box_uv_faces(uoff, voff, w, h, d)
    for face, dst_uv in dst_face_uvs.items():
        if face not in src_faces:
            continue
        copy_face(src_faces[face]['uv'], dst_uv)

dst_img.save(DST_TEXTURE)
print(f'wrote {DST_TEXTURE} ({dst_img.size[0]}x{dst_img.size[1]})')
