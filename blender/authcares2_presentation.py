"""
AuthCares2 — Blender 5.x Presentation Scene
=============================================
Script para generar modelos 3D de celular y reloj inteligente
con la UI de AuthCares2 como textura en las pantallas.

Compatible con Blender 5.0+

USO:
  1. Abre Blender 5.1+
  2. Ve a Scripting > Open > selecciona este archivo
  3. Ejecuta el script (Alt+P o botón Run)
  4. Renderiza con F12
"""

import bpy
import math
import os
from mathutils import Vector, Euler

# ============================================================
# CONFIGURACIÓN
# ============================================================
# Intentar obtener la ruta del script; si no funciona, usar ruta fija
try:
    SCRIPT_DIR = os.path.dirname(os.path.abspath(bpy.context.space_data.text.filepath))
except Exception:
    SCRIPT_DIR = r"C:\Users\marce\AuthCares2\blender"

PHONE_TEXTURE_PATH = os.path.join(SCRIPT_DIR, "textures", "authcares2_phone_ui.png")
WATCH_TEXTURE_PATH = os.path.join(SCRIPT_DIR, "textures", "authcares2_watch_ui.png")

# Colores AuthCares2 (RGB lineal)
def hex_to_linear(hex_color):
    """Convierte hex a RGB lineal para Blender."""
    r = int(hex_color[1:3], 16) / 255.0
    g = int(hex_color[3:5], 16) / 255.0
    b = int(hex_color[5:7], 16) / 255.0
    def to_linear(c):
        return c / 12.92 if c <= 0.04045 else ((c + 0.055) / 1.055) ** 2.4
    return (to_linear(r), to_linear(g), to_linear(b), 1.0)

AUTHCARES_PRIMARY   = hex_to_linear("#0052CC")
AUTHCARES_SURFACE   = hex_to_linear("#FBFDFD")
AUTHCARES_DARK      = hex_to_linear("#1A1C1E")
WATCH_BAND_BLUE     = hex_to_linear("#0052CC")
PHONE_BLACK         = hex_to_linear("#1A1A1A")


# ============================================================
# UTILIDADES
# ============================================================
def move_to_collection(obj, target_collection):
    """Mueve un objeto a una colección de forma segura (Blender 5.x compatible)."""
    # Quitar de TODAS las colecciones donde esté actualmente
    for col in list(obj.users_collection):
        col.objects.unlink(obj)
    # Agregar a la colección destino
    target_collection.objects.link(obj)


def clean_scene():
    """Limpia toda la escena."""
    # Seleccionar todo y borrar
    for obj in bpy.data.objects:
        bpy.data.objects.remove(obj, do_unlink=True)
    # Limpiar datos huérfanos
    for block in bpy.data.meshes:
        if block.users == 0:
            bpy.data.meshes.remove(block)
    for block in bpy.data.materials:
        if block.users == 0:
            bpy.data.materials.remove(block)
    for block in bpy.data.images:
        if block.users == 0:
            bpy.data.images.remove(block)
    # Limpiar colecciones extra
    for col in list(bpy.data.collections):
        bpy.data.collections.remove(col)


def create_material(name, color, metallic=0.0, roughness=0.5, emission=None):
    """Crea un material PBR básico."""
    mat = bpy.data.materials.new(name=name)
    mat.use_nodes = True
    nodes = mat.node_tree.nodes
    links = mat.node_tree.links
    nodes.clear()

    output = nodes.new('ShaderNodeOutputMaterial')
    output.location = (400, 0)

    bsdf = nodes.new('ShaderNodeBsdfPrincipled')
    bsdf.location = (0, 0)
    bsdf.inputs['Base Color'].default_value = color
    bsdf.inputs['Metallic'].default_value = metallic
    bsdf.inputs['Roughness'].default_value = roughness

    if emission:
        bsdf.inputs['Emission Color'].default_value = emission
        bsdf.inputs['Emission Strength'].default_value = 0.3

    links.new(bsdf.outputs['BSDF'], output.inputs['Surface'])
    return mat


