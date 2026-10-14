package Prima.api.Entidades;

import java.util.List;
import jakarta.persistence.*;
import java.util.ArrayList;


@Entity
public class Compra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int iva;
    private float subtotal;
    private float total;

    @ManyToMany
    @JoinTable(name = "compra_productos", joinColumns = @JoinColumn(name = "compra_id"), inverseJoinColumns = @JoinColumn(name = "producto_id"))
    private List<Productos> productos;

    @ManyToOne
    @JoinColumn(name = "comprador_id")
    private Comprador comprador;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "factura_id", referencedColumnName = "id")
    private Facturas factura;

   public Compra(Long id, int iva, float subtotal, float total, List<Productos> productos, Comprador comprador, Facturas factura) {
    this.id = id;
    this.iva = iva;
    this.subtotal = subtotal;
    this.total = total;
    this.productos = productos;
    this.comprador = comprador;
    this.factura = factura;
}
    public Compra() {
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getIva() {
        return iva;
    }

    public void setIva(int iva) {
        this.iva = iva;
    }

    public float getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(float subtotal) {
        this.subtotal = subtotal;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public List<Productos> getProductos() {
        return productos;
    }

    public void setProductos(List<Productos> productos) {
        this.productos = productos;
    }

    public Comprador getComprador() {
        return comprador;
    }

    public void setComprador(Comprador comprador) {
        this.comprador = comprador;
    }

    public Facturas getFactura() {
        return factura;
    }

    public void setFactura(Facturas factura) {
        this.factura = factura;
    }


}

