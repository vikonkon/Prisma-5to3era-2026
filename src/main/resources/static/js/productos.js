"use strict";

const ORIGEN = location.port === "8080" ? "" : "http://localhost:8080";
const API_PRODUCTOS = ORIGEN + "/api/productos";
const grid = document.getElementById("grid");
const contador = document.getElementById("contador");
const precioAR = new Intl.NumberFormat("es-AR", { style: "currency", currency: "ARS", maximumFractionDigits: 0 });
let todos = [];
let categoriaActual = "todo";
const controles = new Map(); // id de producto -> { producto, caja } (para refrescar el selector de cantidad)

// Crea un elemento HTML con clase y texto (más corto que escribirlo cada vez)
function el(tag, clase, texto) {
  const e = document.createElement(tag);
  if (clase) e.className = clase;
  if (texto !== undefined) e.textContent = texto;
  return e;
}

// La categoría es la que eligió el gerente (remeras, pantalones, abrigos o calzado)
function categoria(p) {
  return p.categoria || "";
}

function mostrarEstado(titulo, mensaje) {
  const caja = el("div", "estado");
  caja.append(el("h3", null, titulo), el("p", null, mensaje));
  grid.replaceChildren(caja);
}

async function cargar() {
  mostrarEstado("Cargando…", "Buscando las prendas en la base de datos.");
  try {
    const res = await fetch(API_PRODUCTOS);
    if (!res.ok) throw new Error("Error " + res.status);
    todos = await res.json();
    sincronizarCarrito();
    mostrar(categoriaActual);
    if (typeof alCargarProductos === "function") alCargarProductos(); // avisa al panel del gerente
  } catch (error) {
    mostrarEstado("No pudimos cargar la colección", "Verificá que Spring Boot esté encendido. Detalle: " + error.message);
  }
}

function mostrar(cat) {
  categoriaActual = cat;
  controles.clear();
  document.querySelectorAll(".filtros button").forEach((b) => b.classList.toggle("activo", b.dataset.cat === cat));
  const lista = cat === "todo" ? todos : todos.filter((p) => categoria(p) === cat);
  contador.textContent = `${lista.length} ${lista.length === 1 ? "prenda" : "prendas"}`;
  if (lista.length === 0) {
    mostrarEstado("No hay prendas acá todavía", "Probá con otra categoría.");
    return;
  }
  grid.replaceChildren(...lista.map(crearTarjeta));
}

function crearTarjeta(p) {
  const cant = Number(p.cantidad);
  const tarjeta = el("article", "card");

  const foto = el("div", "foto", (p.nombre || "?").charAt(0).toUpperCase());
  const url = p.urlimagen ?? p.URLimagen ?? "";
  if (url) {
    const img = document.createElement("img");
    img.alt = p.nombre;
    img.referrerPolicy = "no-referrer";
    img.addEventListener("error", () => img.remove()); // si la imagen falla, queda la letra
    img.src = url;
    foto.append(img);
  }

  if (cant === 0) tarjeta.append(el("div", "estrella", "Agotado"));
  else if (cant <= 10) tarjeta.append(el("div", "estrella", "¡Últimas!"));

  // Zona de compra: botón "Agregar" o, si ya está en el carrito, el selector con la cantidad
  const caja = el("div", "compra");
  controles.set(p.id, { producto: p, caja });
  dibujarControl(p, caja);

  const cuerpo = el("div", "cuerpo");
  cuerpo.append(
    el("h3", null, p.nombre),
    el("p", "desc", p.descripcion || ""),
    el("p", "stock", cant > 0 ? `${cant} en stock` : "Sin stock"),
    el("p", "precio", precioAR.format(Number(p.precio))),
    caja
  );

  if (esGerente) {
    if (!p.categoria) cuerpo.append(el("p", "stock", "⚠ Sin categoría: tocá Editar y elegí una"));
    const editar = el("button", "btn btn-claro", "Editar producto");
    editar.type = "button";
    editar.addEventListener("click", () => editarProducto(p)); // definido en gerente.js
    cuerpo.append(editar);
  }

  tarjeta.append(foto, cuerpo);
  dibujarControl(p, caja); // ahora que la caja está dentro de la tarjeta, marca la cinta si ya está en el carrito
  return tarjeta;
}

