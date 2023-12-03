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
                pmat[coluna][linha] = '-'; 
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
    public String descriptografar(String msgCrip) 
    { 
        int i, j, k; 
        char criptografado[] = msgCrip.toCharArray(); 
  
        ordenarChave(); 
  
        //Lendo mensagem e colocando dentro do sistema de colunas para realizar leitura
        int coluna = msgCrip.length(); 
        chave.length(); 
        char pmat[][] = new char[coluna][(chave.length())]; 
        int tempcnt = -1; 
  
        for (i = 0; i < chave.length(); i++) { 
            for (k = 0; k < chave.length(); k++) { 
                if (i == posicoesOrdenas[k]) { 
                    break; 
                } 
            } 
  
            for (j = 0; j < coluna; j++) { 
                tempcnt++; 
                pmat[j][k] = criptografado[tempcnt]; 
            } 
        } 
  
        // Gerando a String final com a mensagem desciptografada
        char msgOG[] = new char[coluna * chave.length()]; 
  
        k = 0; 
        for (i = 0; i < coluna; i++) { 
            for (j = 0; j < chave.length(); j++) { 
                if (pmat[i][j] != '*') { 
                    msgOG[k++] = pmat[i][j]; 
                } 
            } 
        } 
  
        msgOG[k++] = '\0'; 
        return (new String(msgOG)); 
    } 
}
