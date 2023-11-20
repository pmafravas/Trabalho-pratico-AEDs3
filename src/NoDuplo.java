public class NoDuplo {
    NoDuplo proximo;
    NoDuplo anterior;
        
    // Características dos Pilotos que podem ser útil para a pesquisa
    String palavra;
    int incidencia;

    public NoDuplo(String palavra){
        this.palavra = palavra;
        this.proximo = null;
        this.anterior= null;
        this.incidencia = 1; //Definindo valor base de 1 incidencia. Será tratado na hora da inserção
    }
    
    public String getPalavra() {
        return palavra;
    }

    public void setPalavra(String palavra) {
        this.palavra = palavra;
    }

    public int getIncidencia() {
        return incidencia;
    }

    public void setIncidencia(int incidencia) {
        this.incidencia = incidencia;
    }

}
