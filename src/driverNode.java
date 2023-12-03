import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class driverNode {
    public int ID; //ID de cada piloto gerado pelo CRUD
    private String reference; //Identificação do piloto dentro dos documentos da FIA (orgão responsável pela Formula 1)   
    private String name;
    private String surname; 
    private String nationality;
    private String driverNum; //O Numero de piloto deve ser uma String pois até 2014, pilotos não haviam números próprios, fazendo com que qualquer piloto que tenha corrido até 2013 efetivamente sem número.
    private LocalDate date; //Data de nascimento
    private String code; //Abreviação por qual os pilotos atendiam em corridas

    //Variáveis que auxiliarão a conversão do registro para byte
    private DataInputStream DIS;
    private DataOutputStream DOS;
    private ByteArrayInputStream BAS;
    private ByteArrayOutputStream BOS;

    Cifragem cript = new Cifragem(); //Utilizado para criptografagem da referencia do

    /**
     * Construtor básico de um registro sem informações
     */
    public driverNode(){
        this.ID = 0;
        this.reference = null;
        this.name = null;
        this.surname = null;
        this.nationality = null;
        this.driverNum = null;
        this.date = null;
        this.code = null;
    }

    /**
     * Método de registro de novo piloto
     * @param reference
     * @param name
     * @param surname
     * @param nationality
     * @param driverNum
     * @param date
     * @param code
     */
    public void registrar(String reference, String name, String surname, String nationality, String driverNum, LocalDate date, String code){
        this.reference = cript.cripografar(reference); //Criptografando a referencia do piloto
        this.name = name;
        this.surname = surname;
        this.nationality = nationality;
        this.driverNum = driverNum;
        this.date = date; 
        this.code = code;
    }

    /**
     * Método para copiar o registro de um registro anterior
     * @param objeto
     */
    public void registrar(driverNode objeto){
        this.ID = objeto.ID;
        this.reference = cript.cripografar(objeto.reference); //Criptografando a referencia do piloto
        this.name = objeto.name;
        this.surname = objeto.surname;
        this.nationality = objeto.nationality;
        this.driverNum = objeto.driverNum;
        this.date = objeto.date; 
        this.code = objeto.code;
    }


    /*
     * Métodos de códigos Get e Set para os atributos do Node
     */
    public int getID(){
        return ID;
    }

    public void setID(int ID){
        this.ID = ID;
    }

    public String getReference(){
        return cript.descriptografar(reference); //Enviando referencia descriptografada
    }

    public void setReference(String reference){
        this.reference = reference;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getSurname(){
        return surname;
    }

    public void setSurname(String surname){
        this.surname = surname;
    }

    public String getNatiotanlity(){
        return nationality;
    }

    public void setNatiotanlity(String nationality){
        this.nationality = nationality;
    }

    public String getDriverNumber(){
        return driverNum;
    }

    public void setDriverNumber(String driverNum){
        this.driverNum = driverNum;
    }

    public LocalDate getDate(){
        return date;
    }

    public void setDate(LocalDate date){
        this.date = date;
    }

    public String getCode(){
        return code;
    }

    public void setCode(String code){
        this.code = code;
    }

    /**
     * Método para transforma todos os objetos em um vetor de bytes
     * @return Byte Array com as informações de piloto
     * @throws IOException
     */
    public byte[] toByteArray() throws IOException {
        this.BOS = new ByteArrayOutputStream();
        this.DOS = new DataOutputStream(BOS);
        DOS.writeInt(this.ID);
        DOS.writeUTF(this.reference);
        DOS.writeUTF(this.name);
        DOS.writeUTF(this.surname);
        DOS.writeUTF(this.nationality);
        DOS.writeUTF(this.driverNum);
        DOS.writeUTF(this.date.toString());
        DOS.writeUTF(this.code);

        return BOS.toByteArray();
    }

    /**
     * Método para transformar um vetor de bytes lido em um objeto 
     * @param ba (Byte Array)
     * @throws IOException
     */
    public void fromByteArray(byte[] ba) throws IOException {
        this.BAS = new ByteArrayInputStream(ba);
        this.DIS = new DataInputStream(BAS);
        this.ID = DIS.readInt();
        this.reference = DIS.readUTF();
        this.name = DIS.readUTF();
        this.surname = DIS.readUTF();
        this.nationality = DIS.readUTF();
        this.driverNum = DIS.readUTF();
        String dataAux = DIS.readUTF();//Lendo data como String
        this.code = DIS.readUTF();

        //Convertendo a String para data
        LocalDate formatDate = LocalDate.parse(dataAux, DateTimeFormatter.ISO_DATE);
        this.date = formatDate;
    }

    /**
     * Método para imprimir de forma formatada as informações de um piloto
     */
    public void printRegistro(){
        
        System.out.println("\nID: " + this.ID);        
        System.out.println("Referencia: " + this.reference);
        System.out.println("Codigo: " + this.code);
        System.out.println("Numero: " + this.driverNum);
        System.out.println("Nome: " + this.name);
        System.out.println("Sobrenome: " + this.surname);
        System.out.println("Nacionalidade: " + this.nationality);
        System.out.println("Data de Nascimento: " + this.date.toString());
        System.out.println("");//Quebra de linha
    }

    /**
     * Método para imprimir somente uma prévia de um determinado piloto
     * <p> Utilizado para a Lista Invertida
     */
    public void printPreview(){

        System.out.println("\nPiloto: " + this.name + " " + this.surname);
        System.out.println("ID: " + this.ID +"\n");

    }
}