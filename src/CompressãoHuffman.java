import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class CompressãoHuffman {
    HashMap<Character, Integer> frequencia = new HashMap<>(); //Será utilizado para medir a frequencia de todas as letras e numeros
    driverNode pilotos = new driverNode();
    String dbPath = "src/data/driversDB.db";
    RandomAccessFile arquivo;
    NoDeHuffman raiz;

    void comprimir(){
        contagemDeSimbolos(); //Primeiro será contabilizado todos os simbolos do arquivo e construido uma arvore binaria de huffman

    }
    
    void contagemDeSimbolos() {
        try{
            arquivo = new RandomAccessFile(dbPath, "rw"); //Abrindo arquivo para realizar contabilização
            arquivo.seek(0); //Apontado para o inicio do arquivo
            Integer metadados = arquivo.readInt(); //Lendo todo o metadado
            Integer IDloop = 1;

            intToHash(metadados); //Inserindo metadados 

            //Leitura do arquivo pós metadados
            while (IDloop <= metadados){
                try{
                    if(arquivo.readChar() != '*'){ //Verificando se será um registro morto. Caso seja, não iremos comprimir pois será espaço inutilizado
                        int tamanhoRegistro = arquivo.readInt(); //Guardando tamanho do registro para enviar ao HashMap
                        byte[] ba = new byte[tamanhoRegistro]; //Criando byte array do tamanho do registro
                        arquivo.readFully(ba); //Lendo todo o registro de acordo com a quantidade de bytes
                        pilotos.fromByteArray(ba); //Extrai o objeto do vetor de btyes

                        pilotosToHash(pilotos); //Inserindo todos os dados dentro do Hash
                    }
                    else{
                        arquivo.skipBytes(arquivo.readInt()); //Pulando registro morto
                    }
                    IDloop++;
                }
                catch (EOFException e) {
                    break;
                }
            }

            //Após o fim do carregamento de todos os pilotos, eles serão inseridos na Árvore de Huffman
            raiz = construirArvoreHuffman(frequencia);
        }
        catch (IOException e){
            System.out.println("Houve um erro ao tentar criar HashMap de incidencia:");
            e.printStackTrace();
        }
    }

    /**
     * Método para construir a árvore binária com base na frequencia
     * 
     * <p>Método utiliza de um PriorityQueue para ordenar todas as entradas e em seguida cria NósDeHuffman
     * para combina-los e montar uma só árvore
     * 
     * @param frequencia - HashMap com todos os simbolos do arquivo contabilizados
     * @return <b>filaPrioritaria.poll()</b> - Árvore binária já balanceada
     */
    NoDeHuffman construirArvoreHuffman(HashMap<Character, Integer> frequencia){
        PriorityQueue<NoDeHuffman> filaPrioritaria = new PriorityQueue<>();

        //Iterando sobre todas entradas do HashMap
        for (Map.Entry<Character, Integer> entry : frequencia.entrySet()){  
            //Recuperando informações
            char simbolo = entry.getKey();
            int freq = entry.getValue();

            //Inserindo na filaPrioritaria
            filaPrioritaria.add(new NoDeHuffman(simbolo, freq));
        }

        //Montando a arvoré binária de Huffman com base na ordem da fila prioritaria
        while (filaPrioritaria.size() > 1) {
            NoDeHuffman esq = filaPrioritaria.poll();
            NoDeHuffman dir = filaPrioritaria.poll();
            NoDeHuffman combinado = new NoDeHuffman(esq.frequencia + dir.frequencia, esq, dir); //Unindo valores de frequencia e designando cada folha
            filaPrioritaria.add(combinado);
        }

        return filaPrioritaria.poll();
    }
    
    /**
     * Algoritmo para converter um Int para uma sequência de char
     * 
     * <p>A conversão de Int para char permite que o número seja inserido no HashMap para a contabilização de sua frequencia
     * @param n com o numero a ser convertido
     */
    void intToHash(Integer n) {
        String simbolo = n.toString();
        char[] c = new char[simbolo.length()];
        for(int i = 0; i < simbolo.length(); i++){
            c[i] = simbolo.charAt(i); //Pegando o símbolo da String e jogando para um char
            inserirNoHash(c[i]); //Inserindo simbolo no Hash
        }
    }

    /**
     * Algoritmo par converter uma String em uma sequencia de Char
     * 
     * <p>A conversão da String para char permite com que seja contabilizado todas as letras da String de maneira
     * que seja possível serem inseridas no HashMap
     * @param simbolo
     */
    void stringToHash(String simbolo){
        char []c = new char[simbolo.length()];
        for(int i = 0; i < simbolo.length(); i++){
            c[i] = simbolo.charAt(i); //Pegando o símbolo da String e jogando para um char
            inserirNoHash(c[i]); //Inserindo simbolo no Hash
        }
    }

    /**
     * Algoritmo para inserir char no Hash
     * 
     * <p>Será conferido se o simbolo já existe dentro  do HashMap, caso exista ele será incrementado,
     * caso não existe, será inserido com a frequencia base de 1.
     * @param c
     */
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


    /**
     * Método para converter todos os dados de um piloto para Simbolos dentro do hash
     * @param pilotos
     */
    void pilotosToHash(driverNode pilotos){

        /*
         * Todas as classes de driverNode:
         *  public int ID;
         *  private String reference;
         *  private String name;
         *  private String surname; 
         *  private String nationality;
         *  private String driverNum;
         *  private LocalDate date;
         *  private String code;
         */

        intToHash(pilotos.getID());
        stringToHash(pilotos.getReference());
        stringToHash(pilotos.getName());         
        stringToHash(pilotos.getSurname());
        stringToHash(pilotos.getNatiotanlity());
        stringToHash(pilotos.getDriverNumber());
        stringToHash(pilotos.getCode());
        stringToHash(pilotos.getDate().toString());
         
    }

    void codificacaoDeHuffman(){

    }
}
