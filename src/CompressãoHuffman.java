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
        arquivo.seek(0); //Apontado para o inicio do arquivo
        Integer metadados = arquivo.readInt(); //Lendo todo o metadado

        /*
         * Leitura do metadados
         * Lendo um int e o convertendo para um char com auxilio de uma String
         */
        String simbolo = metadados.toString();
        char[] c = new char[simbolo.length()];
        for(int i = 0; i < simbolo.length(); i++){
            c[i] = simbolo.charAt(i); //Pegando o símbolo da String e jogando para um char
            inserirNoHash(c[i]); //Inserindo simbolo no Hash
        }
    }

    void inserirNoHash(Character c) {
        if (!frequencia.containsKey(c)) { //Checando se o HashMap já contem o símbolo
            frequencia.put(c, 1); //Como o Hash não contem o símbolo, ele será adicionado com a frequencia de 1
        }
        else{
            Integer valorInt = frequencia.get(c); //Caso o simbolo existe, ele será pego do Hash
            valorInt++; //Aumentando sua frenquencia
            frequencia.put(c, valorInt); //E devolvido com o Int incrementado
        }
    }
}
