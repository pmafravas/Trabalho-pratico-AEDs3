public class NoDeHuffman {
    Character simbolo;
    Integer frequencia;
    NoDeHuffman esquerda, direita;
    
    public NoDeHuffman(Character simbolo, Integer frequencia) {
        this.simbolo = simbolo;
        this.frequencia = frequencia;
    }
    
    public NoDeHuffman(Integer frequencia, NoDeHuffman esquerda, NoDeHuffman direita) {
        this.frequencia = frequencia;
        this.esquerda = esquerda;
        this.direita = direita;
    }    

    public int comparar(NoDeHuffman outro) {
        return this.frequencia - outro.frequencia;
    }
}
