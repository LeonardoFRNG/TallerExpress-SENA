package exception;

//Al heredar de RuntimeException, creamos una excepción que podemos lanzar en cualquier momento usando throw new
public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String message) {
        super(message);
    }
}