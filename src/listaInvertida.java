import java.io.EOFException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class listaInvertida {

    driverNode pilotos = new driverNode(); //Classe pilotos para utilização dentro do código para tratamento da leitura
    String pathOrigem = "src/data/driversDB.db"; //String contendo caminho da base de dados para realizar leitura    
    RandomAccessFile arquivoOrigem;

    Map<String, DadosNome> contagemNomes; //Mapa servirá para contabilizar a incidencia dos nomes, podendo ser atualizada sempre que necessario

    listaInvertida(){
        this.contagemNomes = new HashMap<>();
    }

    /**
     * Método de criação da lista invertida 
     * <p>É aberto o arquivo driversDB.db para realizar a leitura atualizada de todos os pilotos existentes, após a leitura
     * é inserido dentro de um HashMap os nomes, e caso um nome já tenha sido inserido, sua incidencia apenas será incrementada
     * 
     * @see HashMap
     * @see DadosNome
     * @see ArrayList
     * 
     */
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
                    * 
                    */

                    DadosNome novoIndice = this.contagemNomes.get(pilotos.getName()); //Verificando se contagemNomes já possui uma chave com o nome atual
                    if(novoIndice == null){ //Caso esteja vazio, significa que não existe o nome
                        this.contagemNomes.put(pilotos.getName(), new DadosNome(pilotos.getID())); //É adicionado a classe contagemNomes, o novo nome
                    }
                    else{
                        novoIndice.addLocalID(pilotos.getID()); //Caso já exista, será incrementado seu valor de incidencia e adicionado o ID ao Array
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
    }

    /**
     * Método que cria um arquivo txt para leitura.
     * 
     * <p>O criarArquivoLista() faz a leitura e ordenação do Map atual, e escreve todos os valores lidos dentro de um arquivo TXT
     * de forma organizada e ordenada.
     */
    void criarArquivoLista(){
        //Verificando se o Map de Lista foi criado
        if (this.contagemNomes.size() == 0){ //Verificando se o arquivo existe
            criarListaNome();
        }
        try {
            FileWriter Writer = new FileWriter("src/data/ListaInvertida.txt"); //Abrindo escritor de arquivo
            List<Map.Entry<String, DadosNome>> mapaOrdenado = ordenaMap(contagemNomes); //Ordenando Map para a leitura
            
            //Realizando a iteração pelo Map para pegar todos seus valores
            for(Map.Entry<String, DadosNome> valor : mapaOrdenado) {
                String nome = valor.getKey(); //Recebendo nome
                int incidencia = valor.getValue().incidencia; //Recebendo incidencia
                List<Integer> IDs = valor.getValue().localID; //Recebendo o array de IDs
                
                //Escrevendo os valores no documento
                Writer.write("Nome: " + nome);
                Writer.write(" -> Incidencia: " + incidencia);
                Writer.write(" | IDs:{");
                for(int id : IDs){
                    Writer.write(id + ",");
                }
                Writer.write("}\n"); //Finalizando Nome atual e quebrando linha
            }
            System.out.println("Arquivo criado com sucesso.");
            Writer.close();
        } 
        catch (Exception e) {
            System.out.println("Houve um erro ao tentar criar o arquivo:");
            e.printStackTrace();
        }
    }

    /**
     * Método para imprimir os valores da Lista Invertida de forma ordenada
     * pela incidencia dos nomes
     */
    void imprimirListaInvertida() {
        //Verificando se o Map foi carregado com
        if (this.contagemNomes.size() == 0){ //Verificando se o arquivo existe
            criarListaNome();
        }
        //Chamando a ordenação do Map
        List<Map.Entry<String, DadosNome>> mapaOrdenado = ordenaMap(contagemNomes);

        //Impressão das String ordenadas
        for (Map.Entry<String, DadosNome> valor : mapaOrdenado) {
            System.out.println("Nome: " + valor.getKey() + ", Incidencia: " + valor.getValue().incidencia + ", ID(s) do Nome: " + valor.getValue().localID);
        }
    }

    /**
     * Algoritmo para realizar a ordenação em memória primária de um HashMap
     * 
     * <p>Esse algoritmo ordena, em memória primária, um Map de Lista Invertida Retirando todo seu conteúdo e jogando
     * para um ArrayList, onde é usado a função sort junto de um Comparator para realizar a ordenação.
     * <p>O valor comparado dentro do Map é o int incidencia, que conta quantas vezes X nome apareceu.
     * 
     * @param desordenado - Map pré ordenação
     * @return <b>ordenado</b> - Map após ordenação
     * @see DadosNome
     */
    List<Map.Entry<String, DadosNome>> ordenaMap(Map<String, DadosNome> desordenado) {

        /*
         * Sobre a ordenação:
         * Todos os valores do HashMap são copiados e inseridos dentro de uma ArrayList pois dentro do mesmo existe
         * o comando sort(). No sort é utilizado um Comparator para comparar o valor atual do Map com o ArrayList
         */
        List<Map.Entry<String, DadosNome>> ordenado = new ArrayList<>(this.contagemNomes.entrySet());
        ordenado.sort(Map.Entry.comparingByValue(Comparator.comparingInt(x -> x.incidencia)));

        return ordenado;
    }

    
    /**
     * Algoritmo para realizar a leitura de todos os nós com X nome
     * 
     * <p>Esse algoritmo recebe um nome X determinado pelo usuario e realizar uma busca na Lista Invertida para verificar
     * se possui resultados. Caso tenha ele pegará todos os IDs que possuem o nome X e mostrará, no console, o nome completo
     * do piloto junto de seu ID
     * 
     * @param nome a ser pesquisado
     * @param Index com a lista de Indice
     * @param crud com a função de leitura
     */
    void pesquisaIndice(String nome, chaveIndice Index, CRUD crud) {
        if (this.contagemNomes.size() == 0){ //Verificando se o arquivo existe
            criarListaNome();
        }
        if(!this.contagemNomes.containsKey(nome)){ //Procurando pelo nome recebido
            System.out.println("Nome não encontrado nos valores...");
        }
        else { //Imprimindo caso o nome exista
            DadosNome busca = this.contagemNomes.get(nome); //Recuperando os dados do nome da Lista

            for(int id : busca.localID){ //Iterando por todos os IDs do nome
                long buscaPreview = Index.buscarIndex(id); //Buscando o ID digitado no Indice
                if(crud.readWithIndex(id, buscaPreview)) { //Verificando se existe o ID (que no caso DEVE existir mas nunca se sabe)
                    crud.pilotos.printPreview();
                }
            }
            
        }
    }
}
    