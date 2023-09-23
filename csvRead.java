import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class csvRead {
    public static void csvReader () throws FileNotFoundException{
        Scanner scan = new Scanner(new File("data\\drivers.csv")); //Criando scanner pré carregado com o arquivo de dados
        scan.useDelimiter(","); //Definição do separador de texto para leitura de arquivos
        while (scan.hasNext()) {
            
            System.out.println(scan.next()); //teste de leitura
        } 
    }
}
