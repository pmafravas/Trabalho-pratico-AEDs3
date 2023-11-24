import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.PriorityQueue;

public class CompressãoHuffman {
    HashMap<Character, Integer> frequencia = new HashMap<>(); //Será utilizado para medir a frequencia de todas as letras e numeros
    driverNode pilotos = new driverNode();
    String dbPath = "src/data/driversDB.db";
    RandomAccessFile arquivo;
    PriorityQueue filaPrioritaria = new PriorityQueue<>();
    
    void contagemDeSimbolos() throws IOException {
        arquivo = new RandomAccessFile(dbPath, "rw"); //Abrindo arquivo para realizar contabilização
        
    }
}