// Dibuja (o redibuja) la zona de compra de una prenda según lo que haya en el carrito
function dibujarControl(p, caja) {
  const stock = Number(p.cantidad);
  const item = carrito.find((i) => i.id === p.id);
  const n = item ? item.cantidad : 0;
  caja.replaceChildren();
  const tarjeta = caja.closest(".card");
  if (tarjeta) tarjeta.classList.toggle("elegida", n > 0); // cinta "En tu carrito"

  if (!(stock > 0)) {
    const sin = el("button", "btn", "Sin stock");
    sin.type = "button";
    sin.disabled = true;
    caja.append(sin);
    return;
  }

  if (n === 0) {
    const agregar = el("button", "btn", "Agregar al carrito");
    agregar.type = "button";
    agregar.addEventListener("click", () => fijarCantidad(p, 1));
    caja.append(agregar);
    return;
  }

  const menos = el("button", null, "−");
  menos.type = "button";
  menos.setAttribute("aria-label", "Quitar una unidad");
  menos.addEventListener("click", () => fijarCantidad(p, n - 1));

  const mas = el("button", null, "+");
  mas.type = "button";
  mas.setAttribute("aria-label", "Agregar una unidad");
  mas.disabled = n >= stock;
  mas.addEventListener("click", () => fijarCantidad(p, n + 1));

  const campo = document.createElement("input");
  campo.type = "number";
  campo.min = "0";
  campo.max = String(stock);
  campo.value = String(n);
  campo.setAttribute("aria-label", "Cantidad de " + p.nombre);
  campo.addEventListener("change", () => fijarCantidad(p, parseInt(campo.value, 10) || 0));

  const selector = el("div", "selector");
  selector.append(menos, campo, mas);
  caja.append(
    selector,
    el("p", "en-carrito", `${n} ${n === 1 ? "unidad" : "unidades"} en tu carrito`)
  );
}

// Vuelve a dibujar la zona de compra de todas las prendas visibles
function refrescarControles() {
  controles.forEach(({ producto, caja }) => dibujarControl(producto, caja));
}

// Pone en el carrito exactamente "nueva" unidades de esa prenda (0 = la saca; nunca más que el stock)
function fijarCantidad(p, nueva) {
  nueva = Math.max(0, Math.min(nueva, Number(p.cantidad)));
  const item = carrito.find((i) => i.id === p.id);
  if (nueva === 0) carrito = carrito.filter((i) => i.id !== p.id);
  else if (item) { item.cantidad = nueva; item.stock = p.cantidad; }
  else carrito.push({ id: p.id, nombre: p.nombre, precio: Number(p.precio), cantidad: nueva, stock: p.cantidad });
  guardarCarrito();
}

// ================== CARRITO (se guarda en localStorage) ==================
let carrito = []; // cada item: { id, nombre, precio, cantidad, stock }
try { carrito = JSON.parse(localStorage.getItem("prismaCarrito")) || []; } catch { carrito = []; }

function avisoCarrito(texto) {
  const msg = document.getElementById("carrito-msg");
  msg.textContent = texto;
  msg.hidden = !texto;
}

function guardarCarrito() {
  localStorage.setItem("prismaCarrito", JSON.stringify(carrito));
  dibujarCarrito();
  refrescarControles();
}

// Después de recargar el catálogo: actualiza stock y precios del carrito y saca lo que ya no existe
function sincronizarCarrito() {
  carrito = carrito.filter((item) => {
    const p = todos.find((x) => x.id === item.id);
    if (!p || Number(p.cantidad) <= 0) return false;
    item.stock = p.cantidad;
    item.precio = Number(p.precio);
    item.nombre = p.nombre;
    item.cantidad = Math.min(item.cantidad, Number(p.cantidad));
    return true;
  });
  localStorage.setItem("prismaCarrito", JSON.stringify(carrito));
  dibujarCarrito();
}

function cambiarCantidad(item, cambio) {
  const nueva = item.cantidad + cambio;
  if (nueva < 1) carrito = carrito.filter((i) => i !== item);
  else if (nueva > item.stock) { avisoCarrito("No hay más stock de esa prenda."); return; }
  else item.cantidad = nueva;
  avisoCarrito("");
  guardarCarrito();
}

