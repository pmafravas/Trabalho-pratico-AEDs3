import java.sql.Date;

public class driverNode {
    int ID; //ID como int igual requirido no PDF
    String nome; 
    String nacionalidade;
    byte num; //Números de piloto como byte (nenhum número de piloto vai acima de 99)
    Date nascimento;

    driverNode(){
        this.ID = -1;
        this.nome = null;
        this.nacionalidade = null;
        this.num = -1;
        this.nascimento = null;
    }
}
