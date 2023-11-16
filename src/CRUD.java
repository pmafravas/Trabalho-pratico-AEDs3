import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CRUD {
    public driverNode pilotos = new driverNode(); //Declaração de um objeto driverNode para que possa haver a atribuição de novos valores
    String path = "src/data/driversDB.db"; //String contendo todo o caminho para o arquivo database
    
    //Leitores de arquivos que serão utilizados com os arquivos
    RandomAccessFile file;
    FileReader csv;

    /*
     * Procedimento de Criação de novo registro
     * Parametros: null
     */
    public void createFirstFile() throws IOException{
        try {
            //Abertura de arquivos para leitura
            csv = new FileReader("src/data/drivers.csv"); //Abrindo arquivo CSV para recuperar dados
            BufferedReader leitor = new BufferedReader(csv);

            file = new RandomAccessFile(path, "rw"); //Abrindo-Criando arquivo database para escrita
            file.setLength(0); //Zerando o marcador

            leitor.readLine(); //Pulando a primeira linha do csv que contem metadados
            file.writeInt(0); //Espaçor eservado para um int metadado de cabeçalho

            String linha = leitor.readLine(); //Carregando a linha com conteudo
            String[] linhaSeparada;

            while (linha != null) {
                linhaSeparada = linha.split(","); //Separando todos conteúdos pelo divisor comum ';'
                
                /*
                 * Splits da linha lida:
                 * 
                 * linhaSeparada[0] -> ID
                 * linhaSeparada[1] -> Reference
                 * linhaSeparada[2] -> Number
                 * linhaSeparada[3] -> Code
                 * linhaSeparada[4] -> Name
                 * linhaSeparada[5] -> Surname
                 * linhaSeparada[6] -> Date
                 * linhaSeparada[7] -> Nationality
                 */

                LocalDate formatDate = LocalDate.parse(linhaSeparada[6], DateTimeFormatter.ISO_DATE);

                //Registrando os valores lidos dentro da classe
                pilotos.registrar(linhaSeparada[1], linhaSeparada[4], linhaSeparada[5], linhaSeparada[7],linhaSeparada[2], formatDate, linhaSeparada[3]);

                file.seek(0); //Posicionando ponteiro para inicio do cabeçalho, onde possui o metadados para IDs utilizados, copiando o ID do próprio CSV
                file.writeInt(Integer.parseInt(linhaSeparada[0])); //Escrevendo o ID utilizado do drivers.csv

                file.seek(file.length()); //Voltando ao final do arquivo
                pilotos.setID(Integer.parseInt(linhaSeparada[0]));
                byte[] ba = pilotos.toByteArray();
                file.writeChar(' '); //Marcando a lápide como vazio
                file.writeInt(ba.length);
                file.write(ba);

                linha = leitor.readLine(); //Realiza a leitura da proxima linha
            }

            //Fechando itens abertos para leituras
            csv.close();
            file.close();
            leitor.close();
        }
        catch (IOException e){
            System.out.println("\nErro ao criar arquivo.");
            e.printStackTrace();
        }
    }

    /*
     * Procedimento de criação de novos registros por meio do usuario
     * Parametros: null
     */
    public void create(){
        try {
            file = new RandomAccessFile(path, "rw"); //Abrindo arquivo database para escrita
            
            //Lendo o ID e definindo o ID do novo registro
            file.seek(0);
            int lastID = file.readInt(); //Pegando o ultimo ID usado
            pilotos.setID(lastID + 1); //Novo ID usado
            file.seek(0); //Voltando ao inicio
            file.writeInt(pilotos.ID); //Escrevendo novo ID utilizado

            file.seek(file.length());
            byte[] ba = pilotos.toByteArray(); //Transcrição do objeto para bytes
            file.writeChar(' '); //Definindo lápide vazia
            file.writeInt(ba.length); //Escrevendo tamanho 
            file.write(ba); //Escrevendo o vetor de bytes no database

            file.close();
            System.out.println("\nRegistro criado.");
        } 
        catch (Exception e) {
            System.out.println("\nHouve um erro ao criar o novo registro:");
            e.printStackTrace();
        }
    }

    /*
     * Procedimento para leitura de registros existentes, retornando um bool verdadeiro ou falso para o comando originatário, onde
     * será interpretado sua resposta.
     * Parametro: int ID -> Contem o ID do piloto desejado pelo usuario
     * Retorno: true/false
     */
    public boolean read (int id){
        try {
            file = new RandomAccessFile(path, "rw"); //Abrindo o database no modo escrita e leitura
            file.seek(4); //Pulando o cabeçalho de metadados

            //Buscando pelo arquivo database de modo sequencial
            while (true) {
                try {
                    if(file.readChar() != '*'){
                        byte[] ba = new byte[file.readInt()]; //Lê o tamanho do registro e cria um novo vetor de bytes com o mesmo tamanho
                        file.readFully(ba); //Lendo todo o registro de acordo com a quantidade de bytes
                        pilotos.fromByteArray(ba); //Extrai o objeto do vetor de btyes

                        if(id == pilotos.getID()) { //Conferindo se o ID bate
                            //Fechando o arquivo sem tirar o objeto, assim retornando ao Menu e o usuario
                            file.close();
                            return true;
                        }
                    }
                    else {
                        file.skipBytes(file.readInt()); //Pulando o registro deletado
                    }
                } catch (EOFException e) {
                    break;
                }
            }
            pilotos = new driverNode(); //Esvaziando o registro existente caso não haja correspondencia
            System.out.println("\nNão foi possivel encontrar o registro.");
            file.close();
        } 
        catch (Exception e) {
            System.out.println("\nNão foi possivel realizar a leitura:");
            e.printStackTrace();
        }
        return false;
    }

    /*
     * Procedimento para realizar um update de um registro já existente.
     * Parametros: int ID -> Contem o ID do registro em que se deseja fazer a bsuca
     * Retorno: true/false
     */
    public boolean update(int id) {
        try {
            file = new RandomAccessFile(path, "rw"); //Abre o arquivo criado no modo de escrita e de leitura
            file.seek(4); //Redireciona o ponteiro para o primeiro registro (pula o cabeçalho)

            //Cria um objeto da classe Registro para armazenar os atributos da nova entidade que substituíra a antiga
            driverNode registroNovo = new driverNode();
            registroNovo.registrar(pilotos);

            //Transforma o objeto em um vetor de bytes para escrita como registro
            byte[] registroNovoByte = registroNovo.toByteArray();

            //Busca pelo arquivo inteiro
            while(true) {
                try {
                    long pos = file.getFilePointer(); //Salva a posição atual do ponteiro, que sempre será a posição da lápide de algum dos registros
                    //Confere se o registro é válido
                    if (file.readChar() != '*') {
                        byte[] registroAntigoByte = new byte[file.readInt()]; //Lê o tamanho do registro do objeto antigo e cria um vetor de bytes com o dado tamanho
                        file.readFully(registroAntigoByte); //Lê todo o registro e escreve no vetor de bytes
                        pilotos.fromByteArray(registroAntigoByte); //Extrai o objeto antigo do vetor de bytes
                        //Compara os IDs dos objetos para conferir se o objeto extraído realmente é o certo a ser substituído
                        if (pilotos.getID() == registroNovo.getID()) {
                            //Confere se o registro novo é cabe dentro do espaço que havia sido alocado para o antigo registro
                            if (registroAntigoByte.length >= registroNovoByte.length) {
                                //Caso caiba, redireciona o ponteiro para o início do registro, pula a lápide e o tamanho do registro, e escreve o novo registro. Retorna verdadeiro.
                                file.seek(pos);
                                file.readChar();
                                file.readInt();
                                file.write(registroNovoByte);
                                return true;
                            } else {
                                //Caso contrário, redireciona o ponteiro para o início do registro, marca o registro como inválido, e escreve o novo registro no final do arquivo. Retorna verdadeiro
                                file.seek(pos);
                                file.writeChar('*');
                                pilotos = registroNovo;
                                file.seek(file.length());
                                byte[] ba = pilotos.toByteArray(); 
                                file.writeChar(' '); 
                                file.writeInt(ba.length); 
                                file.write(ba); 
                                return true;
                            }
                        }
                    }
                    //Caso contrário, pula o número de bytes equivalente ao tamanho do registro
                    else {
                        file.skipBytes(file.readInt());
                    }
                }
                catch(EOFException e) {
                    break;
                }
            }
            file.close();
            System.out.println("\nRegistro não encontrado.");
        }
        //Tratamento de exceções
        catch(IOException e) {
            System.out.println("\nErro ao atualizar o registro.");
            e.printStackTrace();
        }
        return false;
    }

    /*
     * Procedimento para deletar um registro.
     * Parametros: int ID -> Contem o ID do registro em que se deseja deletar
     * Retorno: true/false
     */
    public boolean delete(int id){
        try{
            file = new RandomAccessFile(path, "rw"); //Abrindo arquivo no modo leitura e escrita
            file.seek(4); //Pulando campo de metadados (ID)

            while(true){
                try{
                    long pos = file.getFilePointer(); //Salvando a posição atual do registro - posição da lápide para demarcar caso seja o ID buscado
                    if(file.readChar() != '*'){
                        byte[] ba = new byte[file.readInt()]; //Lendo tamanho do registro
                        file.readFully(ba); //Lê todo o registro com base no tamanho do registro
                        pilotos.fromByteArray(ba); //Extraindo o objeto do vetor

                        if(pilotos.getID() == id){ //Verificando se é o registro buscado
                            file.seek(pos); //Voltando a posição da lapide
                            file.writeChar('*'); //Marcando a lápide
                            file.close(); 
                            return true; //Voltando que a deleção foi feita
                        }
                    }
                    else{
                        file.skipBytes(file.readInt()); //Pulando o registro atual baseado na quantidade de bytes existente
                    }
                }
                catch(EOFException EOFe){
                    break;
                }
            }
            System.out.println("\nRegistro não encontrado.");
            file.close();
        }
        catch(IOException e){
            System.out.println("\nErro ao deletar:");
            e.printStackTrace();
        }
        return false;
    }
}
