import fs from "node:fs/promises";
import path from "node:path";
import { Presentation, PresentationFile } from "@oai/artifact-tool";

const OUT_DIR = "C:/Users/marce/AuthCares2/outputs";
const WORK_DIR = "C:/Users/marce/AuthCares2/outputs/authcares_presentation_work";
const FINAL = path.join(OUT_DIR, "AuthCares_Defensa_Mejorada_7_Diapositivas.pptx");
const PREVIEW_DIR = path.join(WORK_DIR, "preview");
const ASSET_DIR = "C:/Users/marce/AuthCares2/app/src/main/res/drawable";

const W = 1280;
const H = 720;
const navy = "#0E1527";
const ink = "#17202E";
const muted = "#667085";
const purple = "#7C3AED";
const violet = "#A78BFA";
const teal = "#25B99A";
const mint = "#DDF7EE";
const amber = "#F4B942";
const soft = "#F7F8FC";
const line = "#D9DDEA";

async function writeBlob(filePath, blob) {
  await fs.writeFile(filePath, new Uint8Array(await blob.arrayBuffer()));
}

async function imageBlob(filePath) {
  const bytes = await fs.readFile(filePath);
  return bytes.buffer.slice(bytes.byteOffset, bytes.byteOffset + bytes.byteLength);
}

function rect(slide, x, y, w, h, fill, radius = "rounded-xl", stroke = "none", shadow = undefined) {
  return slide.shapes.add({
    geometry: "roundRect",
    position: { left: x, top: y, width: w, height: h },
    fill,
    line: { style: "solid", fill: stroke, width: stroke === "none" ? 0 : 1 },
    borderRadius: radius,
    shadow,
  });
}

function circle(slide, x, y, size, fill, opacity = 1) {
  const shape = slide.shapes.add({
    geometry: "ellipse",
    position: { left: x, top: y, width: size, height: size },
    fill,
    line: { style: "solid", fill: "none", width: 0 },
  });
  if (opacity !== 1) shape.opacity = opacity;
  return shape;
}

function text(slide, value, x, y, w, h, style = {}) {
  const box = slide.shapes.add({
    geometry: "textbox",
    position: { left: x, top: y, width: w, height: h },
    fill: "none",
    line: { style: "solid", fill: "none", width: 0 },
  });
  box.text = value;
  box.text.style = {
    fontFace: "Aptos",
    fontSize: 24,
    color: ink,
    fit: "shrink",
    ...style,
  };
  return box;
}

function footer(slide, index, dark = false) {
  text(slide, "AuthCares", 58, 680, 160, 24, {
    fontSize: 13,
    bold: true,
    color: dark ? "#B9C3D8" : "#7B8497",
  });
  text(slide, `${index} / 7`, 604, 680, 72, 24, {
    fontSize: 13,
    color: dark ? "#8591AA" : "#9AA3B2",
    alignment: "center",
  });
}

function topLabel(slide, value, time = "~1 min") {
  text(slide, value, 58, 34, 280, 28, {
    fontSize: 13,
    bold: true,
    color: "#4A5568",
  });
  rect(slide, 1088, 18, 124, 46, amber, "rounded-lg", "#E9A92F", "shadow-sm");
  text(slide, time, 1112, 30, 82, 24, {
    fontSize: 17,
    bold: true,
    color: "#2D2410",
    alignment: "center",
  });
}

function chip(slide, value, x, y, w, fill = "#FFFFFF", color = ink) {
  rect(slide, x, y, w, 42, fill, "rounded-lg", line, "shadow-sm");
  text(slide, value, x + 16, y + 10, w - 32, 24, {
    fontSize: 16,
    bold: true,
    color,
    alignment: "center",
  });
}

function bullet(slide, value, x, y, color = ink) {
  circle(slide, x, y + 7, 10, teal);
  text(slide, value, x + 22, y, 470, 34, { fontSize: 21, color });
}

