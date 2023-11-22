import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class listaInvertida {

    driverNode pilotos = new driverNode(); //Classe pilotos para utilização dentro do código para tratamento da leitura
    String pathOrigem = "src/data/driversDB.db"; //String contendo caminho da base de dados para realizar leitura    
    RandomAccessFile arquivoOrigem;

    Map<String, Integer> contagemNomes = new HashMap<>(); //Mapa servirá para contabilizar a incidencia dos nomes, podendo ser atualizada sempre que necessario

    
    void criarListaNome(){
        try {
            
            arquivoOrigem = new RandomAccessFile(pathOrigem, "rw"); //Abrindo arquivo para leitura
            int quantidadeIDs = 0;
            int IDloop = 1;

            quantidadeIDs = arquivoOrigem.readInt(); //Lendo a quantidade de IDs, provisionado pelo metadados

            while (IDloop < quantidadeIDs) {
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
                    if(contagemNomes.containsKey(pilotos.getName())){ //Checando se o nome do piloto já foi inserido
                        contagemNomes.put(pilotos.getName(), contagemNomes.get(pilotos.getName()) + 1); //Caso já exista, será reinsirindo o piloto com o seu valor int incrementado
                    }
                    else {
                        contagemNomes.put(pilotos.getName(), 1); //Se for a primeira vez que o nome é inserido, sua incidencia será 1
                    }
                }   
                else{
                    arquivoOrigem.skipBytes(arquivoOrigem.readInt()); //Se estiver deletado, pula X bytes para mudar de registro, como indicado no inicio do registro.
                    IDloop++; //Pula o ID atual
                } 
            }
        } 
        catch (EOFException e){} //Resolução para o erro EOF ao terminar de ler o arquivo todo
        
        catch (IOException e) {
            System.out.println("Houve um erro ao tentar abrir o arquivo driversDB:");
            e.printStackTrace();
        } 
        System.out.println("Lista criada com sucesso!\n");
    }

    void pesquisarListaNome(){
        
    }

    
    void imprimirListaInvertida(){
        //Verificando se o Map foi carregado com
        if (contagemNomes.size() == 0) {
            System.out.println("Crie uma lista invertida antes de imprimir!");
            
        }
        else{
            Map<String, Integer> mapaOrdenado = contagemNomes.entrySet().stream() // Criando uma Stream das entradas do mapa
                .sorted(Map.Entry.comparingByValue()) // Ordena as entradas do Stream comparando ao Map.Entry
                .collect(Collectors.toMap( // Coleta os resultados da ordenação em um OUTRO Map
                    Map.Entry::getKey, // Obtendo a String
                    Map.Entry::getValue, // Obtendo o int
                    (oldValue, newValue) -> oldValue, // Resolução de qualquer conflito caso apareçam chaves iguais
                    LinkedHashMap::new)); // Criação do novo Map para o recebimento da Stream ordenada

            //Impressão das String ordenadas
            System.out.println("Incidencia dos nomes:");
            for(Map.Entry<String, Integer> entry : mapaOrdenado.entrySet()){
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }
    }

}
    