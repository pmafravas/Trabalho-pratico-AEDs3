import java.io.FileNotFoundException;

/*
 * Arquivo principal do TP1 AED3
 * Autor: Pedro Mafra Vasconcelos
 * 
 * A respeito: Utilizar alguma base de dados existente para realizar inserção no código, simulando os inúmeros registros de uma base de dados,
 * utilizando as técnicas de CRUD, armazenamento, ordenação e listagem apresentados nas aulas de AEDs3.
 * 
 * Base de dados: drives.csv - Todos os pilotos que já passaram pela Fórmula 1
 * TODO: Implementar carga da base de dados
 * TODO: Implementar CRUD em arquivo sequencial
 * TODO: Lista de valores com separado '-' (Lista para salvar no .db)
 * TODO: (EXTRA) Algoritmo de Ordenação Externa da base - limites: 10 registros e 2 caminhos
 * 
 * (menu em console)
 *  
 */

public class TP {
    public static void main(String[] args) throws FileNotFoundException {
        csvRead.csvReader();
    }
}