function cardTitle(slide, title, body, x, y, w, h, accent, icon) {
  rect(slide, x, y, w, h, "#FFFFFF", "rounded-xl", line, "shadow-md");
  circle(slide, x + 26, y + 28, 54, accent);
  text(slide, icon, x + 26, y + 37, 54, 28, {
    fontSize: 24,
    bold: true,
    color: "#FFFFFF",
    alignment: "center",
  });
  text(slide, title, x + 96, y + 30, w - 122, 32, {
    fontSize: 25,
    bold: true,
    color: ink,
  });
  text(slide, body, x + 96, y + 68, w - 122, Math.max(34, h - 76), {
    fontSize: 20,
    color: muted,
  });
}

function notes(slide, lines) {
  slide.speakerNotes.textFrame.setText(lines);
}

async function addImage(slide, file, x, y, w, h, alt, fit = "cover", radius = "rounded-xl") {
  slide.images.add({
    blob: await imageBlob(file),
    contentType: "image/png",
    alt,
    fit,
    geometry: "roundRect",
    borderRadius: radius,
    position: { left: x, top: y, width: w, height: h },
  });
}

const presentation = Presentation.create({ slideSize: { width: W, height: H } });

// 1. Portada
{
  const slide = presentation.slides.add();
  slide.background.fill = navy;
  circle(slide, 982, -150, 430, "#30245F", 0.9);
  circle(slide, -140, 530, 330, "#2B2456", 0.95);
  rect(slide, 58, 42, 398, 48, purple, "rounded-lg", "#9F7AEA");
  text(slide, "PROG. MOVILES 2026  Â·  TECSUP AREQUIPA", 82, 58, 350, 20, {
    fontSize: 13,
    bold: true,
    color: "#D7CCFF",
    alignment: "center",
  });
  text(slide, "AuthCares", 58, 142, 650, 92, {
    fontFace: "Georgia",
    fontSize: 72,
    bold: true,
    color: "#FFFFFF",
  });
  text(slide, "Monitoreo inteligente para acompaÃ±ar a niÃ±os con TEA en tiempo real.", 62, 250, 720, 72, {
    fontSize: 29,
    italic: true,
    color: "#C9B9FF",
  });
  rect(slide, 58, 363, 820, 2, "#536079", "rounded-sm");
  text(slide, "Equipo: Karla Choque  Â·  [Integrante 2]  Â·  [Integrante 3]  Â·  [Integrante 4]", 58, 392, 760, 32, {
    fontSize: 18,
    color: "#CAD3E7",
  });
  rect(slide, 58, 504, 250, 58, teal, "rounded-lg", "#55D0B4", "shadow-md");
  text(slide, "5 - 10 MINUTOS", 100, 520, 160, 24, { fontSize: 17, bold: true, color: "#FFFFFF", alignment: "center" });
  await addImage(slide, path.join(ASSET_DIR, "hero_bienvenida_authcares.png"), 802, 128, 350, 350, "Imagen principal de AuthCares");
  rect(slide, 930, 506, 222, 100, "#20293B", "rounded-xl", "#6B7280");
  text(slide, "QR GitHub / Demo", 958, 545, 166, 28, { fontSize: 18, color: "#DDE3F0", alignment: "center" });
  footer(slide, 1, true);
  notes(slide, ["Abrir con una frase clara: AuthCares convierte datos del reloj en informacion util para cuidar mejor.", "Presentarse y decir que la demo mostrara el flujo principal."]);
}

