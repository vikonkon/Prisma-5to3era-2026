"use strict";

const API = location.port === "8080" ? "/api/comprador" : "http://localhost:8080/api/comprador";
const $ = (id) => document.getElementById(id);

// Si ya tenés la sesión iniciada, vas directo al inicio
if (localStorage.getItem("prismaUsuario")) location.replace("index.html");

function mostrarVista(vista) {
  $("form-login").hidden = vista !== "login";
  $("form-registro").hidden = vista !== "registro";
  $("msg").hidden = true;
}

function aviso(texto, ok = false) {
  $("msg").textContent = texto;
  $("msg").className = ok ? "msg ok" : "msg";
  $("msg").hidden = false;
}

async function textoDeError(res) {
  if (res.status >= 500) return "Error del servidor. Mirá la consola de Spring Boot.";
  return await res.text();
}

function pedir(ruta, datos) {
  return fetch(API + ruta, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(datos)
  });
}

$("ir-registro").addEventListener("click", (e) => { e.preventDefault(); mostrarVista("registro"); });
$("ir-login").addEventListener("click", (e) => { e.preventDefault(); mostrarVista("login"); });

$("form-login").addEventListener("submit", async (e) => {
  e.preventDefault();
  try {
    const res = await pedir("/login", {
      correo: $("login-correo").value.trim(),
      contrasena: $("login-clave").value
    });
    if (res.ok) {
      localStorage.setItem("prismaUsuario", JSON.stringify(await res.json()));
      location.href = "index.html";
    } else {
      aviso(await textoDeError(res));
    }
  } catch {
    aviso("No hay conexión con el servidor. ¿Está encendido Spring Boot?");
  }
});

$("form-registro").addEventListener("submit", async (e) => {
  e.preventDefault();
  try {
    const correo = $("reg-correo").value.trim();
    const res = await pedir("", {
      nombre: $("reg-nombre").value.trim(),
      apellido: $("reg-apellido").value.trim(),
      correo: correo,
      contrasena: $("reg-clave").value,
      direccion: $("reg-direccion").value.trim(),
      cuit: Number($("reg-cuit").value)
    });
    if (res.ok) {
      $("form-registro").reset();
      mostrarVista("login");
      $("login-correo").value = correo;
      aviso("¡Cuenta creada! Ahora ingresá con tu contraseña.", true);
    } else {
      aviso(await textoDeError(res));
    }
  } catch {
    aviso("No hay conexión con el servidor. ¿Está encendido Spring Boot?");
  }
});
