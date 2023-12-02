import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Arquivo principal do Trabalho Prático de AED3
 * <p>A respeito: Utilizar alguma base de dados existente para realizar inserção no código, simulando os inúmeros registros de uma base de dados,
 * utilizando as técnicas de CRUD, armazenamento, ordenação e listagem apresentados nas aulas de AEDs3.
 * 
 * @author Pedro Mafra
 * @author Alessandro
 * @see driverNode
 * @see chaveIndice
 * @see CRUD
 */

public class TP {
    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);
        CRUD crud = new CRUD();
        chaveIndice Index = new chaveIndice();
        listaInvertida LInvertida = new listaInvertida();
        CompressaoHuffman huffman = new CompressaoHuffman();
        Padroes padrao = new Padroes();
        int opcao;

        do {
            System.out.println("\n ------- MENU -------\n" + 
                "[01] - CRUD - Create.\n" +
                "[02] - CRUD - Read.\n" +
                "[03] - CRUD - Update.\n" +
                "[04] - CRUD - Delete\n" +
                "[05] - DB - Create a DBFile.\n" +
                "[06] - DB - Export DB as CSV\n" +
                "[07] - INDEX - Criar Index\n" + 
                "[08] - INDEX - Exibir Index\n" +
                "[09] - LISTA - Criar Lista Invertida\n" +
                "[10] - LISTA - Exibir Lista Invertida\n" +
                "[11] - LISTA - Criar arquivo da Lista Invertida\n" +
                "[12] - LISTA - Realizar busca na Lista Invertida\n" +
                "[13] - Huffman - Comprimir arquivo\n" +
                "[14] - Casamento de Padrões - Realizar busca\n" +
                "[00] - Sair\n" 
            );
            System.out.print("Digite uma opção: ");
            opcao = scan.nextInt();
            switch (opcao) {

                case 1: //Create
                    try {
                        //Coletando todas as informações para criação do novo piloto
                        System.out.println("Digite o nome do piloto:");
                        if(scan.hasNext())
                            scan.nextLine(); //Limpando buffer
                        String name = scan.nextLine();

                        System.out.println("Digite o sobrenome do píloto:");
                        String surname = scan.nextLine();

                        System.out.println("Digite a referencia do piloto:");
                        String reference = scan.nextLine();

                        System.out.println("Digite a nacionalidade do piloto: ");
                        String nationality = scan.nextLine();

                        System.out.println("Digite o codigo do piloto:");
                        String code = scan.nextLine();

                        System.out.println("Digite o numero do piloto:");
                        String driverNum = scan.nextLine();

                        System.out.println("Digite a data de nascimento do piloto (yyyy-mm-dd):");
                        String date = scan.nextLine();
                        
                        LocalDate dataAux = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
                        //Formatando a data para o padrao usado.
                        crud.pilotos.registrar(reference, name, surname, nationality, driverNum, dataAux, code); //Repassando atributos e criando
                        crud.create();

                        Index.createIndex();
                        System.out.println("Index atualizado com sucesso!\n");
                    } 
                    catch (Exception e) {
                        System.out.println("\nProblema com a formatação da data:");
                        e.printStackTrace();
                    }
                    break;

                case 2: //Read
                    System.out.println("Digite um ID o qual deseja ler: ");
                    int id = scan.nextInt();
                    long busca = Index.buscarIndex(id); //Buscando o ID digitado no Indice

                    if(crud.readWithIndex(id, busca)) {
                        crud.pilotos.printRegistro();
                    }
                    else {
                        System.out.println("\nNao foi possivel encontrar o piloto buscado.");
                    }
                    break;

                case 3: //Update
                    try{
                        System.out.println("Informe o ID do registro que deseja atualizar: ");
                        int id2 = scan.nextInt();

                        //Solicitando os novos dados dos pilotos
                        System.out.println("\nDigite o nome do piloto:");
                        if(scan.hasNext())
                            scan.nextLine(); //Limpando buffer
                        String name = scan.nextLine();
                        System.out.println("Digite o sobrenome do píloto:");
                        String surname = scan.nextLine();
                        System.out.println("Digite a referencia do piloto:");
                        String reference = scan.nextLine();
                        System.out.println("Digite a nacionalidade do piloto: ");
                        String nationality = scan.nextLine();
                        System.out.println("Digite o codigo do piloto:");
                        String code = scan.nextLine();
                        System.out.println("Digite o numero do piloto:");
                        String driverNum = scan.nextLine();
                        System.out.println("Digite a data de nascimento do piloto (yyyy-mm-dd):");
                        String date = scan.nextLine();

                        LocalDate dateAux = LocalDate.parse(date, DateTimeFormatter.ISO_DATE); //Convertendo a String recebida para LocalDate

                        crud.pilotos.setID(id2);
                        crud.pilotos.registrar(reference, name, surname, nationality, driverNum, dateAux, code); //Repassando atributos e criando
                        long busca2 = Index.buscarIndex(id2);

                        if(crud.updateWithIndex(id2, busca2)) { //Checando se é possível atualizar
                            System.out.println("\nRegistro atualizado.");

                            Index.createIndex();
                            System.out.println("Index atualizado com sucesso!\n");
                        }
                        else{
                            System.out.println("\nNao foi possivel encontrar o ID informado.");
                        }
                    }
                    catch(DateTimeParseException e){    
                        System.out.println("\nErro na formatacao da data:");
                        e.printStackTrace();
                    }
                    break;

                case 4: //Delete
                    System.out.println("\nDigite um ID para invalidar o seu respectivo registro: ");
                    int id3 = scan.nextInt();
                    long busca3 = Index.buscarIndex(id3);

                    if(crud.deleteWithIndex(id3, busca3)) { //Deleta o registro indicado pelo ID informado
                        System.out.println("\nRegistro deletado com sucesso.");

                        Index.createIndex();
                        System.out.println("Index atualizado com sucesso!\n");
                    }
                    else {
                        System.out.println("\nNao foi possivel encontrar o registro.");
                    }
                    break;

                case 5:
                    crud.createFirstFile();
                    System.out.println("\nArquivo DB criado.");
                    break;

                case 6: //CSV Export
                    convertDbToCsv(crud);
                    break;
                    
                case 7:
                    Index.createIndex();
                    System.out.println("Índice criado com sucesso!");
                    break;

                case 8:
                    Index.exibirIndex();
                    break;

                case 9:
                    LInvertida.criarListaNome();
                    System.out.println("Lista criada com sucesso!\n");
                    break;
                
                case 10:
                    LInvertida.imprimirListaInvertida();
                    break;

                case 11:
                    LInvertida.criarArquivoLista();
                    break;

                case 12:
                    System.out.println("Digite o nome do piloto que deseja buscar:");
                    if(scan.hasNext()){
                        scan.nextLine(); //Lendo caso exista algo no buffer
                    }
                    String nome = scan.nextLine();
                    LInvertida.pesquisaIndice(nome, Index, crud);
                    break;

                case 13:
                    huffman.comprimir();
                    break;

                case 14:
                    System.out.println("Qual tipo de algoritmo deseja usar?");
                    System.out.println("[1] - Força bruta\n[2] - KMP");

                case 0:
                    System.out.println("\nDesligando...");
                    scan.close();
                    break;

                default:
                    System.out.println("\nOpção invalida.\n");
                    break;
            }

        } while (opcao != 0);
    }

    /**
     * Método para exportar o arquivo database para uma nova lista csv.
     * 
     * <p>Esse método abre ou cria o documento newDrivers.csv para fazer sua escrita. É escrito a primeira linha de metadados
     * que se mantem a mesma independente da situação e em seguida é recuperado do DriversDB.db os pilotos com a 
     * modificação do CRUD
     * @param crud
     * @throws IOException
     * @see CRUD
     */
    public static void convertDbToCsv(CRUD crud) throws IOException {
        RandomAccessFile file; //Declaracao de parametro file para abrir a database
        String csvPath = "src/data/newDrivers.csv"; //Especificando caminho do novo csv

        try {
            //Abrindo Writer com o CSV
            FileWriter fileWriter = new FileWriter(csvPath);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            //Escrevendo primeira header
            String header = "driverId,driverRef,number,code,forename,surname,dob,nationality";
            bufferedWriter.write(header); //Escrevendo a header
            bufferedWriter.newLine(); //Faz a quebra de linha
            
            file = new RandomAccessFile("src/data/driversDB.db", "rw"); //Abrindo o database no modo escrita e leitura
            file.seek(4); //Pulando o cabeçalho de metadados

            while(true) { //Enquanto houver conteudo, haverá uma repetição
                try{
                    if(file.readChar() != '*'){ //Se o registro não estiver deletado

                        //Realizando a pré-carga da classe pilotos
                        byte[] ba = new byte[file.readInt()]; //Criando vetor de bytes do tamanho do registro
                        file.readFully(ba); //Lendo o registro em bytes
                        crud.pilotos.fromByteArray(ba); //Lendo o registro do vetor de btyes de modo organizado

                        //Pegando valores não String
                        int idPiloto = crud.pilotos.getID();
                        LocalDate datePiloto = crud.pilotos.getDate();

                        //Criando vetor de String com diversos valores do piloto
                        String[] linha = {Integer.toString(idPiloto), crud.pilotos.getReference(), crud.pilotos.getDriverNumber(), crud.pilotos.getCode(), crud.pilotos.getName(), crud.pilotos.getSurname(), datePiloto.toString(), crud.pilotos.getNatiotanlity()};
                        String linhaUnida = String.join(",", linha); //Unindo o vetor de String e separando com virgula
                        bufferedWriter.write(linhaUnida); //Escrevendo linha no CSV
                        bufferedWriter.newLine(); //Criando nova linha
                    }
                    else{
                        file.skipBytes(file.readInt());
                    }
                }
                catch(EOFException e){
                    break;
                }
            }
            bufferedWriter.close();
        } 
        catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Database convertida para CSV");
    }
}