// 2. Problema
{
  const slide = presentation.slides.add();
  slide.background.fill = soft;
  topLabel(slide, "01 / EL PROBLEMA");
  text(slide, "Â¿A quiÃ©n le duele?", 58, 96, 720, 72, {
    fontFace: "Georgia",
    fontSize: 54,
    bold: true,
    color: ink,
  });
  rect(slide, 58, 196, 710, 300, "#FFFFFF", "rounded-xl", line, "shadow-md");
  text(slide, "El problema real:", 88, 222, 260, 34, { fontSize: 24, bold: true, color: ink });
  text(
    slide,
    "Una madre no siempre puede estar cerca de su hijo durante clases, terapia o actividades diarias. Si ocurre un cambio importante, muchas veces se entera tarde o sin datos claros.",
    88,
    282,
    610,
    92,
    { fontSize: 24, color: "#495060" },
  );
  text(
    slide,
    "No se trata de vigilar mÃ¡s. Se trata de acompaÃ±ar mejor, con calma y contexto.",
    88,
    406,
    610,
    54,
    { fontSize: 22, italic: true, color: purple },
  );
  circle(slide, 862, 176, 330, "#EDE7FF");
  text(slide, "!", 982, 248, 90, 92, { fontSize: 72, bold: true, color: "#E5484D", alignment: "center" });
  text(slide, "Distancia", 842, 392, 370, 28, { fontSize: 22, bold: true, color: ink, alignment: "center" });
  text(slide, "Poca informaciÃ³n", 842, 426, 370, 28, { fontSize: 19, color: muted, alignment: "center" });
  rect(slide, 58, 560, 1164, 54, "#FFF8DB", "rounded-md", "#F2D36D");
  text(slide, "TIP: empieza con esta historia antes de enseÃ±ar pantallas. El jurado debe sentir por quÃ© existe AuthCares.", 84, 575, 1090, 24, {
    fontSize: 18,
    color: "#5B4B13",
  });
  footer(slide, 2);
  notes(slide, ["Contar una historia sencilla de una mama o cuidador que necesita saber que paso durante el dia.", "Evitar sonar medico o alarmista."]);
}

// 3. Solucion
{
  const slide = presentation.slides.add();
  slide.background.fill = soft;
  topLabel(slide, "02 / LA SOLUCION");
  text(slide, "Tu app en una sola oraciÃ³n.", 58, 96, 780, 66, {
    fontFace: "Georgia",
    fontSize: 50,
    bold: true,
    color: ink,
  });
  rect(slide, 58, 180, 1164, 126, navy, "rounded-xl", "#20283C", "shadow-md");
  text(
    slide,
    "AuthCares permite a padres y cuidadores monitorear seÃ±ales del niÃ±o desde un Galaxy Watch para actuar con mÃ¡s calma, contexto y rapidez.",
    92,
    222,
    1096,
    46,
    { fontSize: 25, italic: true, color: "#FFFFFF", alignment: "center" },
  );
  cardTitle(slide, "1. El reloj escucha", "Registra ritmo cardÃ­aco y movimiento durante el dÃ­a.", 70, 338, 330, 206, teal, "1");
  text(slide, "â†’", 418, 410, 60, 44, { fontSize: 42, bold: true, color: "#8B7AC5", alignment: "center" });
  cardTitle(slide, "2. La app ordena", "Firebase actualiza la informaciÃ³n y la muestra clara.", 476, 338, 330, 206, purple, "2");
  text(slide, "â†’", 824, 410, 60, 44, { fontSize: 42, bold: true, color: "#8B7AC5", alignment: "center" });
  cardTitle(slide, "3. El cuidador actÃºa", "Recibe alertas, consulta IA y revisa estadÃ­sticas.", 882, 338, 330, 206, amber, "3");
  footer(slide, 3);
  notes(slide, ["Decir la frase completa sin entrar a codigo.", "Luego explicar los 3 pasos: reloj, app, cuidador."]);
}

