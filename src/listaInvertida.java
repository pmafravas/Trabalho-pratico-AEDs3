public class listaInvertida {
    
    // Características dos Pilotos que podem ser útil para a pesquisa
    String nome;
    String sobrenome;
    String nacionalidade;
    
    /**
     * Construtor vazio para inicialização da classe sem parâmetros
     */
    public listaInvertida() {
        this.nome = null;
        this.sobrenome = null;
        this.nacionalidade = null;
    }

    /**
     * Construtor para criação de um novo conteúdo da lista com parâmetros existentes
     * @param nome
     * @param sobrenome
     * @param nacionalidade
     */
    public listaInvertida(String nome, String sobrenome, String nacionalidade) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.nacionalidade = nacionalidade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }
}
