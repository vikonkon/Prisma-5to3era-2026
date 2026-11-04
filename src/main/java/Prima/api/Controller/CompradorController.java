package Prima.api.Controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import Prima.api.IService.CompradorIService;
import Prima.api.Entidades.Comprador;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.Map;
import org.springframework.http.ResponseEntity;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/comprador")
public class CompradorController {
@Autowired
private CompradorIService aService;
@GetMapping
public List<Comprador> getAllComprador() {
return aService.findAllComprador();
}
@PostMapping
public ResponseEntity<?> createComprador(@RequestBody Comprador comprador) {
    // Si ya hay alguien con ese correo, no lo dejamos registrarse de nuevo
    if (aService.buscarPorCorreo(comprador.getCorreo()) != null) {
        return ResponseEntity.status(409).body("Ese correo ya está registrado");
    }
        aService.saveComprador(comprador);
    return ResponseEntity.ok("Comprador registrado");
}

@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody Comprador datos) {
    Comprador c = aService.buscarPorCorreo(datos.getCorreo());
    // Si no existe el correo o la contraseña no coincide, devolvemos error 401
    if (c == null || !datos.getContrasena().equals(c.getContrasena())) {
        return ResponseEntity.status(401).body("Correo o contraseña incorrectos");
    }
    // Devolvemos solo algunos datos (nunca la contraseña)
    return ResponseEntity.ok(Map.of(
        "id", c.getId(),
        "nombre", c.getNombre(),
        "correo", c.getCorreo()
    ));
}
}