// 4. Demo
{
  const slide = presentation.slides.add();
  slide.background.fill = navy;
  circle(slide, 186, 24, 640, "#171B43", 0.92);
  rect(slide, 58, 22, 258, 58, teal, "rounded-lg", "#55D0B4", "shadow-md");
  text(slide, "4 - 5 MIN", 120, 40, 120, 24, { fontSize: 18, bold: true, color: "#FFFFFF", alignment: "center" });
  text(slide, "DEMO", 132, 146, 530, 142, {
    fontFace: "Georgia",
    fontSize: 116,
    bold: true,
    color: "#7C3AED",
    alignment: "center",
  });
  text(slide, "EN VIVO", 314, 372, 220, 48, { fontSize: 34, color: "#FFFFFF", alignment: "center" });
  rect(slide, 782, 18, 430, 536, "#232B3D", "rounded-xl", "#445066", "shadow-lg");
  text(slide, "Checklist de demo", 868, 48, 258, 28, { fontSize: 20, bold: true, color: "#BDA7FF", alignment: "center" });
  const items = [
    "Inicio desde cero",
    "Login o registro",
    "Flujo principal completo",
    "IA con datos reales",
    "Estado de carga o error visible",
  ];
  items.forEach((item, i) => {
    circle(slide, 812, 104 + i * 80, 38, teal);
    text(slide, "âœ“", 812, 111 + i * 80, 38, 18, { fontSize: 18, bold: true, color: "#FFFFFF", alignment: "center" });
    text(slide, item, 862, 106 + i * 80, 300, 30, { fontSize: 20, color: "#EAF0F8" });
  });
  rect(slide, 790, 570, 410, 70, "#251D57", "rounded-lg", "#7C3AED", "shadow-md");
  text(slide, "Si algo falla: calma. Mostrar video de respaldo y explicar quÃ© estaba cargando.", 816, 588, 350, 36, {
    fontSize: 17,
    color: "#F8ECFF",
  });
  footer(slide, 4, true);
  notes(slide, ["En la demo ir directo: abrir app, mostrar monitoreo, estadisticas, alerta o IA.", "Si no carga, usar video de respaldo."]);
}

// 5. IA en accion
{
  const slide = presentation.slides.add();
  slide.background.fill = soft;
  topLabel(slide, "03 / IA EN ACCION");
  text(slide, "Â¿QuÃ© hace la IA por el usuario?", 58, 92, 920, 72, {
    fontFace: "Georgia",
    fontSize: 50,
    bold: true,
    color: ink,
  });
  rect(slide, 58, 190, 520, 314, "#FFF1F2", "rounded-xl", "#F2C5CE", "shadow-sm");
  text(slide, "SIN la IA", 92, 220, 220, 32, { fontSize: 25, bold: true, color: "#8A2D3B" });
  bullet(slide, "El cuidador revisa datos sueltos.", 92, 286, "#5A2B33");
  bullet(slide, "Tiene que interpretar todo solo.", 92, 338, "#5A2B33");
  bullet(slide, "Puede tardar en decidir quÃ© hacer.", 92, 390, "#5A2B33");
  text(slide, "â†’", 604, 326, 72, 54, { fontSize: 46, bold: true, color: purple, alignment: "center" });
  rect(slide, 704, 190, 520, 314, "#ECFDF3", "rounded-xl", "#B7E4CB", "shadow-sm");
  text(slide, "CON la IA", 738, 220, 220, 32, { fontSize: 25, bold: true, color: "#206B50" });
  bullet(slide, "Resume lo importante.", 738, 286, "#245445");
  bullet(slide, "Sugiere pasos de calma y revisiÃ³n.", 738, 338, "#245445");
  bullet(slide, "Da orientaciÃ³n sin reemplazar al profesional.", 738, 390, "#245445");
  rect(slide, 430, 558, 420, 58, purple, "rounded-lg", "#9F7AEA", "shadow-md");
  text(slide, "TecnologÃ­a usada: Gemini API + Firebase", 458, 576, 365, 24, {
    fontSize: 18,
    bold: true,
    color: "#FFFFFF",
    alignment: "center",
  });
  await addImage(slide, path.join(ASSET_DIR, "fab_asistente_ia.png"), 1070, 98, 90, 90, "BotÃ³n del asistente IA", "contain", "rounded-lg");
  footer(slide, 5);
  notes(slide, ["Explicar que la IA ayuda a entender y orientar.", "Importante: no diagnostica ni reemplaza a especialistas."]);
}

