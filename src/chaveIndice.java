import java.io.RandomAccessFile;
import java.util.ArrayList;

public class chaveIndice {
    driverNode pilotos = new driverNode(); //Classe pilotos para utilização dentro do código
    String pathOrigem = "src/data/driversDB.db"; //String contendo caminho da base de dados para realizar leitura    
    String pathIndex = "src/data/index.db"; //String contendo caminho da base de dados para realizar leitura
    ArrayList<Integer> idsRemovidos = new ArrayList<>(); //ArrayList que será usado para contabilizar todos os IDs removidos durante a criação do índice
        
    /**
     * Método que realiza a criação de um índice.
     * <p> O método abre o documento driversDB para fazer uma leitura e assim escrever em um novo documento,
     * o ID do piloto e seu respectivo byte de localização.
     */
    void createIndex(){
        
        //Leitores de arquivos que serão utilizados com os arquivos
        RandomAccessFile arquivoOrigem;
        RandomAccessFile arquivoIndex;
        
    
        try {
            arquivoOrigem = new RandomAccessFile(pathOrigem, "rw"); // Abetura do arquivo            
            arquivoIndex = new RandomAccessFile(pathIndex, "rw"); // Abertura/Criação do arquivo de índices
            int quantidadeIDs = 0; //Numero de IDs dentro do arquivo
            int IDloop = 1; //Quantidade de IDs lida

            quantidadeIDs = arquivoOrigem.readInt(); //Lendo quantidade de IDs, provisionado pelo metadados

            /*
             * ORDEM DE LEITURA DO ARQUIVO DB:
             * -ID
             * -REFERENCIA
             * -NOME
             * -SOBRENOME
             * -NACIONALIDADE
             * -N DE PILOTO
             * -DATA DE NASCIMENTO
             * -CODIGO (abreviação do nome)
             */

            while (IDloop <= quantidadeIDs) {
                try {
                    long posicaoByte;
                    
                    if(arquivoOrigem.readChar() != '*'){ //Checando lápide

                        /*
                        * Primeiro é feito a leitura dos dados e são carregados e tratados dentro da classe piloto
                        */

                        posicaoByte = arquivoOrigem.getFilePointer(); //Pegando a posição do ponteiro / qual byte se encontra o registro
                        byte[] ba = new byte[arquivoOrigem.readInt()]; //Lê tamanho do registro e cria um novo vetor com o mesmo tamanho
                        arquivoOrigem.readFully(ba); //Lê todo o registro e escreve dentro do byte array
                        pilotos.fromByteArray(ba); //Transcrevendo o byte array para classe piloto


                        /*
                        * Em seguida será escrito o ID do piloto e seu byte onde inicia o registro
                        */

                        arquivoIndex.writeInt(pilotos.ID); //Escrevendo ID do piloto que também servirá como ID do arquivo
                        arquivoIndex.writeLong(posicaoByte); //Escrevendo posição dos bytes do respectivo ID - bem onde se encontra o Int indicador de tamanho

                        IDloop++; //Aumetando a quantidade de IDs lidos
                    }
                    else{
                        arquivoOrigem.skipBytes(arquivoOrigem.readInt()); //Se estiver deletado, pula X bytes para mudar de registro, como indicado no inicio do registro.
                        IDloop++; //Indicando iqual ID foi removido
                        if(!idsRemovidos.contains(IDloop)){ //Verificando se o ArrayList já possui o ID deletado para haver duplicados
                            idsRemovidos.add(IDloop); //Adicionando o ID removido a lista para tratamentos em outros métodos
                        }
                    }
                }
                catch (Exception e){
                    System.out.println("Não foi possível realizar a leitura dos arquivos");
                }
            }           
        } 
        catch (Exception e) {
            System.out.println("Não foi possível abrir os arquivos:");
            e.printStackTrace();
        }
        
    }

    /**
     * Método para imprimir um índice existente no console.
     * <p>É aberto o documento de índice criado pelo createIndex() e exibe de forma formatada, suas informações.
     */
    void exibirIndex(){
        RandomAccessFile index;

        try {
            index = new RandomAccessFile(pathIndex, "rw"); //Abrindo arquivo
            while (true) {
                //Variáveis para leitura
                int IDleitura;
                long byteLeitura;
                
                //Leitura de dados
                IDleitura = index.readInt();
                byteLeitura = index.readLong();

                //Exibição de dados
                System.out.println("ID: " + IDleitura + " - byte: " + byteLeitura);

            }
        } catch (Exception e) {
            System.out.println(" --FIM DE LEITURA-- ");
        }
    }


    /**
     * Método para buscar o byte de um ID
     * 
     * <p>Esse método procura o respectivo byte de um ID. Dentro de seu arquivo <i>index.db</i> estão todos os IDs e Bytes de pilotos de tal modo que é possível
     * calcular quantos bytes serão nescessários pular para chegar ao ID desejado.
     * <p>É levado em consideração também os IDs deletados, onde será checado se haverá interferência dos mesmo e decidirá qual será o método correto a se 
     * utilizar
     * @param ID que deseja buscar
     * @return <b>endereco</b> com o byte de localização
     */
    long buscarIndex(int ID){
        RandomAccessFile index;
        long endereco = 0;
        int IDleitura;

        try {
            int qtdRemovidos = 0; //Variavel para contar quantos IDs removidos são maior ou iguais
            boolean isBigger = false; //Variável responsável por indicar se o ID removido foi maior ou não, para realizar diferente tratamentos
            index = new RandomAccessFile(pathIndex, "rw"); //Abertura do arquivo
            createIndex(); //Atualiza o index para verificação de IDs removidos

            for (int i = 0; i < idsRemovidos.size(); i++) { //Procurando por IDs removidos que tenham sejam menor que o ID de busca
                if(ID >= idsRemovidos.get(i)){ //Iterando pelo array de IDs removidos
                    isBigger = true;
                    qtdRemovidos++;
                }
            }

            if(isBigger){
                for(int IDloop = 1; IDloop < ID-qtdRemovidos; IDloop++){ //A quantidade de IDs removidos será contabilizada para a pesquisa cair na linha certa
                    index.skipBytes(12); //Número de bytes ocupando por um int e um long
                }
            }
            else{
                for(int IDloop = 1; IDloop < ID; IDloop++){ //Iterando pelos IDs
                    index.skipBytes(12); //Número de bytes ocupando por um int e um long
                }
            }
            
            //Leitura de dados
            IDleitura = index.readInt();
            endereco = index.readLong();

            if(IDleitura == ID){ //Verificando se o ID lido foi o correto
                System.out.println("Busca do ID feita com sucesso!");
            }
            else{ //Caso não seja, o endereço será redefinido automaticamente para 0
                System.out.println("Não foi possível encontrar o ID selecionado, ele pode ter sido removido...");
                endereco = 0;
            }
        } 
        catch (Exception e) {
            System.out.println("Não foi possível ler o arquinvo index.db:");
            e.printStackTrace();
        }
        
        return endereco;      
    }
}