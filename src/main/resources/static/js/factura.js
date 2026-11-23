"use strict";
// Muestra una factura completa. Usa de sesion.js: cabeceras, sesionVencida.

const ORIGEN = location.port === "8080" ? "" : "http://localhost:8080";
const dinero = new Intl.NumberFormat("es-AR", { style: "currency", currency: "ARS", minimumFractionDigits: 2 });
const $ = (id) => document.getElementById(id);

function poner(id, valor) {
  $(id).textContent = valor === null || valor === undefined || valor === "" ? "—" : String(valor);
}

function mostrarError(texto) {
  const msg = $("f-msg");
  msg.textContent = texto;
  msg.hidden = false;
}

function celda(texto, clase) {
  const td = document.createElement("td");
  td.textContent = texto;
  if (clase) td.className = clase;
  return td;
}

function renglon(i) {
  const tr = document.createElement("tr");

  const prod = document.createElement("td");
  prod.className = "f-prod";
  if (i.imagen) {
    const img = document.createElement("img");
    img.alt = "";
    img.referrerPolicy = "no-referrer";
    img.addEventListener("error", () => img.remove());
    img.src = i.imagen;
    prod.append(img);
  }
  const info = document.createElement("div");
  const nombre = document.createElement("strong");
  nombre.textContent = i.nombre;
  info.append(nombre);
  if (i.descripcion) {
    const d = document.createElement("span");
    d.textContent = i.descripcion;
    info.append(d);
  }
  if (i.categoria) {
    const c = document.createElement("em");
    c.textContent = "Categoría: " + i.categoria;
    info.append(c);
  }
  prod.append(info);

  tr.append(celda(i.codigo, "f-cod"), prod, celda(i.cantidad, "num"), celda(dinero.format(i.precioUnitario), "num"), celda(dinero.format(i.importe), "num"));
  return tr;
}

function dibujar(f) {
  document.title = "Prisma · Factura " + f.numero;
  poner("f-numero", f.numero);
  poner("f-fecha", f.fecha || "Sin fecha registrada");
  poner("e-razon", f.emisor.razonSocial);
  poner("e-cuit", f.emisor.cuit);
  poner("e-domicilio", f.emisor.domicilio);
  poner("e-iva", f.emisor.condicionIva);
  poner("e-correo", f.emisor.correo);
  poner("r-nombre", f.receptor.nombre);
  poner("r-cuit", f.receptor.cuit);
  poner("r-iva", f.receptor.condicionIva);
  poner("r-correo", f.receptor.correo);
  poner("r-domicilio", f.receptor.domicilio);
  poner("r-pago", f.medioPago);
  $("f-items").replaceChildren(...f.items.map(renglon));
  poner("t-subtotal", dinero.format(f.subtotal));
  $("t-iva-etiqueta").textContent = `IVA ${f.alicuotaIva}%`;
  poner("t-iva", dinero.format(f.iva));
  poner("t-total", dinero.format(f.total));
  $("factura").hidden = false;
}

async function cargar() {
  const id = new URLSearchParams(location.search).get("id");
  if (!id || !/^\d+$/.test(id)) { mostrarError("Falta indicar qué factura querés ver."); return; }
  try {
    const res = await fetch(`${ORIGEN}/api/facturas/${id}`, { headers: cabeceras() });
    if (res.status === 401) { sesionVencida(); return; }
    if (res.status === 403) { mostrarError("Esta factura pertenece a otra cuenta."); return; }
    if (res.status === 404) { mostrarError("No encontramos esa factura."); return; }
    if (!res.ok) { mostrarError("No se pudo cargar la factura (error " + res.status + ")."); return; }
    dibujar(await res.json());
  } catch (error) {
    mostrarError("No se pudo cargar la factura. ¿Está encendido Spring Boot? " + error.message);
  }
}

cargar();
