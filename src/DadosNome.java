import java.util.*;

/**
 * A classe Indice serve como auxiliar para uma lista invertida, onde todos os IDs dos nomes serão guardados, e 
 * sua incidencia ao longo do arquivo contabilizada.
 */
public class DadosNome {
    int incidencia;
    List<Integer> localID;
    
    /**
     * Construtor para receber novo ID de um nome
     * @param ID
     */
    public DadosNome(int ID){
        this.incidencia = 1; //Aumenta incidencia para 1
        this.localID = new ArrayList<>(); //Cria arraylist para receber todos os IDs repetentes
        this.localID.add(ID);
    }


    /**
     * Método para aumentar a incidencia de aparição de um determinado nome
     */
    void aumentarIncidencia(){
        this.incidencia++;
    }

    /**
     * Método para adicionar um ID de um nome
     * <p>O ID é verificado se já foi adicionado para não realizar dupla inserção.
     * @param ID
     */
    void addLocalID(int ID){
        if(!this.localID.contains(ID)){ //Checando se o ID já foi adicionado previamente
            this.localID.add(ID); //Adicionando ID ao Array
            aumentarIncidencia(); //Aumentando incidencia do nome
        }
    }
}
