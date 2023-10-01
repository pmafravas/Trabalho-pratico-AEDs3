package src;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/*
 * Arquivo principal do TP1 AED3
 * Autor: Pedro Mafra Vasconcelos & Alessandro
 * 
 * A respeito: Utilizar alguma base de dados existente para realizar inserção no código, simulando os inúmeros registros de uma base de dados,
 * utilizando as técnicas de CRUD, armazenamento, ordenação e listagem apresentados nas aulas de AEDs3.
 */

public class TP {
    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);
        CRUD crud = new CRUD();
        int opcao;

        SimpleDateFormat formatarData = new SimpleDateFormat("dd/MM/yyyy"); //Formatação da leitura de anos
        Date dataAux;


        do {
            System.out.println("\n\n ------- MENU -------\n" + 
                "[01] - Create a File DB.\n" +
                "[02] - CRUD - Create.\n" +
                "[03] - CRUD - Read.\n" +
                "[04] - CRUD - Update.\n" +
                "[05] - CRUD - Delete]\n" +
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

                        System.out.println("Digite a data de nascimento do piloto (dd/mm/yyyy):");
                        String date = scan.nextLine();
                        
                        dataAux = formatarData.parse(date);
                        //Formatando a data para o padrao usado.
                        crud.pilotos.registrar(reference, name, surname, nationality, driverNum, dataAux, code); //Repassando atributos e criando
                        crud.create();
                    } 
                    catch (ParseException e) {
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
                        System.out.println("Digite a data de nascimento do piloto (dd/mm/yyyy):");
                        String date = scan.nextLine();
                        dataAux = formatarData.parse(date);

                        crud.pilotos.setID(id2);
                        crud.pilotos.registrar(reference, name, surname, nationality, driverNum, dataAux, code); //Repassando atributos e criando
                        
                        if(crud.update(id2)) { //Checando se é possível atualizar
                            System.out.println("\nRegistro atualizado.");
                        }
                        else{
                            System.out.println("\nNao foi possivel encontrar o ID informado.");
                        }
                    }
                    catch(ParseException e){    
                        System.out.println("\nErro na formatacao da data:");
                        e.printStackTrace();
                    }
                    break;

                case 5: //Delete
                    System.out.println("\nDigite um ID para invalidar o seu respectivo registro: ");
                    int id3 = scan.nextInt();

                    if(crud.delete(id3)) { //Deleta o registro indicado pelo ID informado
                        System.out.println("\nRegistro deletado com sucesso.");
                    }
                    else {
                        System.out.println("\nNao foi possivel encontrar o registro.");
                    }
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

    /*
     * Método para exportar o arquivo database para uma nova lista csv
     * Method to export the database file to a new csv list
     */
    public static void writeToCSV(RandomAccessFile database) throws Exception {
        SimpleDateFormat formatarData = new SimpleDateFormat("dd/MM/yyyy"); //Formatação da leitura de anos
        Date dataAux;
        long startOfEntry;
        BufferedWriter bw = new BufferedWriter(new FileWriter("src\\data\\drivers.csv"));
        database.seek(4); //Pulando metadados
        while (database.getFilePointer() < database.length()) {
            startOfEntry = database.getFilePointer();
            if (database.readChar() != '*') {
                database.skipBytes(4); //Pulando indicador de tamanho
                driverNode searchPiloto = new driverNode();
                searchPiloto.registrar(database.readUTF(), database.readUTF(), database.readUTF(),database.readUTF(),database.readUTF(), dataAux = formatarData.parse(database.readUTF()),database.readUTF());
                bw.write(searchPiloto.toCSVLine());
            } else {
                database.seek(startOfEntry + database.readInt());
            }
        }
        bw.close();
    }
}