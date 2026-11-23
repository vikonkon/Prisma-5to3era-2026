"use strict";
// Lista de facturas. El gerente ve todas (con los clientes); un cliente ve solo las suyas.
// Usa de sesion.js: esGerente, cabeceras, sesionVencida.

const ORIGEN = location.port === "8080" ? "" : "http://localhost:8080";
const dinero = new Intl.NumberFormat("es-AR", { style: "currency", currency: "ARS", minimumFractionDigits: 2 });
const $ = (id) => document.getElementById(id);
let facturas = [];

function celda(texto, clase) {
  const td = document.createElement("td");
  td.textContent = texto === null || texto === undefined || texto === "" ? "—" : String(texto);
  if (clase) td.className = clase;
  return td;
}

function fila(f) {
  const tr = document.createElement("tr");
  tr.append(celda(f.numero, "f-nro"), celda(f.fecha));
  if (esGerente) tr.append(celda(f.cliente), celda(f.cuit), celda(f.correo));
  tr.append(celda(f.prendas, "num"), celda(dinero.format(f.total), "num"));
  const ver = document.createElement("a");
  ver.className = "btn btn-claro btn-mini";
  ver.href = "factura.html?id=" + encodeURIComponent(f.id);
  ver.textContent = "Ver factura";
  const td = document.createElement("td");
  td.append(ver);
  tr.append(td);
  return tr;
}

function dibujarResumen() {
  const total = facturas.reduce((suma, f) => suma + Number(f.total), 0);
  const datos = [[String(facturas.length), esGerente ? "Facturas emitidas" : "Compras realizadas"], [dinero.format(total), esGerente ? "Total facturado" : "Total gastado"]];
  if (esGerente) {
    const clientes = new Set(facturas.map((f) => f.correo || f.cliente));
    datos.push([String(clientes.size), "Clientes que compraron"]);
  }
  $("resumen").replaceChildren(...datos.map(([valor, etiqueta]) => {
    const caja = document.createElement("div");
    const b = document.createElement("b");
    b.textContent = valor;
    const s = document.createElement("span");
    s.textContent = etiqueta;
    caja.append(b, s);
    return caja;
  }));
}

function dibujar() {
  const q = $("buscar").value.trim().toLowerCase();
  const visibles = facturas.filter((f) =>
    !q || [f.numero, f.fecha, f.cliente, f.correo, f.cuit].some((v) => String(v || "").toLowerCase().includes(q)));
  $("filas").replaceChildren(...visibles.map(fila));
  $("cuenta").textContent = `${visibles.length} de ${facturas.length}`;
  const vacio = $("vacio");
  vacio.hidden = visibles.length > 0;
  vacio.textContent = facturas.length === 0
    ? (esGerente ? "Todavía no se realizó ninguna compra." : "Todavía no hiciste ninguna compra.")
    : "No encontramos facturas con esa búsqueda.";
}

async function cargar() {
  try {
    const res = await fetch(ORIGEN + "/api/facturas", { headers: cabeceras() });
    if (res.status === 401) { sesionVencida(); return; }
    if (!res.ok) throw new Error("Error " + res.status);
    facturas = await res.json();
    dibujarResumen();
    dibujar();
  } catch (error) {
    $("vacio").hidden = false;
    $("vacio").textContent = "No pudimos cargar las facturas. ¿Está encendido Spring Boot? " + error.message;
  }
}

$("titulo").textContent = esGerente ? "Facturas emitidas" : "Mis compras";
$("subtitulo").textContent = esGerente
  ? "Todas las compras realizadas en Prisma y los clientes que las hicieron."
  : "Acá están las facturas de tus compras. Tocá una para ver el detalle completo.";
document.querySelectorAll(".solo-gerente").forEach((e) => { e.hidden = !esGerente; });
$("buscar").addEventListener("input", dibujar);
cargar();