// 6. Aprendizajes
{
  const slide = presentation.slides.add();
  slide.background.fill = soft;
  topLabel(slide, "04 / APRENDIZAJES DEL EQUIPO");
  text(slide, "Â¿QuÃ© se llevaron del proyecto?", 58, 94, 900, 70, {
    fontFace: "Georgia",
    fontSize: 50,
    bold: true,
    color: ink,
  });
  cardTitle(slide, "El mayor reto", "Unir datos del reloj, Firebase y pantallas de la app sin perder claridad para el usuario.", 58, 184, 1164, 110, "#4F7BFF", "R");
  cardTitle(slide, "CÃ³mo lo resolvimos", "Probamos el flujo por partes: primero datos, luego monitoreo, despuÃ©s estadÃ­sticas, alertas e IA.", 58, 320, 1164, 110, teal, "S");
  cardTitle(slide, "Lo harÃ­amos diferente", "Preparar antes el video de demo, completar links y cerrar nombres del equipo para que todo se vea listo.", 58, 456, 1164, 110, purple, "M");
  footer(slide, 6);
  notes(slide, ["Ser honestos: el aprendizaje principal fue construir un producto conectado.", "No presentar AuthCares como pantallas aisladas, sino como un flujo de cuidado."]);
}

// 7. Cierre
{
  const slide = presentation.slides.add();
  slide.background.fill = navy;
  circle(slide, 982, -130, 410, "#2B2C50", 0.95);
  circle(slide, -160, 548, 360, "#2B2456", 0.96);
  text(slide, "Gracias.", 58, 104, 540, 86, {
    fontFace: "Georgia",
    fontSize: 78,
    bold: true,
    color: "#FFFFFF",
  });
  text(slide, "Â¿Preguntas?", 62, 244, 430, 50, {
    fontSize: 34,
    italic: true,
    color: "#B99DFF",
  });
  rect(slide, 58, 330, 680, 218, "#222B3E", "rounded-xl", "#48546A", "shadow-md");
  text(slide, "GitHub: github.com/KvrlvP/AuthCares2", 94, 398, 580, 28, { fontSize: 22, color: "#E8EEF8" });
  text(slide, "Demo: [pegar link del video]", 94, 448, 520, 28, { fontSize: 22, color: "#E8EEF8" });
  text(slide, "Equipo: Karla Choque  Â·  [Integrante 2]  Â·  [Integrante 3]  Â·  [Integrante 4]", 94, 498, 600, 28, {
    fontSize: 19,
    color: "#B8C2D8",
  });
  rect(slide, 910, 300, 242, 242, "#222B3E", "rounded-xl", "#697386");
  text(slide, "QR GitHub o video", 950, 402, 162, 28, { fontSize: 20, color: "#DDE3F0", alignment: "center" });
  text(slide, "AuthCares convierte datos en cuidado.", 58, 604, 760, 36, {
    fontSize: 24,
    bold: true,
    color: "#FFFFFF",
  });
  footer(slide, 7, true);
  notes(slide, ["Cerrar con la frase final: AuthCares convierte datos en cuidado.", "Agradecer y dejar abierto a preguntas."]);
}

await fs.mkdir(PREVIEW_DIR, { recursive: true });
for (const [index, slide] of presentation.slides.items.entries()) {
  const stem = `slide-${String(index + 1).padStart(2, "0")}`;
  await writeBlob(path.join(PREVIEW_DIR, `${stem}.png`), await presentation.export({ slide, format: "png", scale: 1 }));
  await fs.writeFile(path.join(PREVIEW_DIR, `${stem}.layout.json`), await (await slide.export({ format: "layout" })).text());
}
await writeBlob(path.join(WORK_DIR, "AuthCares_Defensa_Mejorada_preview.webp"), await presentation.export({ format: "webp", montage: true, scale: 1 }));
const snapshot = await presentation.inspect({ kind: "slide,textbox,shape,image,chart,notes", maxChars: 25000 });
await fs.writeFile(path.join(WORK_DIR, "AuthCares_Defensa_Mejorada.inspect.ndjson"), snapshot.ndjson, "utf8");
const pptx = await PresentationFile.exportPptx(presentation);
await pptx.save(FINAL);
console.log(FINAL);


