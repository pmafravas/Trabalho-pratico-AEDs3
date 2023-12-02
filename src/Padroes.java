import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Padroes {
    void forcaBruta(String padrao){
        /*
         * Realizando a leitura do arquivo CSV para poder fazer o casamento de padrão
         * como designado pelas aulas
         */

         //Abertura de arquivos para leitura
            try{
                FileReader csv = new FileReader("src/data/newDrivers.csv"); //Abrindo arquivo CSV para recuperar dados
                BufferedReader leitor = new BufferedReader(csv); 
                String linha;
                Integer comparacoes = 0, IDloop = 1;
                Integer pl = padrao.length(); //Tamanho do padrão

                while ((linha = leitor.readLine()) != null) { //Lendo todo o arquivo CSV para realizar toda a analise possível
                    Integer tl = linha.length(); 
                    
                    for(int l1 = 0; l1 <= tl-pl; l1++){ //Loop para percorrer o texto (linha no caso)
                        int l2; //Será utilizado para comparar depois
                        for(l2 = 0; l2 <= pl; l2++){ //Loop para percorrer o padrão
                            comparacoes++; IDloop++;
                            if(linha.charAt(l1+l2) != padrao.charAt(l2)){
                                break; //Caso as letras não batam, o loop secundário será interrompido
                            }
                        }
                        //Caso o valor de loop2 seja o mesmo do padrão, significa que os loops de comparações foram completados
                        //mostrando que houve uma comparação igual
                        if(l2 == padrao.length()){ 
                            System.out.println("Padrão encontrado no ID " + IDloop);
                        }
                    }
                }

            }
            catch (IOException e){
                System.out.println("Houve um erro ao tentar abrir o arquivo newDrivers:");
                e.printStackTrace();
            }
    }

    void KMP(String padrão){
        
    }
}
