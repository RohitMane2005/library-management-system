package library.model;

import java.io.Serializable;
import java.util.Objects;

public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;
    private int quantity;

    public Book(String name, int quantity) {
        this.name = name.trim().toUpperCase();
        this.quantity = Math.max(0, quantity);
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void addCopy() {
        quantity++;
    }

    public boolean issueCopy() {
        if (quantity > 0) {
            quantity--;
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return name.equals(book.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name + " (Qty: " + quantity + ")";
    }
}
