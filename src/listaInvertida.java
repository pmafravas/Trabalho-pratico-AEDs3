import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

public class listaInvertida {

    driverNode pilotos = new driverNode(); //Classe pilotos para utilização dentro do código para tratamento da leitura
    String pathOrigem = "src/data/driversDB.db"; //String contendo caminho da base de dados para realizar leitura    
    RandomAccessFile arquivoOrigem;

    Map<String, Integer> contagemNomes = new HashMap<>(); //Mapa servirá para contabilizar a incidencia dos nomes, podendo ser atualizada sempre que necessario

    void criarListaNome(){
        try {
            
            arquivoOrigem = new RandomAccessFile(pathOrigem, "rw"); //Abrindo arquivo para leitura
            int quantidadeIDs = 0;
            int IDloop = 0;

            quantidadeIDs = arquivoOrigem.readInt(); //Lendo a quantidade de IDs, provisionado pelo metadados

            while (IDloop >= quantidadeIDs) {
                if(arquivoOrigem.readChar() != '*'){ //Checando lápide
                    /*
                    * Primeiro, será feito a leitura do arquivo
                    * e carregado seus dados para a classe piloto
                    */
 
                    byte[] ba = new byte[arquivoOrigem.readInt()]; //Lê tamanho do registro e cria um novo vetor com o mesmo tamanho
                    arquivoOrigem.readFully(ba); //Lê todo o registro e escreve dentro do byte array
                    pilotos.fromByteArray(ba); //Transcrevendo o byte array para classe piloto
 
                    /*
                    * Em seguida é feita a inserção do nome na Lista
                    * Será utilizado um Map para manter
                    * conta da incidencia dos nomes inseridos
                    */
                    
                    
                }   
                else{
                    arquivoOrigem.skipBytes(arquivoOrigem.readInt()); //Se estiver deletado, pula X bytes para mudar de registro, como indicado no inicio do registro.
                } 
            }
        } 
        catch (IOException e) {
            System.out.println("Houve um erro ao tentar abrir o arquivo driversDB:");
            e.printStackTrace();
        } 

    }

    void pesquisarListaNome(){

    }

}
    