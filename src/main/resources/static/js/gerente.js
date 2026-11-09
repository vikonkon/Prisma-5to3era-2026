"use strict";
// Panel del gerente: renovar stock, agregar y editar productos (con la URL de su imagen).
// Solo se activa si la cuenta tiene rol GERENTE en la base de datos.
// Usa cosas de sesion.js (esGerente, cabeceras, sesionVencida) y de productos.js (todos, cargar, API_PRODUCTOS).

(function () {
  const panel = document.getElementById("panel-gerente");
  if (!panel || !esGerente) return;
  panel.hidden = false;

  const $ = (id) => document.getElementById(id);

  function aviso(texto, ok = false) {
    const msg = $("gerente-msg");
    msg.textContent = texto;
    msg.style.background = ok ? "var(--foam)" : "";
    msg.hidden = !texto;
  }

  // Hace la petición con el token del gerente y traduce los errores a mensajes claros
  async function llamar(ruta, metodo, cuerpo) {
    const res = await fetch(API_PRODUCTOS + ruta, {
      method: metodo,
      headers: cabeceras({ "Content-Type": "application/json" }),
      body: JSON.stringify(cuerpo)
    });
    if (res.status === 401) { sesionVencida(); throw new Error("Tu sesión venció, ingresá de nuevo."); }
    if (res.status === 403) throw new Error("Tu cuenta no tiene permisos de gerente.");
    if (!res.ok) throw new Error((await res.text()) || "Error " + res.status);
    return res;
  }

  // Llena el desplegable de "Reponer stock" con los productos actuales
  window.alCargarProductos = function () {
    const select = $("rep-producto");
    const elegido = select.value;
    select.replaceChildren(...todos.map((p) => {
      const o = document.createElement("option");
      o.value = p.id;
      o.textContent = `#${p.id} · ${p.nombre} (stock: ${p.cantidad})`;
      return o;
    }));
    if (elegido) select.value = elegido;
  };

  // ---------- Renovar stock ----------
  $("form-reponer").addEventListener("submit", async (e) => {
    e.preventDefault();
    aviso("");
    try {
      const unidades = parseInt($("rep-cantidad").value, 10);
      await llamar(`/${$("rep-producto").value}/stock`, "PATCH", { cantidad: unidades });
      $("rep-cantidad").value = "";
      aviso(`Stock actualizado: se sumaron ${unidades} unidades.`, true);
      await cargar();
    } catch (error) {
      aviso(error.message);
    }
  });

  // ---------- Agregar / editar producto ----------
  function salirDeEdicion() {
    $("form-producto").reset();
    $("prod-id").value = "";
    $("prod-titulo").textContent = "Agregar producto";
    $("prod-guardar").textContent = "Agregar producto";
    $("prod-cancelar").hidden = true;
    $("prod-eliminar").hidden = true;
  }

  // La llama el botón "Editar producto" de cada tarjeta
  window.editarProducto = function (p) {
    $("prod-id").value = p.id;
    $("prod-nombre").value = p.nombre || "";
    $("prod-precio").value = p.precio;
    $("prod-descripcion").value = p.descripcion || "";
    $("prod-cantidad").value = p.cantidad;
    $("prod-categoria").value = p.categoria || "";
    $("prod-imagen-url").value = p.urlimagen ?? p.URLimagen ?? "";
    $("prod-titulo").textContent = `Editar producto #${p.id}`;
    $("prod-guardar").textContent = "Guardar cambios";
    $("prod-cancelar").hidden = false;
    $("prod-eliminar").hidden = false;
    aviso("");
    panel.scrollIntoView({ behavior: "smooth", block: "start" });
  };

  // Eliminar: el producto deja de aparecer en la tienda
  $("prod-eliminar").addEventListener("click", async () => {
    const id = $("prod-id").value;
    if (!id || !confirm("¿Eliminar este producto? Va a dejar de aparecer en la tienda.")) return;
    aviso("");
    try {
      await llamar(`/${id}`, "DELETE");
      salirDeEdicion();
      aviso("Producto eliminado.", true);
      await cargar();
    } catch (error) {
      aviso(error.message);
    }
  });

  $("prod-cancelar").addEventListener("click", () => { salirDeEdicion(); aviso(""); });

  $("form-producto").addEventListener("submit", async (e) => {
    e.preventDefault();
    aviso("");
    try {
      const urlImagen = $("prod-imagen-url").value.trim();
      if (!$("prod-categoria").value) throw new Error("Elegí una categoría.");

      const producto = {
        nombre: $("prod-nombre").value.trim(),
        precio: Number($("prod-precio").value),
        descripcion: $("prod-descripcion").value.trim(),
        urlimagen: urlImagen,
        categoria: $("prod-categoria").value,
        cantidad: parseInt($("prod-cantidad").value, 10)
      };

      const id = $("prod-id").value;
      await llamar(id ? `/${id}` : "", id ? "PUT" : "POST", producto);
      aviso(id ? "Producto actualizado." : "Producto agregado.", true);
      salirDeEdicion();
      await cargar();
    } catch (error) {
      aviso(error.message);
    }
  });

  window.alCargarProductos();
})();