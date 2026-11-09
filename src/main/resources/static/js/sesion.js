"use strict";
// Se usa en index.html y nosotros.html: si no iniciaste sesión, te manda al login.

let usuario = null;
try { usuario = JSON.parse(localStorage.getItem("prismaUsuario")); } catch { usuario = null; }

// Una sesión vieja (sin token) ya no sirve: hay que ingresar de nuevo para obtener rol y token
if (usuario && !usuario.token) {
  localStorage.removeItem("prismaUsuario");
  usuario = null;
}

if (!usuario) {
  location.replace("login.html");
}

// ¿Esta cuenta es gerente? (el rol viene de la base de datos a través del login)
const esGerente = !!usuario && String(usuario.rol || "").trim().toUpperCase() === "GERENTE";

// Headers para las peticiones que necesitan sesión (agregar/editar productos, comprar)
function cabeceras(extra) {
  return Object.assign({ Authorization: "Bearer " + (usuario ? usuario.token : "") }, extra);
}

// Si el servidor dice 401 (por ejemplo, se reinició Spring Boot), volvemos a pedir login
function sesionVencida() {
  localStorage.removeItem("prismaUsuario");
  location.replace("login.html");
}

document.addEventListener("DOMContentLoaded", () => {
  const saludo = document.getElementById("saludo");
  if (saludo && usuario) saludo.textContent = `Hola, ${usuario.nombre}` + (esGerente ? " (Gerente)" : "");

  const salir = document.getElementById("btn-salir");
  if (salir) {
    salir.addEventListener("click", (e) => {
      e.preventDefault();
      localStorage.removeItem("prismaUsuario");
      location.href = "login.html";
    });
  }
});