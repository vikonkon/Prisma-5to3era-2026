package Prima.api.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import Prima.api.IService.ProductosIService;
import Prima.api.Entidades.Productos;
import Prima.api.Repository.ProductosRepository;

@Service
public class ProductosService implements ProductosIService{
    @Autowired
    private ProductosRepository pRepository;
    // La tienda solo muestra los productos que no fueron eliminados (activo = null cuenta como activo)
    public List<Productos> findAllProductos() {
        return pRepository.findAll().stream()
                .filter(p -> !Boolean.FALSE.equals(p.getActivo()))
                .toList();
    }
    public Productos saveProductos(Productos producto) {
        return pRepository.save(producto);
    }
    public Optional<Productos> findById(Long id) {
        return pRepository.findById(id);
    }

    // Suma unidades al stock actual (lo usa el gerente para renovar stock)
    @Transactional
    public Productos reponerStock(Long id, int unidades) {
        Productos p = pRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El producto " + id + " no existe"));
        p.setCantidad(p.getCantidad() + unidades);
        return pRepository.save(p);
    }

    // Al iniciar: a los productos viejos que quedaron sin categoría les guarda una, UNA SOLA VEZ
    // (después se cambia a mano con "Editar producto"). Si no se reconoce el nombre, queda sin categoría.
    @EventListener(ApplicationReadyEvent.class)
    public void completarCategorias() {
        for (Productos p : pRepository.findAll()) {
            if (p.getCategoria() != null && !p.getCategoria().isBlank()) continue;
            String cat = deducirCategoria(p.getNombre());
            if (cat != null) {
                p.setCategoria(cat);
                pRepository.save(p);
            }
        }
    }

    private String deducirCategoria(String nombre) {
        String n = Normalizer.normalize(nombre == null ? "" : nombre.toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        String[] palabras = n.split("[^a-z0-9]+");
        if (tiene(palabras, "zapatilla", "zapato", "bota", "borcego", "botin", "sandalia", "ojota",
                "mocasin", "alpargata", "championes", "chancla", "pantufla")) return "calzado";
        if (tiene(palabras, "remera", "top", "musculosa", "camiseta", "camisa", "blusa", "chomba")) return "remeras";
        if (tiene(palabras, "pantal", "jean", "short", "bermuda", "jogger", "cargo", "calza", "babucha", "pollera")) return "pantalones";
        if (tiene(palabras, "campera", "buzo", "sweater", "tapado", "chaleco", "abrigo", "hoodie", "saco", "rompevientos")) return "abrigos";
        return null;
    }

    private boolean tiene(String[] palabras, String... claves) {
        for (String palabra : palabras)
            for (String clave : claves)
                if (palabra.startsWith(clave)) return true;
        return false;
    }
}
