package Prima.api.Entidades;

import java.util.List;
import jakarta.persistence.*;

@Entity
public class Facturas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nomcli;
    private int iva;
    private float subtotal;
    private float total;

    @OneToOne(mappedBy = "factura")
    private Compra compra;

    public Facturas(Long id, String nomcli, int iva, float subtotal, float total, Compra compra) {
        this.id = id;
        this.nomcli = nomcli;
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

    public String getNomcli() {
        return nomcli;
    }

    public void setNomcli(String nomcli) {
        this.nomcli = nomcli;
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