def create_screen_material(name, texture_path, emission_strength=1.0):
    """Crea un material emisivo con textura para pantallas."""
    mat = bpy.data.materials.new(name=name)
    mat.use_nodes = True
    nodes = mat.node_tree.nodes
    links = mat.node_tree.links
    nodes.clear()

    output = nodes.new('ShaderNodeOutputMaterial')
    output.location = (600, 0)

    bsdf = nodes.new('ShaderNodeBsdfPrincipled')
    bsdf.location = (300, 0)
    bsdf.inputs['Roughness'].default_value = 0.05
    bsdf.inputs['Metallic'].default_value = 0.0

    if os.path.exists(texture_path):
        tex_node = nodes.new('ShaderNodeTexImage')
        tex_node.location = (-300, 0)
        tex_node.image = bpy.data.images.load(texture_path)
        links.new(tex_node.outputs['Color'], bsdf.inputs['Base Color'])
        links.new(tex_node.outputs['Color'], bsdf.inputs['Emission Color'])
        bsdf.inputs['Emission Strength'].default_value = emission_strength
        print(f"  ✅ Textura cargada: {os.path.basename(texture_path)}")
    else:
        bsdf.inputs['Base Color'].default_value = AUTHCARES_SURFACE
        bsdf.inputs['Emission Color'].default_value = AUTHCARES_SURFACE
        bsdf.inputs['Emission Strength'].default_value = 0.5
        print(f"  ⚠️ Textura NO encontrada: {texture_path}")

    # Efecto vidrio sobre la pantalla
    bsdf.inputs['Coat Weight'].default_value = 1.0
    bsdf.inputs['Coat Roughness'].default_value = 0.02

    links.new(bsdf.outputs['BSDF'], output.inputs['Surface'])
    return mat


def set_smooth(obj):
    """Aplica smooth shading de forma segura."""
    bpy.ops.object.select_all(action='DESELECT')
    obj.select_set(True)
    bpy.context.view_layer.objects.active = obj
    bpy.ops.object.shade_smooth()
    bpy.ops.object.select_all(action='DESELECT')


def apply_modifier(obj, mod_name):
    """Aplica un modifier de forma segura."""
    bpy.ops.object.select_all(action='DESELECT')
    obj.select_set(True)
    bpy.context.view_layer.objects.active = obj
    bpy.ops.object.modifier_apply(modifier=mod_name)
    bpy.ops.object.select_all(action='DESELECT')


# ============================================================
# MODELO DEL CELULAR
# ============================================================
def create_phone(location=(0, 0, 0), rotation=(0, 0, 0)):
    """Crea un modelo de smartphone moderno."""

    width = 0.075
    height = 0.16
    depth = 0.008
    corner_radius = 0.012
    screen_inset = 0.003

    phone_col = bpy.data.collections.new("Phone")
    bpy.context.scene.collection.children.link(phone_col)

    # --- CUERPO ---
    bpy.ops.mesh.primitive_cube_add(size=1, location=(0, 0, 0))
    body = bpy.context.active_object
    body.name = "Phone_Body"
    body.scale = (width, height, depth)
    bpy.ops.object.transform_apply(scale=True)

    bevel = body.modifiers.new(name="Bevel", type='BEVEL')
    bevel.width = corner_radius
    bevel.segments = 8
    bevel.limit_method = 'ANGLE'
    bevel.angle_limit = math.radians(30)
    apply_modifier(body, "Bevel")

    sub = body.modifiers.new(name="Subsurf", type='SUBSURF')
    sub.levels = 2
    sub.render_levels = 3
    apply_modifier(body, "Subsurf")

    body_mat = create_material("Phone_Body_Mat", PHONE_BLACK, metallic=0.8, roughness=0.15)
    body.data.materials.append(body_mat)
    set_smooth(body)
    move_to_collection(body, phone_col)

    # --- PANTALLA ---
    sw = width - screen_inset * 2
    sh = height - screen_inset * 2

    bpy.ops.mesh.primitive_plane_add(size=1, location=(0, 0, depth / 2 + 0.0005))
    screen = bpy.context.active_object
    screen.name = "Phone_Screen"
    screen.scale = (sw, sh, 1)
    bpy.ops.object.transform_apply(scale=True)

    bevel_s = screen.modifiers.new(name="Bevel", type='BEVEL')
    bevel_s.width = corner_radius * 0.8
    bevel_s.segments = 6
    bevel_s.limit_method = 'ANGLE'
    apply_modifier(screen, "Bevel")

    screen_mat = create_screen_material("Phone_Screen_Mat", PHONE_TEXTURE_PATH, emission_strength=0.8)
    screen.data.materials.append(screen_mat)
    move_to_collection(screen, phone_col)

    # --- CÁMARA FRONTAL ---
    bpy.ops.mesh.primitive_cylinder_add(
        radius=0.002, depth=0.001,
        location=(0, height / 2 - 0.012, depth / 2 + 0.001)
    )
    cam_hole = bpy.context.active_object
    cam_hole.name = "Phone_Camera"
    cam_mat = create_material("Phone_Camera_Mat", (0.01, 0.01, 0.01, 1.0), metallic=0.9, roughness=0.1)
    cam_hole.data.materials.append(cam_mat)
    move_to_collection(cam_hole, phone_col)

    # --- BOTONES LATERALES ---
    for i, y_off in enumerate([0.03, 0.015]):
        bpy.ops.mesh.primitive_cube_add(size=1, location=(-width / 2 - 0.001, y_off, 0))
        btn = bpy.context.active_object
        btn.name = f"Phone_VolBtn_{i}"
        btn.scale = (0.001, 0.008, 0.002)
        bpy.ops.object.transform_apply(scale=True)
        bv = btn.modifiers.new(name="Bevel", type='BEVEL')
        bv.width = 0.0005
        bv.segments = 3
        apply_modifier(btn, "Bevel")
        btn.data.materials.append(body_mat)
        move_to_collection(btn, phone_col)

    bpy.ops.mesh.primitive_cube_add(size=1, location=(width / 2 + 0.001, 0.02, 0))
    pwr = bpy.context.active_object
    pwr.name = "Phone_PwrBtn"
    pwr.scale = (0.001, 0.012, 0.002)
    bpy.ops.object.transform_apply(scale=True)
    bv = pwr.modifiers.new(name="Bevel", type='BEVEL')
    bv.width = 0.0005
    bv.segments = 3
    apply_modifier(pwr, "Bevel")
    pwr.data.materials.append(body_mat)
    move_to_collection(pwr, phone_col)

    # --- PARENT ---
    for obj in phone_col.objects:
        if obj != body:
            obj.parent = body

    body.location = location
    body.rotation_euler = Euler(rotation)
    return body


