"use strict";
// Se usa en index.html y nosotros.html: si no iniciaste sesión, te manda al login.

let usuario = null;
try { usuario = JSON.parse(localStorage.getItem("prismaUsuario")); } catch { usuario = null; }

if (!usuario) {
  location.replace("login.html");
}

document.addEventListener("DOMContentLoaded", () => {
  const saludo = document.getElementById("saludo");
  if (saludo && usuario) saludo.textContent = `Hola, ${usuario.nombre}`;

  const salir = document.getElementById("btn-salir");
  if (salir) {
    salir.addEventListener("click", (e) => {
      e.preventDefault();
      localStorage.removeItem("prismaUsuario");
      location.href = "login.html";
    });
  }
});
