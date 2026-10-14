package Prima.api.Entidades;

import java.util.List;
import jakarta.persistence.*;

@Entity
public class Facturas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nomcomprador;
    private int iva;
    private float subtotal;
    private float total;

    @OneToOne(mappedBy = "factura")
    private Compra compra;

    public Facturas(Long id, String nomcomprador, int iva, float subtotal, float total, Compra compra) {
        this.id = id;
        this.nomcomprador = nomcomprador;
        this.iva = iva;
        this.subtotal = subtotal;
        this.total = total;
        this.compra = compra;
    }

    public Facturas(){   
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomcomprador() {
        return nomcomprador;
    }

    public void setNomcomprador(String nomcomprador) {
        this.nomcomprador = nomcomprador;
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

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }
   
}
