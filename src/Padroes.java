import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Padroes {
    
    /**
     * Método para realizar Casamento de Padrões 
     * <p>Método Força Bruta
     * @param padrao - contendo o padrão a ser buscado
     */
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
                long inicio = System.currentTimeMillis();

                while ((linha = leitor.readLine()) != null) { //Lendo todo o arquivo CSV para realizar toda a analise possível
                    Integer tl = linha.length(); 
                    
                    for(int l1 = 0; l1 <= tl-pl; l1++){ //Loop para percorrer o texto (linha no caso)
                        int l2; //Será utilizado para comparar depois
                        
                        for(l2 = 0; l2 < pl; l2++){ //Loop para percorrer o padrão
                            comparacoes++; 
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
                    IDloop++;
                }
                long fim = System.currentTimeMillis();
                System.out.println("Foram realizadas " + comparacoes + " comparações.");
                System.out.println("A comparação por todo o arquivo levou " + (fim-inicio) + " milissegundos.");
                leitor.close();
            }
            catch (IOException e){
                System.out.println("Houve um erro ao tentar abrir o arquivo newDrivers:");
                e.printStackTrace();
            }
    }

    /**
     * Método para realizar Casamento de Padrões 
     * <p>Método KMP
     * @param padrao - contendo o padrão a ser buscado
     */
    void KMP(String padrao){
        /*
         * Realizando a leitura do arquivo CSV para poder fazer o casamento de padrão
         * como designado pelas aulas
         */

         //Abertura de arquivos para leitura
            try{
                FileReader csv = new FileReader("src/data/newDrivers.csv"); //Abrindo arquivo CSV para recuperar dados
                BufferedReader leitor = new BufferedReader(csv); 
                String linha;
                int comparacoes = 0, IDloop = 0;
                long inicio = System.currentTimeMillis();

                int[] vetorF = calcularVetorDeFalhas(padrao); //Vetor dde falhas
                int pl = padrao.length(); //Tamanho do padrão
                
                linha = leitor.readLine();
                do {
                    int j = 0; //Indice para o padrão
                    int i = 0; //Indice para o texto (linha nesse caso)
                    int tl = linha.length();
                    while((tl - i) >= (pl - j)){
                        if(padrao.charAt(j) == linha.charAt(i)){ //Comparando letras
                            i++;
                            j++;
                        }
                        
                        comparacoes++;
                        
                        if(j==pl){ //Confenrindo se foram lidas a quantidade de letras suficiente do padrao para ter achado o resultado
                            System.out.println("Padrão encontrado no ID: " + IDloop);
                            j = vetorF[j-1];
                        }

                        else if(i < tl && padrao.charAt(j) != linha.charAt(i)){ //Caso aconteca uma comparação negativa após realizar X comparações positivas
                            if (j!=0)
                                j = vetorF[j-1];
                            else
                                i = i+1;
                        }
                        
                    }
                    linha = leitor.readLine();
                    IDloop++;
                    
                }while (linha != null);
                long fim = System.currentTimeMillis();
                System.out.println("Foram realizadas " + comparacoes + " comparações.");
                System.out.println("A comparação por todo o arquivo levou " + (fim-inicio) + " milissegundos.");
                leitor.close();
            }
            catch (IOException e){
                System.out.println("Houve um erro ao tentar abrir o arquivo newDrivers:");
                e.printStackTrace();
            }
    }

    /**
     * Método para calcular o Vetor de Falhas utilizado por KMP
     * <p>Calcula para quais as letras o KMP pode voltar caso haja uma falha nas comparações
     * @param padrao - contendo o padrão
     * @return vetorF com as possibilidades de retorno
     */
    private int[] calcularVetorDeFalhas(String padrao){
        int pl = padrao.length();
        int[] vetorF = new int[pl];
        int lenght = 0; //comprimento do prefixo/sufixo
        int loop = 1;
        
        while (loop < pl) {
            if (padrao.charAt(loop) == padrao.charAt(lenght)) { //Verificando se as letras são iguais
                lenght++;
                vetorF[loop] = lenght; //Armarzenando a posição do vetor de falhas
                loop++;
            }
            else{
                if(lenght!=0){
                    lenght = vetorF[lenght-1]; //Caso tivermos um sufixo que também é prefixo diminuiremos o vetor
                }
                else{
                    vetorF[loop] = 0; //Indicação que não foi encontrado nenhum prefixo que seja sufixo ao mesmo tempo
                    loop++;
                }
            }
        }

        return vetorF;
    }
}
