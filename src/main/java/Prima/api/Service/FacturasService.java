package Prima.api.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import Prima.api.IService.FacturasIService;
import Prima.api.Entidades.Compra;
import Prima.api.Entidades.Comprador;
import Prima.api.Entidades.Facturas;
import Prima.api.Entidades.Productos;
import Prima.api.Repository.FacturasRepository;

@Service
public class FacturasService implements FacturasIService {

    // ===== Datos del emisor (Prisma). Cambialos por los datos reales del negocio =====
    private static final String EMISOR_RAZON = "Prisma · Ropa vintage";
    private static final String EMISOR_CUIT = "30-00000000-0";
    private static final String EMISOR_DOMICILIO = "Sede Central, Mendoza, Argentina";
    private static final String EMISOR_IVA = "Responsable Inscripto";
    private static final String EMISOR_CORREO = "hola@prisma.com";
    private static final String PUNTO_DE_VENTA = "0001";

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Autowired
    private FacturasRepository fRepository;

    public List<Facturas> findAllFacturas() {
        return fRepository.findAll();
    }
    public Facturas saveFacturas(Facturas facturas) {
        return fRepository.save(facturas);
    }

    // ---------- Lista de facturas (la más nueva primero) ----------
    @Transactional(readOnly = true)
    public List<Map<String, Object>> resumenes(Long compradorId) {
        List<Map<String, Object>> lista = new ArrayList<>();
        for (Facturas f : fRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))) {
            Compra compra = f.getCompra();
            Comprador c = compra == null ? null : compra.getComprador();
            if (compradorId != null && (c == null || !compradorId.equals(c.getId()))) continue;

            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", f.getId());
            m.put("numero", numero(f.getId()));
            m.put("fecha", fecha(f));
            m.put("cliente", nombreCliente(f, c));
            m.put("correo", c == null ? null : c.getCorreo());
            m.put("cuit", c == null ? null : formatoCuit(c.getCuit()));
            m.put("prendas", compra == null || compra.getProductos() == null ? 0 : compra.getProductos().size());
            m.put("total", f.getTotal());
            lista.add(m);
        }
        return lista;
    }

    // ---------- Una factura completa ----------
    @Transactional(readOnly = true)
    public Map<String, Object> detalle(Long id) {
        Facturas f = fRepository.findById(id).orElse(null);
        if (f == null || f.getCompra() == null) return null;
        Compra compra = f.getCompra();
        Comprador c = compra.getComprador();

        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", f.getId());
        m.put("numero", numero(f.getId()));
        m.put("fecha", fecha(f));
        m.put("medioPago", f.getMedioPago());
        m.put("compradorId", c == null ? null : c.getId()); // lo usa el controller para controlar el acceso (no se envía al navegador)

        Map<String, Object> emisor = new LinkedHashMap<>();
        emisor.put("razonSocial", EMISOR_RAZON);
        emisor.put("cuit", EMISOR_CUIT);
        emisor.put("domicilio", EMISOR_DOMICILIO);
        emisor.put("condicionIva", EMISOR_IVA);
        emisor.put("correo", EMISOR_CORREO);
        m.put("emisor", emisor);

        Map<String, Object> receptor = new LinkedHashMap<>();
        receptor.put("nombre", nombreCliente(f, c));
        receptor.put("cuit", c == null ? null : formatoCuit(c.getCuit()));
        receptor.put("condicionIva", "Consumidor Final");
        receptor.put("domicilio", c == null ? null : c.getDireccion()); // domicilio de entrega
        receptor.put("correo", c == null ? null : c.getCorreo());
        m.put("receptor", receptor);

        // Los productos se repiten una vez por unidad: los agrupamos en renglones con su cantidad
        Map<Long, Productos> productos = new LinkedHashMap<>();
        Map<Long, Integer> cantidades = new LinkedHashMap<>();
        if (compra.getProductos() != null) {
            for (Productos p : compra.getProductos()) {
                productos.putIfAbsent(p.getId(), p);
                cantidades.merge(p.getId(), 1, Integer::sum);
            }
        }
        List<Map<String, Object>> items = new ArrayList<>();
        for (Map.Entry<Long, Productos> e : productos.entrySet()) {
            Productos p = e.getValue();
            int cantidad = cantidades.get(e.getKey());
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("codigo", p.getId());
            item.put("nombre", p.getNombre());
            item.put("descripcion", p.getDescripcion());
            item.put("categoria", p.getCategoria());
            item.put("imagen", p.getURLimagen());
            item.put("cantidad", cantidad);
            item.put("precioUnitario", p.getPrecio());
            item.put("importe", p.getPrecio() * cantidad);
            items.add(item);
        }
        m.put("items", items);

        m.put("subtotal", f.getSubtotal());
        m.put("alicuotaIva", 21);
        m.put("iva", f.getIva());
        m.put("total", f.getTotal());
        return m;
    }

    // ---------- Ayudas ----------

    // 0001-00000012 (punto de venta + número de comprobante)
    private String numero(Long id) {
        return String.format("%s-%08d", PUNTO_DE_VENTA, id);
    }

    private String fecha(Facturas f) {
        return f.getFecha() == null ? null : f.getFecha().format(FORMATO_FECHA);
    }

    private String nombreCliente(Facturas f, Comprador c) {
        if (f.getNomcomprador() != null && !f.getNomcomprador().isBlank()) return f.getNomcomprador();
        if (c == null) return null;
        String nombre = c.getNombre() == null ? "" : c.getNombre();
        String apellido = c.getApellido() == null ? "" : c.getApellido();
        return (nombre + " " + apellido).trim();
    }

    // 20-12345678-9
    private String formatoCuit(Long cuit) {
        if (cuit == null) return null;
        String s = String.valueOf(cuit);
        if (s.length() != 11) return s;
        return s.substring(0, 2) + "-" + s.substring(2, 10) + "-" + s.substring(10);
    }
}
