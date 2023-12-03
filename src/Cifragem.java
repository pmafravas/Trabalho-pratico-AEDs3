public class Cifragem {
    String chave;
    char[] chaveOrdenada;
    int[] posicoesOrdenas;

    //Construtor padrão para a cifragem contendo sua chave
    Cifragem(){
        chave = "AED"; //Nossa chave para cifragem
        chaveOrdenada = chave.toCharArray(); //Criando vetor de char da chave para ordenar depois
        posicoesOrdenas = new int[chave.length()]; //Criando vetor de posições do tamanho da chave
    }


    /**
     * Método para realizar ordenação da chave de cifragem.
     * <p>Método que realizará ordenação da chavem de cifragem e montará no vetor posiçõesOrdenadas a ordem
     * da chave para realizar a cifragem
     */
     void ordenarChave()
    {
        int min, i, j; 
        char chaveOG[] = chave.toCharArray();
        char temp; 
  
        //Realizando buuble sort para ordenar a achave em ordem alfabetica para a cifragem
        for (i = 0; i < chave.length(); i++) {
            min = i; 
            for (j = i; j < chave.length(); j++) {
                if (chaveOrdenada[min] > chaveOrdenada[j]) {
                    min = j; 
                } 
            } 
  
            if (min != i) { 
                temp = chaveOrdenada[i];
                chaveOrdenada[i] = chaveOrdenada[min];
                chaveOrdenada[min] = temp;
            } 
        } 
  
        // Adicionando posições da chave ordenada ao vetor indicador
        for (i = 0; i < chave.length(); i++) {
            for (j = 0; j < chave.length(); j++) {
                if (chaveOG[i] == chaveOrdenada[j])
                    posicoesOrdenas[i] = j;
            } 
        } 
    } 
  
     
    /**
     * Método para criptografar uma palavra
     * 
     * <p>Método que irá criptografar uma palavra recebida pelo sistema e utilizar do método de Cifra de Transposição por colunas
     * @param textoPadrao - contendo o texto a ser criptografado
     * @return - o texto criptografado
     */
    public String cripografar(String textoPadrao)
    { 
        int i, j;  
        ordenarChave(); 
  
        // Gerando a palavra criptografada
        int coluna = textoPadrao.length() / chave.length(); 
        int extrabit = textoPadrao.length() % chave.length(); 
        int exrow = (extrabit == 0) ? 0 : 1; 
        int linha = -1; 
        int tamTotal = (coluna + exrow) * chave.length(); 
        char pmat[][] = new char[(coluna + exrow)] [(chave.length())]; 
        char criptografado[] = new char[tamTotal]; 
  
        coluna = 0; 
  
        //Passando coluna e linha gerando suas respectivas letras para depois ler
        for (i = 0; i < tamTotal; i++) { 
            linha++; 
            if (i < textoPadrao.length()) { 
                if (linha == (chave.length())) { //Checando se chegou ao final da linha
                    coluna++; 
                    linha = 0; 
                } 
                pmat[coluna][linha] = textoPadrao.charAt(i); 
            } 
  
            else { 
                // Adição de símbolo para espaços entre letras
                pmat[coluna][linha] = '~'; 
            } 
        } 
  
        int tam = -1, k; 
        
        //Checando por letras criptografadas e realizando a montagem da palavra final
        for (i = 0; i < chave.length(); i++) { 
            for (k = 0; k < chave.length(); k++) { 
                if (i == posicoesOrdenas[k]) { 
                    break; //Caso i (posição num representativo da palavra) houver sido criptografado, o for é interrompido para prosseguir
                } 
            } 
            for (j = 0; j <= coluna; j++) { 
                tam++; 
                criptografado[tam] = pmat[j][k]; 
            } 
        } 
  
        String msgCrip = new String(criptografado); //Gerando uma string do array de char final
        return (new String(msgCrip)); 
    } 
  
    
    /**
     * Método para discriptografar uma mensagem
     * 
     * <p>Método irá utilizar da chave pré-selecionada para realizar a descriptografagem da mensagem
     * @param msgCrip - com o texto criptografado
     * @return texto original
     */
    public String descriptografar(String msgCrip) {  

        //Criando as variáveis necessárias para descriptografar
        char criptografado[] = msgCrip.toCharArray(); 
        int colunas = chave.length();
        int linhas = (int) Math.ceil((msgCrip.length() / colunas));
        char[][] msgNorm = new char[linhas][colunas]; //Matriz para receber a mensagem e descriptografar
        ordenarChave(); //Ordenando chave
  
        //Lendo mensagem e colocando dentro do sistema de colunas para realizar leitura
        int x = 0;//Variavel para ler de criptografado[]
        for (int i = 0; i < colunas; i++) {
            for (int k = 0; k < linhas; k++) {
                if(i+k >= criptografado.length){ //Saindo do loop caso a mensagem toda já tenha sido percorrida
                    i = colunas;
                    break;
                }
                
                if(criptografado[x] == '~'){ //Checando por espaço separado dentro do arquivo
                    msgNorm[k][i] = ' ';
                }else{
                    msgNorm[k][i] = criptografado[x];
                }
                x++;//Movendo de letra da mensagem criptografada
            }
        }
        
  
        // Gerando a String final com a mensagem desciptografada
        char msgOG[] = new char[colunas * chave.length()]; 
        x = 0;
        for (int i = 0; i < linhas; i++) {
            for (int k = 0; k < colunas; k++) {
                if(i+k >= msgOG.length){ //Saindo do loop caso a mensagem toda já tenha sido percorrida
                    i = colunas;
                    break;
                }
                msgOG[x] = msgNorm[i][posicoesOrdenas[k]];
                x++;
            }
        }
  
        String msgFinal = new String(msgOG);
        msgFinal = msgFinal.trim(); //Removendo qualquer espaço
        return (msgFinal); 
    } 
}