// Dibuja la lista del carrito y calcula subtotal, IVA y total
function dibujarCarrito() {
  document.getElementById("btn-carrito").textContent = `Carrito (${carrito.reduce((suma, i) => suma + i.cantidad, 0)})`;

  const lista = document.getElementById("carrito-items");
  lista.replaceChildren();
  if (carrito.length === 0) lista.append(el("p", null, "Todavía no agregaste nada."));

  carrito.forEach((item) => {
    const menos = el("button", null, "−");
    const mas = el("button", null, "+");
    menos.type = mas.type = "button";
    menos.addEventListener("click", () => cambiarCantidad(item, -1));
    mas.addEventListener("click", () => cambiarCantidad(item, 1));
    const cant = el("span", "cant");
    cant.append(menos, el("b", null, String(item.cantidad)), mas);
    const linea = el("div", "linea");
    linea.append(el("span", null, item.nombre), cant, el("span", null, precioAR.format(item.precio * item.cantidad)));
    lista.append(linea);
  });

  const subtotal = carrito.reduce((suma, i) => suma + i.precio * i.cantidad, 0);
  const iva = Math.round(subtotal * 0.21);
  document.getElementById("c-subtotal").textContent = precioAR.format(subtotal);
  document.getElementById("c-iva").textContent = precioAR.format(iva);
  document.getElementById("c-total").textContent = precioAR.format(subtotal + iva);
  document.getElementById("c-comprar").disabled = carrito.length === 0;
}

// Ventana de "Gracias por tu compra" con el detalle de lo que se compró
function mostrarGracias(compra, items) {
  const nombre = usuario && usuario.nombre ? usuario.nombre : "";
  document.getElementById("gracias-sub").textContent =
    (nombre ? nombre + ", tu" : "Tu") + " pedido quedó registrado" + (compra.id ? " (compra #" + compra.id + ")." : ".");

  const lista = document.getElementById("gracias-items");
  lista.replaceChildren(...items.map((i) => {
    const linea = el("div", "linea");
    linea.append(el("span", null, `${i.cantidad} × ${i.nombre}`), el("span", null, precioAR.format(i.precio * i.cantidad)));
    return linea;
  }));

  document.getElementById("g-subtotal").textContent = precioAR.format(compra.subtotal);
  document.getElementById("g-iva").textContent = precioAR.format(compra.iva);
  document.getElementById("g-total").textContent = precioAR.format(compra.total);
  document.getElementById("gracias").showModal();
}

// Al confirmar, se crea una Compra (con su Factura) en el servidor, que verifica y descuenta el stock
async function confirmarCompra() {
  avisoCarrito("");
  try {
    // Un mismo id repetido = una unidad más de esa prenda
    const productosIds = carrito.flatMap((i) => Array.from({ length: i.cantidad }, () => i.id));
    const res = await fetch(ORIGEN + "/api/compras/nueva", {
      method: "POST",
      headers: cabeceras({ "Content-Type": "application/json" }),
      body: JSON.stringify({ productosIds })
    });
    if (res.status === 401) { sesionVencida(); return; }
    if (!res.ok) {
      avisoCarrito((await res.text()) || "No se pudo completar la compra.");
      cargar(); // por si cambió el stock
      return;
    }

    const compra = await res.json(); // subtotal, IVA y total calculados por el servidor
    const items = carrito.map((i) => ({ nombre: i.nombre, cantidad: i.cantidad, precio: i.precio })); // antes de vaciar el carrito
    carrito = [];
    guardarCarrito();
    document.getElementById("carrito").close();
    mostrarGracias(compra, items);
    cargar(); // muestra el stock actualizado
  } catch (error) {
    avisoCarrito("No se pudo completar la compra. ¿Está encendido Spring Boot? " + error.message);
  }
}

document.getElementById("btn-carrito").addEventListener("click", (e) => {
  e.preventDefault();
  avisoCarrito("");
  dibujarCarrito();
  document.getElementById("carrito").showModal();
});
document.getElementById("c-cerrar").addEventListener("click", () => document.getElementById("carrito").close());
document.getElementById("c-comprar").addEventListener("click", confirmarCompra);
const dialogoGracias = document.getElementById("gracias");
document.getElementById("g-cerrar").addEventListener("click", () => dialogoGracias.close());
dialogoGracias.addEventListener("click", (e) => { if (e.target === dialogoGracias) dialogoGracias.close(); }); // clic afuera = cerrar
// ======================================================

document.querySelectorAll(".filtros button").forEach((b) => {
  b.addEventListener("click", () => mostrar(b.dataset.cat));
});
cargar();
dibujarCarrito();