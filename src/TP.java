import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Arquivo principal do Trabalho Prático de AED3 -
 * A respeito: Utilizar alguma base de dados existente para realizar inserção no código, simulando os inúmeros registros de uma base de dados,
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
        int opcao;

        do {
            System.out.println("\n\n ------- MENU -------\n" + 
                "[01] - Create a File DB.\n" +
                "[02] - CRUD - Create.\n" +
                "[03] - CRUD - Read.\n" +
                "[04] - CRUD - Update.\n" +
                "[05] - CRUD - Delete\n" +
                "[06] - Export DB as CSV\n" +
                "[07] - Criar Index\n" + 
                "[08] - Exibir Index\n" +
                "[00] - Sair\n" 
            );
            System.out.print("Digite uma opção: ");
            opcao = scan.nextInt();
            switch (opcao) {
                case 1:
                    crud.createFirstFile();
                    System.out.println("\nArquivo DB criado.");
                    break;

                case 2: //Create
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

                case 3: //Read
                    System.out.println("Digite um ID o qual deseja ler: ");
                    int id = scan.nextInt();

                    //Le o ID registrado
                    if(crud.read(id)) {
                        crud.pilotos.printRegistro();
                    }
                    else {
                        System.out.println("\nNao foi possivel encontrar o piloto buscado.");
                    }
                    break;

                case 4: //Update
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
                        
                        if(crud.update(id2)) { //Checando se é possível atualizar
                            System.out.println("\nRegistro atualizado.");

                            Index.createIndex();
                            System.out.println("Index atualizado com sucesso!\n");
                        }
                        else{
                            System.out.println("\nNao foi possivel encontrar o ID informado.");
                        }
                    }
                    catch(Exception e){    
                        System.out.println("\nErro na formatacao da data:");
                        e.printStackTrace();
                    }
                    break;

                case 5: //Delete
                    System.out.println("\nDigite um ID para invalidar o seu respectivo registro: ");
                    int id3 = scan.nextInt();

                    if(crud.delete(id3)) { //Deleta o registro indicado pelo ID informado
                        System.out.println("\nRegistro deletado com sucesso.");

                        Index.createIndex();
                        System.out.println("Index atualizado com sucesso!\n");
                    }
                    else {
                        System.out.println("\nNao foi possivel encontrar o registro.");
                    }
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
     * - Método abre ou cria o documento newDrivers.csv para fazer sua escrita. É escrito a primeira linha de metadados
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