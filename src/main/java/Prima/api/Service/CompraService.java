package Prima.api.Service;

import Prima.api.Entidades.Compra;
import Prima.api.Entidades.Comprador;
import Prima.api.Entidades.Facturas;
import Prima.api.Entidades.Productos;
import Prima.api.Repository.CompraRepository;
import Prima.api.Repository.ProductosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompraService {

    @Autowired
    private CompraRepository compraRepository;
    @Autowired
    private ProductosRepository productoRepository;

    // El comprador ya viene verificado desde el token de sesión (no se confía en lo que mande el navegador).
    // Recibe solo los ids de los productos: un mismo id repetido = una unidad más.
    @Transactional
    public Compra registrarCompra(List<Long> productosIds, Comprador comprador) {

        if (productosIds == null || productosIds.isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }

        List<Productos> productosReales = new ArrayList<>();
        float subtotal = 0;

        // 1. Recorrer los ids que vienen del frontend
        for (Long idProducto : productosIds) {
            if (idProducto == null) {
                throw new RuntimeException("Hay un producto inválido en el carrito");
            }

            Productos productoReal = productoRepository.findById(idProducto)
                    .orElseThrow(() -> new RuntimeException("Producto con ID " + idProducto + " no existe"));

            if (Boolean.FALSE.equals(productoReal.getActivo())) {
                throw new RuntimeException("El producto " + productoReal.getNombre() + " ya no está disponible");
            }

            // 2. Verificar y bajar el stock (una unidad por cada aparición del producto)
            if (productoReal.getCantidad() > 0) {
                productoReal.setCantidad(productoReal.getCantidad() - 1);
                productoRepository.save(productoReal);
                productosReales.add(productoReal);
                subtotal += productoReal.getPrecio(); // el precio sale de la base, no del navegador
            } else {
                // Si entra acá, @Transactional cancela todo: no se cobra nada ni baja stock
                throw new RuntimeException("No hay stock suficiente de: " + productoReal.getNombre());
            }
        }

        // 3. Totales calculados por el servidor
        int iva = Math.round(subtotal * 0.21f);
        float total = subtotal + iva;

        Compra compra = new Compra();
        compra.setComprador(comprador);
        compra.setProductos(productosReales);
        compra.setSubtotal(subtotal);
        compra.setIva(iva);
        compra.setTotal(total);

        // 4. La factura de esta compra (se guarda sola gracias al cascade de Compra)
        String nombre = comprador.getNombre() == null ? "" : comprador.getNombre();
        String apellido = comprador.getApellido() == null ? "" : comprador.getApellido();
        Facturas factura = new Facturas();
        factura.setNomcomprador((nombre + " " + apellido).trim());
        factura.setSubtotal(subtotal);
        factura.setIva(iva);
        factura.setTotal(total);
        factura.setCompra(compra);
        compra.setFactura(factura);

        return compraRepository.save(compra);
    }
}