# ============================================================
# MODELO DEL RELOJ INTELIGENTE
# ============================================================
def create_watch(location=(0, 0, 0), rotation=(0, 0, 0)):
    """Crea un modelo de smartwatch redondo."""

    watch_radius = 0.022
    watch_depth = 0.011
    screen_radius = 0.019
    band_width = 0.022
    band_length = 0.08
    band_thick = 0.002

    watch_col = bpy.data.collections.new("Watch")
    bpy.context.scene.collection.children.link(watch_col)

    # --- CAJA ---
    bpy.ops.mesh.primitive_cylinder_add(radius=watch_radius, depth=watch_depth, location=(0, 0, 0))
    case = bpy.context.active_object
    case.name = "Watch_Case"

    bevel = case.modifiers.new(name="Bevel", type='BEVEL')
    bevel.width = 0.003
    bevel.segments = 6
    bevel.limit_method = 'ANGLE'
    apply_modifier(case, "Bevel")

    sub = case.modifiers.new(name="Subsurf", type='SUBSURF')
    sub.levels = 2
    sub.render_levels = 3
    apply_modifier(case, "Subsurf")

    case_mat = create_material("Watch_Case_Mat", AUTHCARES_DARK, metallic=0.9, roughness=0.1)
    case.data.materials.append(case_mat)
    set_smooth(case)
    move_to_collection(case, watch_col)

    # --- BISEL ---
    bpy.ops.mesh.primitive_torus_add(
        major_radius=watch_radius - 0.001,
        minor_radius=0.0015,
        location=(0, 0, watch_depth / 2 - 0.001)
    )
    bezel = bpy.context.active_object
    bezel.name = "Watch_Bezel"
    bezel_mat = create_material("Watch_Bezel_Mat", (0.02, 0.02, 0.02, 1.0), metallic=0.95, roughness=0.05)
    bezel.data.materials.append(bezel_mat)
    set_smooth(bezel)
    move_to_collection(bezel, watch_col)

    # --- PANTALLA ---
    bpy.ops.mesh.primitive_cylinder_add(
        radius=screen_radius, depth=0.0005,
        location=(0, 0, watch_depth / 2 + 0.0003)
    )
    screen = bpy.context.active_object
    screen.name = "Watch_Screen"

    screen_mat = create_screen_material("Watch_Screen_Mat", WATCH_TEXTURE_PATH, emission_strength=1.2)
    screen.data.materials.append(screen_mat)

    bpy.context.view_layer.objects.active = screen
    bpy.ops.object.mode_set(mode='EDIT')
    bpy.ops.uv.project_from_view(camera_bounds=False, scale_to_bounds=True)
    bpy.ops.object.mode_set(mode='OBJECT')

    set_smooth(screen)
    move_to_collection(screen, watch_col)

    # --- CORONA ---
    bpy.ops.mesh.primitive_cylinder_add(
        radius=0.003, depth=0.004,
        location=(watch_radius + 0.002, 0, 0.002),
        rotation=(0, math.pi / 2, 0)
    )
    crown = bpy.context.active_object
    crown.name = "Watch_Crown"
    bv = crown.modifiers.new(name="Bevel", type='BEVEL')
    bv.width = 0.001
    bv.segments = 4
    apply_modifier(crown, "Bevel")
    crown.data.materials.append(case_mat)
    set_smooth(crown)
    move_to_collection(crown, watch_col)

    # --- CORREAS ---
    band_mat = create_material("Watch_Band_Mat", WATCH_BAND_BLUE, metallic=0.0, roughness=0.7)

    for direction in [1, -1]:
        label = "Top" if direction > 0 else "Bottom"

        bpy.ops.mesh.primitive_cube_add(
            size=1,
            location=(0, direction * (watch_radius + band_length / 2 - 0.005), 0)
        )
        band = bpy.context.active_object
        band.name = f"Watch_Band_{label}"
        band.scale = (band_width / 2, band_length / 2, band_thick / 2)
        bpy.ops.object.transform_apply(scale=True)

        bv = band.modifiers.new(name="Bevel", type='BEVEL')
        bv.width = 0.001
        bv.segments = 4
        bv.limit_method = 'ANGLE'
        apply_modifier(band, "Bevel")

        sub = band.modifiers.new(name="Subsurf", type='SUBSURF')
        sub.levels = 2
        sub.render_levels = 2
        apply_modifier(band, "Subsurf")

        band.data.materials.append(band_mat)
        set_smooth(band)
        move_to_collection(band, watch_col)

        # Lugs
        bpy.ops.mesh.primitive_cube_add(
            size=1,
            location=(0, direction * (watch_radius - 0.002), -0.002)
        )
        lug = bpy.context.active_object
        lug.name = f"Watch_Lug_{label}"
        lug.scale = (band_width / 2.2, 0.005, 0.004)
        bpy.ops.object.transform_apply(scale=True)
        bv = lug.modifiers.new(name="Bevel", type='BEVEL')
        bv.width = 0.002
        bv.segments = 4
        apply_modifier(lug, "Bevel")
        lug.data.materials.append(case_mat)
        set_smooth(lug)
        move_to_collection(lug, watch_col)

    # --- PARENT ---
    for obj in watch_col.objects:
        if obj != case:
            obj.parent = case

    case.location = location
    case.rotation_euler = Euler(rotation)
    return case


