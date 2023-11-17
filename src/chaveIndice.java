import java.io.FileReader;
import java.io.RandomAccessFile;

public class chaveIndice {
    driverNode pilotos = new driverNode(); //Classe pilotos para utilização dentro do código
    String pathOrigem = "src/data/driversDB.db"; //String contendo caminho da base de dados para realizar leitura    
    String pathIndex = "src/data/index.db"; //String contendo caminho da base de dados para realizar leitura

        
    void createIndex(){
        
        //Leitores de arquivos que serão utilizados com os arquivos
        RandomAccessFile arquivoOrigem;
        RandomAccessFile arquivoIndex;
        
    
        try {
            arquivoOrigem = new RandomAccessFile(pathOrigem, "rw"); // Abetura do arquivo            
            arquivoIndex = new RandomAccessFile(pathIndex, "rw"); // Abertura/Criação do arquivo de índices
            int quantidadeIDs = 0;
            int IDloop = 0;
           // arquivoOrigem.setLength(0); //Zerando ponteiro
           // arquivoIndex.setLength(0); //Zerando ponteiro 

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

            while (IDloop >= quantidadeIDs) {
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
            // TODO: handle exception
        }
    }
}