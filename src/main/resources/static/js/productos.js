"use strict";

const API_PRODUCTOS = location.port === "8080" ? "/api/productos" : "http://localhost:8080/api/productos";
const grid = document.getElementById("grid");
const contador = document.getElementById("contador");
const precioAR = new Intl.NumberFormat("es-AR", { style: "currency", currency: "ARS", maximumFractionDigits: 0 });
let todos = [];

// Crea un elemento HTML con clase y texto (más corto que escribirlo cada vez)
function el(tag, clase, texto) {
  const e = document.createElement(tag);
  if (clase) e.className = clase;
  if (texto !== undefined) e.textContent = texto;
  return e;
}

// Decide la categoría mirando cómo empieza el nombre del producto
function categoria(p) {
  const n = (p.nombre || "").toLowerCase();
  if (n.startsWith("remera")) return "remeras";
  if (n.startsWith("pantal")) return "pantalones";
  if (n.startsWith("zapatilla")) return "calzado";
  return "abrigos"; // camperas y buzos
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
    mostrar("todo");
  } catch (error) {
    mostrarEstado("No pudimos cargar la colección", "Verificá que Spring Boot esté encendido. Detalle: " + error.message);
  }
}

function mostrar(cat) {
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

  const boton = el("button", "btn", "Comprar");
  boton.type = "button";
  boton.disabled = !(cant > 0);
  boton.addEventListener("click", () => comprarProducto(p));

  const cuerpo = el("div", "cuerpo");
  cuerpo.append(
    el("h3", null, p.nombre),
    el("p", "desc", p.descripcion || ""),
    el("p", "stock", cant > 0 ? `${cant} en stock` : "Sin stock"),
    el("p", "precio", precioAR.format(Number(p.precio))),
    boton
  );

  tarjeta.append(foto, cuerpo);
  return tarjeta;
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
}

// Se llama al tocar "Comprar" en una prenda: la agrega al carrito
function comprarProducto(producto) {
  const item = carrito.find((i) => i.id === producto.id);
  if (item) {
    if (item.cantidad >= producto.cantidad) { alert("No hay más stock de esta prenda."); return; }
    item.cantidad++;
  } else {
    carrito.push({ id: producto.id, nombre: producto.nombre, precio: Number(producto.precio), cantidad: 1, stock: producto.cantidad });
  }
  guardarCarrito();
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

// Al confirmar la compra, lo único que se guarda en la base de datos es el stock nuevo
async function confirmarCompra() {
  avisoCarrito("");
  try {
    // 1) Pedimos a la base de datos el stock actual
    const res = await fetch(API_PRODUCTOS);
    if (!res.ok) throw new Error("Error " + res.status);
    const actuales = await res.json();

    // 2) Verificamos que alcance el stock de cada prenda
    for (const item of carrito) {
      const p = actuales.find((x) => x.id === item.id);
      if (!p || p.cantidad < item.cantidad) {
        avisoCarrito(`No hay stock suficiente de ${item.nombre}.`);
        return;
      }
    }

    // 3) Guardamos el stock nuevo de cada prenda (POST con un id existente actualiza el producto)
    for (const item of carrito) {
      const p = actuales.find((x) => x.id === item.id);
      p.cantidad = p.cantidad - item.cantidad;
      const r = await fetch(API_PRODUCTOS, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(p)
      });
      if (!r.ok) throw new Error("Error " + r.status);
    }

    // 4) Listo: vaciamos el carrito y mostramos el resumen
    const subtotal = carrito.reduce((suma, i) => suma + i.precio * i.cantidad, 0);
    const iva = Math.round(subtotal * 0.21);
    carrito = [];
    guardarCarrito();
    document.getElementById("carrito").close();
    alert(`¡Compra realizada!\nSubtotal: ${precioAR.format(subtotal)}\nIVA: ${precioAR.format(iva)}\nTotal: ${precioAR.format(subtotal + iva)}`);
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
// ======================================================

document.querySelectorAll(".filtros button").forEach((b) => {
  b.addEventListener("click", () => mostrar(b.dataset.cat));
});
cargar();
dibujarCarrito();