# ============================================================
# ESCENA DE PRESENTACIÓN
# ============================================================
def create_presentation_scene():
    """Configura la escena completa para renderizado."""

    # --- FONDO ---
    bpy.ops.mesh.primitive_plane_add(size=2, location=(0, 0, -0.15))
    floor = bpy.context.active_object
    floor.name = "Floor"
    floor.scale = (1.5, 1.5, 1)
    bpy.ops.object.transform_apply(scale=True)

    floor_mat = bpy.data.materials.new(name="Floor_Mat")
    floor_mat.use_nodes = True
    nodes = floor_mat.node_tree.nodes
    links = floor_mat.node_tree.links
    nodes.clear()

    output = nodes.new('ShaderNodeOutputMaterial')
    output.location = (600, 0)
    bsdf = nodes.new('ShaderNodeBsdfPrincipled')
    bsdf.location = (300, 0)

    gradient = nodes.new('ShaderNodeTexGradient')
    gradient.location = (-300, 0)
    gradient.gradient_type = 'RADIAL'

    colorramp = nodes.new('ShaderNodeValToRGB')
    colorramp.location = (0, 0)
    colorramp.color_ramp.elements[0].color = hex_to_linear("#FFFFFF")
    colorramp.color_ramp.elements[0].position = 0.0
    colorramp.color_ramp.elements[1].color = hex_to_linear("#D5E3FF")
    colorramp.color_ramp.elements[1].position = 0.7

    mapping = nodes.new('ShaderNodeMapping')
    mapping.location = (-600, 0)
    mapping.inputs['Location'].default_value = (0.5, 0.5, 0)
    mapping.inputs['Scale'].default_value = (1.0, 1.0, 1.0)

    texcoord = nodes.new('ShaderNodeTexCoord')
    texcoord.location = (-800, 0)

    links.new(texcoord.outputs['Generated'], mapping.inputs['Vector'])
    links.new(mapping.outputs['Vector'], gradient.inputs['Vector'])
    links.new(gradient.outputs['Color'], colorramp.inputs['Fac'])
    links.new(colorramp.outputs['Color'], bsdf.inputs['Base Color'])
    bsdf.inputs['Roughness'].default_value = 0.9
    bsdf.inputs['Specular IOR Level'].default_value = 0.1

    links.new(bsdf.outputs['BSDF'], output.inputs['Surface'])
    floor.data.materials.append(floor_mat)

    # Cyclorama
    bpy.ops.mesh.primitive_plane_add(size=3, location=(0, -1.0, 0.35))
    backdrop = bpy.context.active_object
    backdrop.name = "Backdrop"
    backdrop.rotation_euler = (math.radians(70), 0, 0)
    backdrop_mat = create_material("Backdrop_Mat", hex_to_linear("#E8EEFF"), roughness=0.95)
    backdrop.data.materials.append(backdrop_mat)

    # --- WORLD ---
    world = bpy.context.scene.world
    if world is None:
        world = bpy.data.worlds.new("World")
        bpy.context.scene.world = world
    world.use_nodes = True
    wn = world.node_tree.nodes
    wl = world.node_tree.links
    wn.clear()

    w_out = wn.new('ShaderNodeOutputWorld')
    w_out.location = (400, 0)
    w_bg = wn.new('ShaderNodeBackground')
    w_bg.location = (200, 0)
    w_bg.inputs['Color'].default_value = hex_to_linear("#E0E8F8")
    w_bg.inputs['Strength'].default_value = 0.3
    wl.new(w_bg.outputs['Background'], w_out.inputs['Surface'])

    # --- LUCES ---
    bpy.ops.object.light_add(type='AREA', location=(0.2, -0.25, 0.35))
    key = bpy.context.active_object
    key.name = "Key_Light"
    key.data.energy = 30
    key.data.size = 0.5
    key.data.color = (1.0, 0.98, 0.95)
    key.rotation_euler = (math.radians(45), math.radians(15), math.radians(-20))

    bpy.ops.object.light_add(type='AREA', location=(-0.3, -0.15, 0.2))
    fill = bpy.context.active_object
    fill.name = "Fill_Light"
    fill.data.energy = 12
    fill.data.size = 0.6
    fill.data.color = (0.9, 0.93, 1.0)
    fill.rotation_euler = (math.radians(50), math.radians(-30), math.radians(10))

    bpy.ops.object.light_add(type='AREA', location=(0.0, 0.2, 0.25))
    rim = bpy.context.active_object
    rim.name = "Rim_Light"
    rim.data.energy = 18
    rim.data.size = 0.3
    rim.data.color = (0.85, 0.9, 1.0)
    rim.rotation_euler = (math.radians(-30), 0, 0)

    bpy.ops.object.light_add(type='POINT', location=(0.15, 0.0, -0.05))
    accent = bpy.context.active_object
    accent.name = "Accent_Light_Blue"
    accent.data.energy = 3
    accent.data.color = (0.0, 0.32, 0.8)

    # --- CÁMARA ---
    bpy.ops.object.camera_add(
        location=(0.06, -0.38, 0.12),
        rotation=(math.radians(72), 0, math.radians(5))
    )
    cam = bpy.context.active_object
    cam.name = "Presentation_Camera"
    cam.data.lens = 85
    cam.data.dof.use_dof = True
    cam.data.dof.aperture_fsize = 2.8
    cam.data.dof.focus_distance = 0.38
    bpy.context.scene.camera = cam

    # --- RENDER ---
    scene = bpy.context.scene
    scene.render.engine = 'CYCLES'
    scene.cycles.device = 'GPU'
    scene.cycles.samples = 256
    scene.cycles.use_denoising = True
    scene.render.resolution_x = 3840
    scene.render.resolution_y = 2160
    scene.render.resolution_percentage = 100
    scene.render.film_transparent = False
    scene.render.image_settings.file_format = 'PNG'
    scene.render.image_settings.color_mode = 'RGBA'
    scene.render.filepath = os.path.join(SCRIPT_DIR, "render_authcares2_presentation.png")


# ============================================================
# MAIN
# ============================================================
def main():
    print("\n" + "=" * 60)
    print("  AuthCares2 — Generando escena 3D")
    print("=" * 60)

    print("\n🗑️  Limpiando escena...")
    clean_scene()

    print("📱 Creando celular...")
    create_phone(
        location=(-0.04, 0, 0.0),
        rotation=(math.radians(75), math.radians(-8), math.radians(-12))
    )

    print("⌚ Creando reloj...")
    create_watch(
        location=(0.065, 0.02, -0.04),
        rotation=(math.radians(70), math.radians(5), math.radians(15))
    )

    print("🎬 Configurando escena...")
    create_presentation_scene()

    print("\n" + "=" * 60)
    print("🎉 ¡LISTO! Presiona F12 para renderizar.")
    print(f"   Render 4K → {SCRIPT_DIR}")
    print("=" * 60 + "\n")


# Ejecutar
main()
