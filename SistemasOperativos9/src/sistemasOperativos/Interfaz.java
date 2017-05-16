/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemasOperativos;

import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author javier
 */
public final class Interfaz extends javax.swing.JFrame {

    int numeroProcesos = 0;
    int contadorGlobal = 0;
    int id = 0;
    int procesosListos = 0;
    String idAux = "";
    int quantum = 0;

    boolean keyP = false, keyE = false,
            keyC = false, keyI = false,
            keyT = false, keyN = false,
            keyM = false, keyS = false, keyR = false, keyV=false;

    private final ArrayList<Procesos> procesos = new ArrayList<>();
    private final ArrayList<Procesos> listos = new ArrayList<>();
    private final ArrayList<Procesos> bloqueados = new ArrayList<>();
    private final ArrayList<Procesos> terminados = new ArrayList<>();
    private final ArrayList<Memoria> listaMemoria = new ArrayList<>();
    private final ArrayList<Procesos> suspendidos = new ArrayList<>();
    private final ArrayList<Procesos> memoriaAux = new ArrayList<>();
    private StringBuilder str = new StringBuilder();
    
    private final ArrayList<Memoria> memoriaVirtual = new ArrayList<>();
    private final ArrayList<String> listaAux = new ArrayList<>();
    
    

    DefaultTableModel modelo = new DefaultTableModel();
    DefaultTableModel modeloTerminados = new DefaultTableModel();
    DefaultTableModel modeloMemoria = new DefaultTableModel();

    /**
     * Creates new form Interfaz
     */
    public Interfaz() {

        initComponents();
        mostrarDatos();
        mostrarDatosTerminados();
        crearTablaMemoria();
        inicializarTabla();

        tablaDatos.setVisible(true);
        memoria.setVisible(true);
    }

    public void colorear() {
        memoria.setDefaultRenderer(Object.class, new MiRender());
    }
    
    /***********************Memoria virtual*********************************
    
    * 
    * 
    * 
    */
    
    public void guardarArchivo() {
        try {
            borrarArchivo();
            File archivo = new File("Archivo.txt");
            BufferedWriter bw;
            
            
            str.delete(0, str.length());
            
            for(int i=0; i<memoriaVirtual.size(); i++){
                if(!memoriaVirtual.get(i).getId().equals(" ")){
                    str.append("Id: "+memoriaVirtual.get(i).getId()+" M: "+mostrarPaginas(i)+" | \n ");
                }
                    
                
            }
            
            bw = new BufferedWriter(new FileWriter(archivo));
            bw.write(str.toString());
            
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void borrarArchivo(){
        
        File fichero = new File("Archivo.txt");
        
        if (fichero.delete())
            System.out.println("El fichero ha sido borrado satisfactoriamente");
        else
            System.out.println("El fichero no puede ser borrado");
        
    }
    
    public int mostrarPaginas(int pos){
        int cont=0;
        
        if(!memoriaVirtual.get(pos).getM1().equals(" ")){
            cont++;
            
        }
         if(!memoriaVirtual.get(pos).getM2().equals(" ")){
            cont++;
        }
         if(!memoriaVirtual.get(pos).getM3().equals(" ")){
            cont++;
        }
         if(!memoriaVirtual.get(pos).getM4().equals(" ")){
            cont++;
        }
         if(!memoriaVirtual.get(pos).getM5().equals(" ")){
            cont++;
        }
        
        return cont;
    }
    
    public void moverUltimaPagina(){
        
        String idAux2;
        int pos;
        int cont=0;
        
        for(int i=0; i<listaMemoria.size(); i++){
            
            idAux2=listaMemoria.get(i).getId();
          
            if(!buscadorDeId(idAux2)){
                 cont=0;
                
                //System.out.println("Id: "+idAux);
                pos=seEncontroVeces(idAux2);
                //System.out.println("Cuantas veces se encuentra "+pos );
                
                for(int j=0; j<listaMemoria.size(); j++){
                    
                    if(idAux2.equals("")){
                        
                        break;
                    }
                    
                    if(idAux2.equals(idAux)){
                        break;
                    }
                    
                    if(listaMemoria.get(j).getId().equals(idAux2)){
                        cont++;
                        
                        if(cont==pos){
                            listaAux.add(idAux2);
                            pasarMemoriaVirtual(j);
                            borrarUltimaPagina(j);
                            
                            break;
                            
                            
                        }
                            
                    }
                    
                    
                }
            }
            
        }
        
        
        
    }
    
    
    public boolean verificaSiEstaEnListaAux(){
        
        if(listaAux.contains(idAux)){
            return true;
        }
        else{
            return false;
            
        }
        
        
    }
    
    
    public void mostrarAux(){
        auxTxt.setText("");
        for(int i=0; i<memoriaAux.size(); i++){
            
            auxTxt.append("Id: "+memoriaAux.get(i).getId()+" M: "
                    +memoriaAux.get(i).getMemoria()+"\n");
            
            
        }
        
        
        
    }
    
    public boolean verificaSiEntra(){
        
        
        
        
        
        if(comprobarEspacio(5)){
            
             for(int i=0; i<memoriaVirtual.size(); i++){
                    
                 if(memoriaVirtual.get(i).getId().equals(idAux)){
                        
                       agregarAMemoria(memoriaVirtual.get(i).getId(), 
                               memoriaVirtual.get(i).getM1(),
                               memoriaVirtual.get(i).getM2(),
                               memoriaVirtual.get(i).getM3(),
                               memoriaVirtual.get(i).getM4(),
                               memoriaVirtual.get(i).getM5());
                       
                       
                       memoriaVirtual.remove(i);
                       
                       listaAux.remove(idAux);
                       
                       
                       guardarArchivo();
                       break;
                        
                 }
             
             }
            
            return true;
            
        }
        else{
            return false;
                  
        }
        
        
    }
    
    public void agregarAMemoria(String idx, String m1,String m2, String m3, String m4, String m5){
        
        for(int i=0; i<listaMemoria.size(); i++){
            
            if(listaMemoria.get(i).getId().equals(" ")){
                listaMemoria.get(i).setId(idx);
                listaMemoria.get(i).setM1(m1);
                listaMemoria.get(i).setM2(m2);
                listaMemoria.get(i).setM3(m3);
                listaMemoria.get(i).setM4(m4);
                listaMemoria.get(i).setM5(m5);
               return;
            }
            
        }
        
        
    }
    
    
    public boolean buscadorDeId(String idx){
        for(int i=0; i<listaAux.size(); i++){
             
            if(listaAux.get(i).equals(idx)){
                return true;
            }
            
            
        }
        return false;
        
    }
    
    public void borrarUltimaPagina(int pos){
        listaMemoria.get(pos).setId(" ");
        listaMemoria.get(pos).setM1(" ");
        listaMemoria.get(pos).setM2(" ");
        listaMemoria.get(pos).setM3(" ");
        listaMemoria.get(pos).setM4(" ");
        listaMemoria.get(pos).setM5(" ");
        
        
    }
    
    public void pasarMemoriaVirtual(int pos){
        
        memoriaVirtual.add(new Memoria(
        "",
        listaMemoria.get(pos).getId(),
        listaMemoria.get(pos).getM1(),
                listaMemoria.get(pos).getM2(),
                listaMemoria.get(pos).getM3(),
                listaMemoria.get(pos).getM4(),
             listaMemoria.get(pos).getM5()
        ));
        
    }
    
    public int seEncontroVeces(String idx){
        int cont=0;
        for(int i=0; i<listaMemoria.size(); i++){
            
            if(listaMemoria.get(i).getId().equals(idx)){
                cont++;
            }
            
        }
        
        return cont;
        
        
    }
    
    
    
    
    ///Memoria***************************************************************
    /*
     R-> reservado
     A-> Agregado
     B-> Bloqueados
     E-> Ejecucion
    
     */
    public void inicializarTabla() {
        listaMemoria.add(new Memoria("0", "", "R", "R", "R", "R", "R"));
        listaMemoria.add(new Memoria("1", "", "R", "R", "R", "R", "R"));

        for (int i = 2; i < 28; i++) {

            listaMemoria.add(new Memoria(Integer.toString(i), " ", " ", " ", " ", " ", " "));

        }

    }

    public void mostrarMemoria() {
        modeloMemoria.getDataVector().removeAllElements();
        colorear();
        for (int i = 0; i < listaMemoria.size(); i++) {

            meterDatosMemoria(listaMemoria.get(i).getMarco(),
                    listaMemoria.get(i).getId(),
                    listaMemoria.get(i).getM1(),
                    listaMemoria.get(i).getM2(),
                    listaMemoria.get(i).getM3(),
                    listaMemoria.get(i).getM4(),
                    listaMemoria.get(i).getM5());

        }

    }

    public void memoriaBloqueados(String iden) {

        for (int i = 0; i < listaMemoria.size(); i++) { //Modificamos su color

            if (listaMemoria.get(i).getId().equals(iden)) {
                cambiarValorBloqueados(i);
            }
        }

    }

    public void memoriaEjecucion(String iden) {
        for (int i = 0; i < listaMemoria.size(); i++) { //Modificamos su color de ejecucion

            if (listaMemoria.get(i).getId().equals(iden)) {
                cambiarValorEjecucion(i);
            }
        }
    }

    public void memoriaListos(String iden) {
        for (int i = 0; i < listaMemoria.size(); i++) { //Modificamos su color de listos

            if (listaMemoria.get(i).getId().equals(iden)) {
                cambiarValorListos(i);
            }
        }

    }

    public void memoriaTerminados(String iden) {
        System.out.println("Elimina "+iden);
        for (int i = 0; i < listaMemoria.size(); i++) { //Modificamos su color de listos

            if (listaMemoria.get(i).getId().equals(iden)) {
                liberarEspacio(i);
            }
        }

    }
    public void memoriaSuspendidos(String iden){
        
        for (int i = 0; i < listaMemoria.size(); i++) { //Modificamos su color de listos

            if (listaMemoria.get(i).getId().equals(iden)) {
                limpiarValorBloqueados(i);
            }
        }
        
        
    }

    public void liberarEspacio(int pos) {
        //int contAux=0;
        Memoria m = (Memoria) listaMemoria.get(pos);

        m.setId(" ");
        m.setM1(" ");
        m.setM2(" ");
        m.setM3(" ");
        m.setM4(" ");
        m.setM5(" ");

    }
    
    public void limpiarValorBloqueados(int pos){
        Memoria m = (Memoria) listaMemoria.get(pos);

        if (m.getM1().equals("B")) {
            m.setM1(" ");
        }
        if (m.getM2().equals("B")) {
            m.setM2(" ");
        }
        if (m.getM3().equals("B")) {
            m.setM3(" ");
        }
        if (m.getM4().equals("B")) {
            m.setM4(" ");
        }
        if (m.getM5().equals("B")) {
            m.setM5(" ");
        }
        
        m.setId(" ");
        
        
        
    }

    public void cambiarValorListos(int pos) {
        //int contAux=0;
        Memoria m = (Memoria) listaMemoria.get(pos);

        if (m.getM1().equals("B")) {
            m.setM1("A");
        }
        if (m.getM2().equals("B")) {
            m.setM2("A");
        }
        if (m.getM3().equals("B")) {
            m.setM3("A");
        }
        if (m.getM4().equals("B")) {
            m.setM4("A");
        }
        if (m.getM5().equals("B")) {
            m.setM5("A");
        }

        if (m.getM1().equals("E")) {
            m.setM1("A");
        }
        if (m.getM2().equals("E")) {
            m.setM2("A");
        }
        if (m.getM3().equals("E")) {
            m.setM3("A");
        }
        if (m.getM4().equals("E")) {
            m.setM4("A");
        }
        if (m.getM5().equals("E")) {
            m.setM5("A");
        }

    }

    public void cambiarValorEjecucion(int pos) {
        //int contAux=0;
        Memoria m = (Memoria) listaMemoria.get(pos);

        if (m.getM1().equals("A")) {
            m.setM1("E");
        }
        if (m.getM2().equals("A")) {
            m.setM2("E");
        }
        if (m.getM3().equals("A")) {
            m.setM3("E");
        }
        if (m.getM4().equals("A")) {
            m.setM4("E");
        }
        if (m.getM5().equals("A")) {
            m.setM5("E");
        }

        if (m.getM1().equals("B")) {
            m.setM1("E");
        }
        if (m.getM2().equals("B")) {
            m.setM2("E");
        }
        if (m.getM3().equals("B")) {
            m.setM3("E");
        }
        if (m.getM4().equals("B")) {
            m.setM4("E");
        }
        if (m.getM5().equals("B")) {
            m.setM5("E");
        }

    }

    public void cambiarValorBloqueados(int pos) {
        //int contAux=0;
        Memoria m = (Memoria) listaMemoria.get(pos);

        if (m.getM1().equals("E")) {
            m.setM1("B");
        }
        if (m.getM2().equals("E")) {
            m.setM2("B");
        }
        if (m.getM3().equals("E")) {
            m.setM3("B");
        }
        if (m.getM4().equals("E")) {
            m.setM4("B");
        }
        if (m.getM5().equals("E")) {
            m.setM5("B");
        }

    }

    public void agregarMemoria(int iden, int tam) {

        int pos;

        while (tam != 0) {

            for (pos = 0; pos < listaMemoria.size(); pos++) {

                if (listaMemoria.get(pos).getId().equals(" ")) {
                    break;
                }
            }

            listaMemoria.get(pos).setId(Integer.toString(iden));

            for (int i = 0; i < 5; i++) {

                if (tam == 0) {
                    break;
                }

                tam--;
                agregarPagina(pos);

            }

        }

    }

    public void agregarPagina(int pos) {

        Memoria m = (Memoria) listaMemoria.get(pos);

        if (m.getM1().equals(" ")) {
            m.setM1("A");
        } else if (m.getM2().equals(" ")) {
            m.setM2("A");
        } else if (m.getM3().equals(" ")) {
            m.setM3("A");
        } else if (m.getM4().equals(" ")) {
            m.setM4("A");
        } else if (m.getM5().equals(" ")) {
            m.setM5("A");
        } else {
            System.out.print("Problemas en agregarPagina");
        }

    }

    public boolean comprobarEspacio(int tam) {

        if (tamanoDisponible() >= tam) {
            return true;
        } else {
            return false;
        }

    }

    public int tamanoDisponible() {

        int c = 0;
        for (int i = 0; i < listaMemoria.size(); i++) {
            if (listaMemoria.get(i).getId().equals(" ")) {
                c++;
            }
        }

        return (c * 5);

    }

    public void crearTablaMemoria() {
        modeloMemoria.addColumn("M");
        modeloMemoria.addColumn("Id");
        modeloMemoria.addColumn("");
        modeloMemoria.addColumn("");
        modeloMemoria.addColumn("");
        modeloMemoria.addColumn("");
        modeloMemoria.addColumn("");
        memoria.setModel(modeloMemoria);
    }

    public void meterDatosMemoria(String m, String iden, String p1, String p2, String p3,
            String p4, String p5) {

        String[] datos = new String[7];
        datos[0] = m;
        datos[1] = iden;
        datos[2] = p1;
        datos[3] = p2;
        datos[4] = p3;
        datos[5] = p4;
        datos[6] = p5;

        modeloMemoria.addRow(datos);
        memoria.setModel(modeloMemoria);

    }

    //Memoria ***************************************************************
    public void meterDatosAlaTabla() {
        //espera=servicio-restante

        int esperaAux;
        String llegada;

        /*String id,String op,String res,String ttl,String tf,
         String ret,String ser,String esp,String resp, String estado, String rest*/
        for (int i = 0; i < listos.size(); i++) {
            datosEnTabla(listos.get(i).getId(),
                    "",
                    "", "",
                    "", "", "", "",
                    "", "nuevos", "");
        }

        for (int i = 0; i < procesos.size(); i++) {

            if (procesos.get(i).getId().equals(idAux)) {

                if (procesos.get(i).getTiempoEspera().equals("")) {
                    //procesos.get(i).setTiempoEspera("0");
                    llegada = procesos.get(i).getTiempoLlegada();
                    esperaAux = contadorGlobal - Integer.parseInt(llegada);
                    procesos.get(i).setTiempoEspera(Integer.toString(esperaAux));
                }

                datosEnTabla(procesos.get(i).getId(),
                        procesos.get(i).getOp1() + procesos.get(i).getOperators() + procesos.get(i).getOp2(),
                        "", procesos.get(i).getTiempoLlegada(),
                        "", "", procesos.get(i).getTiempoTranscurrido(), procesos.get(i).getTiempoEspera(),
                        procesos.get(i).getTiempoRespuesta(), "ejecucion", procesos.get(i).getTiempoMaximo());
            } else {

                llegada = procesos.get(i).getTiempoLlegada();
                esperaAux = contadorGlobal - Integer.parseInt(llegada);
                procesos.get(i).setTiempoEspera(Integer.toString(esperaAux));

                datosEnTabla(procesos.get(i).getId(),
                        procesos.get(i).getOp1() + procesos.get(i).getOperators() + procesos.get(i).getOp2(),
                        "", procesos.get(i).getTiempoLlegada(),
                        "", "", procesos.get(i).getTiempoTranscurrido(), procesos.get(i).getTiempoEspera(),
                        procesos.get(i).getTiempoRespuesta(), "listos", procesos.get(i).getTiempoMaximo());
            }

        }
        for (int i = 0; i < bloqueados.size(); i++) {

            llegada = bloqueados.get(i).getTiempoLlegada();
            esperaAux = contadorGlobal - Integer.parseInt(llegada);
            bloqueados.get(i).setTiempoEspera(Integer.toString(esperaAux));

            datosEnTabla(bloqueados.get(i).getId(),
                    bloqueados.get(i).getOp1() + "+" + bloqueados.get(i).getOp2(),
                    "", bloqueados.get(i).getTiempoLlegada(),
                    "", "", bloqueados.get(i).getTiempoTranscurrido(), bloqueados.get(i).getTiempoEspera(), // espera
                    bloqueados.get(i).getTiempoRespuesta(), "bloqueados", bloqueados.get(i).getTiempoMaximo());

        }
        for (int i = 0; i < terminados.size(); i++) {

            if (terminados.get(i).getResult().equals("E")) {
                datosEnTabla(terminados.get(i).getId(),
                        terminados.get(i).getOp1() + " "
                        + terminados.get(i).getOperators() + " "
                        + terminados.get(i).getOp2(),
                        terminados.get(i).getResult(),
                        terminados.get(i).getTiempoLlegada(),
                        terminados.get(i).getTiempoFinalizacion(),
                        terminados.get(i).getTiempoRetorno(),
                        terminados.get(i).getTiempoServicio(),
                        terminados.get(i).getTiempoEspera(), // espera
                        terminados.get(i).getTiempoRespuesta(),
                        "Error",
                        terminados.get(i).getTiempoRestante());
            } else {
                datosEnTabla(terminados.get(i).getId(),
                        terminados.get(i).getOp1() + " "
                        + terminados.get(i).getOperators() + " "
                        + terminados.get(i).getOp2(),
                        terminados.get(i).getResult(),
                        terminados.get(i).getTiempoLlegada(),
                        terminados.get(i).getTiempoFinalizacion(),
                        terminados.get(i).getTiempoRetorno(),
                        terminados.get(i).getTiempoServicio(),
                        terminados.get(i).getTiempoEspera(), // espera
                        terminados.get(i).getTiempoRespuesta(),
                        "Normal",
                        terminados.get(i).getTiempoRestante());
            }

        }
    }

    public void mostrarDatos() {
        modelo.addColumn("Id");
        modelo.addColumn("Estado");
        modelo.addColumn("Op");
        modelo.addColumn("Resultado");
        modelo.addColumn("TLL");
        modelo.addColumn("TF");
        modelo.addColumn("TRetorno");
        modelo.addColumn("TServicio");
        modelo.addColumn("TEspera");
        modelo.addColumn("TRespuesta");
        modelo.addColumn("TRestante");
        tablaDatos.setModel(modelo);
    }

    public void mostrarDatosTerminados() {
        modeloTerminados.addColumn("Id");
        modeloTerminados.addColumn("Op");
        modeloTerminados.addColumn("Resultado");
        modeloTerminados.addColumn("TLL");
        modeloTerminados.addColumn("TF");
        modeloTerminados.addColumn("TRetorno");
        modeloTerminados.addColumn("TServicio");
        modeloTerminados.addColumn("TEspera");
        modeloTerminados.addColumn("TRespuesta");
        terminadosT.setModel(modeloTerminados);

    }

    public void datosEnTablaTerminados(String id, String op, String resultado,
            String tll, String tf, String tRetorno, String tServicio, String tEspera,
            String tRespuesta) {

        String[] datos = new String[9];

        datos[0] = id;
        datos[1] = op;
        datos[2] = resultado;
        datos[3] = tll;
        datos[4] = tf;
        datos[5] = tRetorno;
        datos[6] = tServicio;
        datos[7] = tEspera;
        datos[8] = tRespuesta;

        modeloTerminados.addRow(datos);
        terminadosT.setModel(modeloTerminados);

    }

    public void datosEnTabla(String id, String op, String res, String ttl, String tf,
            String ret, String ser, String esp, String resp, String estado, String rest) {
        String[] datos = new String[11];

        datos[0] = id;
        datos[1] = estado;
        datos[2] = op;
        datos[3] = res;
        datos[4] = ttl;
        datos[5] = tf;
        datos[6] = ret;
        datos[7] = ser;
        datos[8] = esp;
        datos[9] = resp;
        datos[10] = rest;

        modelo.addRow(datos);
        tablaDatos.setModel(modelo);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        tNumeroProcesos = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        tQuantum = new javax.swing.JTextField();
        ejecutar = new javax.swing.JButton();
        registrar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        lProcesosEspera = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lContadorGlobal = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lContadorBloqueados = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tProcesosListos = new javax.swing.JTextArea();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tProcesoEjecucion = new javax.swing.JTextArea();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        ltiempoTranscurrido = new javax.swing.JLabel();
        lTiempoRestante = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tBloqueados = new javax.swing.JTextArea();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tablaDatos = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();
        lQuantum = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        terminadosT = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        memoria = new javax.swing.JTable();
        jScrollPane7 = new javax.swing.JScrollPane();
        nuevosT = new javax.swing.JTextArea();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        suspendidosT = new javax.swing.JTextArea();
        jLabel13 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        auxTxt = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sistemas operativos");

        jLabel1.setText("Numero Procesos: ");

        jLabel2.setText("Quantum: ");

        ejecutar.setText("Ejecutar");
        ejecutar.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                ejecutarFocusGained(evt);
            }
        });
        ejecutar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ejecutarActionPerformed(evt);
            }
        });
        ejecutar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ejecutarKeyPressed(evt);
            }
        });

        registrar.setText("Registrar");
        registrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registrarActionPerformed(evt);
            }
        });

        jLabel3.setText("Procesos en espera: ");

        lProcesosEspera.setText("0");

        jLabel4.setText("Contador Global:");

        lContadorGlobal.setText("0");

        jLabel5.setText("Contador bloqueados:");

        lContadorBloqueados.setText("0");

        jLabel6.setText("Procesos listos.");

        tProcesosListos.setColumns(20);
        tProcesosListos.setRows(5);
        jScrollPane1.setViewportView(tProcesosListos);

        jLabel7.setText("Proceso en ejecucion.");

        tProcesoEjecucion.setColumns(20);
        tProcesoEjecucion.setRows(5);
        jScrollPane2.setViewportView(tProcesoEjecucion);

        jLabel8.setText("Tiempo transcurrido:");

        jLabel9.setText("Tiempo restante:");

        ltiempoTranscurrido.setText("0");

        lTiempoRestante.setText("0");

        jLabel10.setText("Bloqueados");

        tBloqueados.setColumns(20);
        tBloqueados.setRows(5);
        jScrollPane3.setViewportView(tBloqueados);

        jLabel11.setText("Procesos terminados.");

        tablaDatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane5.setViewportView(tablaDatos);

        jLabel12.setText("Quantum:");

        lQuantum.setText("0");

        terminadosT.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane6.setViewportView(terminadosT);

        memoria.setBorder(new javax.swing.border.MatteBorder(null));
        jScrollPane4.setViewportView(memoria);

        nuevosT.setColumns(20);
        nuevosT.setRows(5);
        jScrollPane7.setViewportView(nuevosT);

        jLabel14.setText("Nuevos");

        suspendidosT.setColumns(20);
        suspendidosT.setRows(5);
        jScrollPane8.setViewportView(suspendidosT);

        jLabel13.setText("Suspendidos");

        jLabel15.setText("Memoria auxiliar");

        auxTxt.setColumns(20);
        auxTxt.setRows(5);
        jScrollPane9.setViewportView(auxTxt);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5)
                    .addComponent(jScrollPane6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel13))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(ejecutar)
                                .addGap(49, 49, 49)
                                .addComponent(registrar))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2))
                                .addGap(12, 12, 12)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tQuantum, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tNumeroProcesos, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addGap(6, 6, 6)
                                        .addComponent(lProcesosEspera))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addGap(6, 6, 6)
                                        .addComponent(lContadorBloqueados)))
                                .addGap(31, 31, 31)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lContadorGlobal))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel12)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lQuantum))))
                            .addComponent(jLabel6)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(116, 116, 116)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(ltiempoTranscurrido))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel9)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lTiempoRestante))
                                    .addComponent(jLabel10)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7)))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel11)))
                .addGap(47, 47, 47)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(jLabel15))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(11, 11, 11)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGap(11, 11, 11)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(ltiempoTranscurrido))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(lTiempoRestante))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGap(8, 8, 8))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(jLabel1))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(tNumeroProcesos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(11, 11, 11)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(tQuantum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(19, 19, 19)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ejecutar)
                            .addComponent(registrar))
                        .addGap(22, 22, 22)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(lProcesosEspera)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel4)
                                .addComponent(lContadorGlobal)))
                        .addGap(13, 13, 13)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lContadorBloqueados)
                                .addComponent(jLabel12)
                                .addComponent(lQuantum)))
                        .addGap(12, 12, 12)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 476, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void registrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registrarActionPerformed
        // TODO add your handling code here:

        int op1, op2, op, tiempoMaximo, numeroMemoria;
        String[] operadores = {"+", "/", "*", "%", "+", "sqrt", "-"};

        numeroProcesos = Integer.parseInt(tNumeroProcesos.getText());
        quantum = Integer.parseInt(tQuantum.getText());

        lProcesosEspera.setText(Integer.toString(numeroProcesos));

        tQuantum.setText("");
        tNumeroProcesos.setText("");

        for (int i = 1; i <= numeroProcesos; i++) {

            op1 = (int) (Math.random() * 25 + 1);
            op = (int) (Math.random() * 2 + 0);
            op2 = (int) (Math.random() * 25 + 1);
            tiempoMaximo = (int) (Math.random() * 15 + 5);
            numeroMemoria = (int) (Math.random() * 10 + 7);

            if (!comprobarEspacio(numeroMemoria)) {

                listos.add(new Procesos(
                        Integer.toString(id),
                        "", Integer.toString(op1), operadores[op],
                        Integer.toString(op2), "", //operaciones
                        Integer.toString(tiempoMaximo), "",
                        "0", "", "", "0", "", "", Integer.toString(contadorGlobal),
                        Integer.toString(tiempoMaximo),
                        Integer.toString(quantum), "0" //tiempos
                        , Integer.toString(numeroMemoria)
                ));

            } else {

                procesos.add(new Procesos(
                        Integer.toString(id),
                        "", Integer.toString(op1), operadores[op],
                        Integer.toString(op2), "", //operaciones
                        Integer.toString(tiempoMaximo), "",
                        "0", "", "", "0", "", "", Integer.toString(contadorGlobal),
                        Integer.toString(tiempoMaximo),
                        Integer.toString(quantum), "0" //tiempos
                        , Integer.toString(numeroMemoria)
                ));
                agregarMemoria(id, numeroMemoria);

            }

            id++;
        }


    }//GEN-LAST:event_registrarActionPerformed

    private void ejecutarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ejecutarActionPerformed
        // TODO add your handling code here:

        procesosListos = numeroProcesos;
        if (procesosListos < 4) {
            procesosListos = 0;
        } else {
            procesosListos -= 4;
        }

        lProcesosEspera.setText(Integer.toString(procesosListos));

        mostrarProcesos();
        mostrarMemoria();

        try {
            simularContador();
            // task.isDone();
        } catch (InterruptedException ex) {
            Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_ejecutarActionPerformed

    private void ejecutarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ejecutarKeyPressed
        // TODO add your handling code here:
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_E:
                if (!keyP || !keyT) {
                    keyE = true;
                }

                break;
            case KeyEvent.VK_P:
                keyP = true;
                break;
            case KeyEvent.VK_C:
                keyC = true;
                tablaDatos.setVisible(false);
                modelo.getDataVector().removeAllElements();
                synchronized (this) {
                    notifyAll();
                }
                break;
            case KeyEvent.VK_I:

                if (!keyP || !keyT) {
                    keyI = true;
                }

                break;
            case KeyEvent.VK_T:
                keyT = true;

                break;
            case KeyEvent.VK_N:

                if (!keyP || !keyT) {

                    keyN = true;

                }

                break;
            case KeyEvent.VK_M:
                keyM = true;

                break;
            case KeyEvent.VK_S:
                keyS = true;
                break;
            case KeyEvent.VK_R:
                keyR = true;
                break;
            case KeyEvent.VK_V:
                keyV=true;
                break;

        }


    }//GEN-LAST:event_ejecutarKeyPressed

    private void ejecutarFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ejecutarFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_ejecutarFocusGained

    public void simularContador() throws InterruptedException {
        Executors.newSingleThreadExecutor().execute(new Runnable() {

            @Override
            public void run() {
                try {
                    proceso();
                    //To change body of generated methods, choose Tools | Templates.
                } catch (InterruptedException ex) {
                    Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });

    }

    public void mandarSuspendidos() {

        if (!bloqueados.isEmpty()) {

            Procesos p = (Procesos) bloqueados.get(0);

            suspendidos.add(
                    new Procesos(p.getId(), p.getNombre(), p.getOp1(), p.getOperators(),
                            p.getOp2(), p.getResult(), p.getTiempoMaximo(), p.getLote(),
                            p.getTiempoTranscurrido(), p.getTiempoFinalizacion(), p.getTiempoEspera(),
                            p.getTiempoServicio(), p.getTiempoRetorno(), p.getTiempoRespuesta(),
                            p.getTiempoLlegada(), p.getTiempoRestante(), p.getQuantum(),
                            p.getTiempoBloqueado(), p.getMemoria())
            );
            
            memoriaSuspendidos(p.getId());
            
            bloqueados.remove(0);
            
            
        }

    }

    public void mandarMemoria() {

        if (!suspendidos.isEmpty()) {

            Procesos p = (Procesos) suspendidos.get(0);

            if (comprobarEspacio(Integer.parseInt(p.getMemoria()))) {

                procesos.add(
                        new Procesos(p.getId(), p.getNombre(), p.getOp1(), p.getOperators(),
                                p.getOp2(), p.getResult(), p.getTiempoMaximo(), p.getLote(),
                                p.getTiempoTranscurrido(), p.getTiempoFinalizacion(), p.getTiempoEspera(),
                                p.getTiempoServicio(), p.getTiempoRetorno(), p.getTiempoRespuesta(),
                                p.getTiempoLlegada(), p.getTiempoRestante(), p.getQuantum(),
                                p.getTiempoBloqueado(), p.getMemoria())
                );
                
                agregarMemoria(Integer.parseInt(p.getId()),Integer.parseInt(p.getMemoria()));
                
                suspendidos.remove(0);

            }

        }

    }

    public void mostrarSuspendidos() {
        suspendidosT.setText("");

        for (int i = 0; i < suspendidos.size(); i++) {

            suspendidosT.append("Id: " + suspendidos.get(i).getId() + "     "
                    + "M: " + suspendidos.get(i).getMemoria() + "\n");

        }

    }
    
    public void mandarAnuevos(){
        String id = " ";
        System.out.println("No borrar"+idAux);
        
        for(int i=0; i<listaMemoria.size(); i++){
            
            if(!listaMemoria.get(i).getId().equals(idAux) &&
                    !(listaMemoria.get(i).getId().equals(" ") ||  listaMemoria.get(i).getId().equals(""))){
                id=listaMemoria.get(i).getId();
                memoriaTerminados(listaMemoria.get(i).getId());
                break;
            }
        }
        
        for(int j=0; j<procesos.size(); j++){
            
            if(procesos.get(j).getId().equals(id)){
                    nuevos(j);
                    break;
            }
        }
        
        
        
        
    }
    
    public void nuevos(int pos){
        
        Procesos p = (Procesos) procesos.get(pos);
        
        memoriaAux.add(new Procesos(
        p.getId(),p.getNombre(),p.getOp1(),p.getOperators(),p.getOp2(),p.getResult(),
                p.getTiempoMaximo(),p.getLote(),p.getTiempoTranscurrido(),p.getTiempoFinalizacion(),
                p.getTiempoEspera(),p.getTiempoServicio(),p.getTiempoRetorno(),
                p.getTiempoRespuesta(),p.getTiempoLlegada(),p.getTiempoRestante(),
                p.getQuantum(),p.getTiempoBloqueado(),p.getMemoria()
        ));
        
                
        
        if(listaAux.contains(p.getId())){
            listaAux.remove(p.getId());
        }
        
        procesos.remove(pos);
        eliminar(p.getId());
        
        guardarArchivo();
       // memoriaVirtual.remove(pos)

        mostrarProcesos();
        
        
        
        
    }
    
    public void eliminar(String idc){
        
        for(int i=0; i<memoriaVirtual.size(); i++){
            
            if(memoriaVirtual.get(i).getId().equals(idc)){
                memoriaVirtual.remove(i);
                break;
            }
            
        }
        
        
    }
    
    
    public void verificaSiEntraAux(){
        int memoria=0;
        for(int i=0; i<memoriaAux.size(); i++){
            memoria=Integer.parseInt(memoriaAux.get(i).getMemoria());
            
            if (comprobarEspacio(memoria)) {

               procesos.add(new Procesos(
                    memoriaAux.get(i).getId(),
                    memoriaAux.get(i).getNombre(),
                    memoriaAux.get(i).getOp1(),
                       memoriaAux.get(i).getOperators(),
                       memoriaAux.get(i).getOp2(),
                    memoriaAux.get(i).getResult(),
                      memoriaAux.get(i).getTiempoMaximo(), //operaciones
                    memoriaAux.get(i).getLote(),
                      memoriaAux.get(i).getTiempoTranscurrido(),
                    memoriaAux.get(i).getTiempoFinalizacion(), 
                       memoriaAux.get(i).getTiempoEspera(),
                       memoriaAux.get(i).getTiempoServicio(),
                       memoriaAux.get(i).getTiempoRetorno(),
                       memoriaAux.get(i).getTiempoRespuesta(),
                       memoriaAux.get(i).getTiempoLlegada(),
                       memoriaAux.get(i).getTiempoRestante(),
                    memoriaAux.get(i).getQuantum(),
                    memoriaAux.get(i).getTiempoBloqueado(), //tiempos
                    memoriaAux.get(i).getMemoria()
            ));

            agregarMemoria(Integer.parseInt(memoriaAux.get(i).getId()), memoria); // Agregar a memoria
            memoriaAux.remove(i);

        }
            
            
        }
        
        
      
    }
    
    public void verificaNuevo(){
       int memoria=0;
        for(int i=0; i<listos.size(); i++){
            memoria=Integer.parseInt(listos.get(i).getMemoria());
            
            if (comprobarEspacio(memoria)) {

               procesos.add(new Procesos(
                    listos.get(i).getId(),
                    listos.get(i).getNombre(),
                    listos.get(i).getOp1(),
                       listos.get(i).getOperators(),
                       listos.get(i).getOp2(),
                    listos.get(i).getResult(),
                      listos.get(i).getTiempoMaximo(), //operaciones
                    listos.get(i).getLote(),
                      listos.get(i).getTiempoTranscurrido(),
                    listos.get(i).getTiempoFinalizacion(), 
                       listos.get(i).getTiempoEspera(),
                       listos.get(i).getTiempoServicio(),
                       listos.get(i).getTiempoRetorno(),
                       listos.get(i).getTiempoRespuesta(),
                       listos.get(i).getTiempoLlegada(),
                       listos.get(i).getTiempoRestante(),
                    listos.get(i).getQuantum(),
                    listos.get(i).getTiempoBloqueado(), //tiempos
                    listos.get(i).getMemoria()
            ));

            agregarMemoria(Integer.parseInt(listos.get(i).getId()), memoria); // Agregar a memoria
            listos.remove(i);

        }
            
            
        }
    }
    

    public void proceso() throws InterruptedException {

        int tiempoRespuesta = 1;
        String op;
        int tiempo, tiempoBloqueados = 1;
        boolean iniciaContadorBloqueado = false;
        boolean round = false;

        while (numeroProcesos != 0) {
            ejecutar.setFocusable(true);
            //System.out.println("Numero de procesos " + numeroProcesos);
            
            
            while (!procesos.isEmpty()) {
                
                ejecutar.setFocusable(true);
                
                mostrarAux();
                verificaSiEntraAux();
                verificaNuevo();
                mostrarAux();

                Procesos p = (Procesos) procesos.get(0);

                idAux = p.getId();
                
                //
                if(!verificaSiEntra() && verificaSiEstaEnListaAux()){
                    
                    //aqui quitar break
                    mandarAnuevos();
                    verificaSiEntra();
                    break;
                    
                   
                }
                
                calcularTiempoRespuesta(tiempoRespuesta);

                mostrarMemoria();
                mostrarBloqueados();
                mostrarProcesos();
                mostrarNuevos();
                mostrarSuspendidos();

                memoriaEjecucion(idAux);
                mostrarMemoria(); //ver memoria 
                
                tProcesoEjecucion.setText(
                        "Operacion: " + p.getOp1() + " " + p.getOperators() + " " + p.getOp2() + "\n"
                        + "Tiempo maximo estimado: " + p.getTiempoMaximo() + "\n"
                        + "id: " + p.getId() + "\n"
                        + "Quantum: " + p.getQuantum() + "\n"
                        + "Memoria: " + p.getMemoria() + "\n"
                );

                p.setResult(operacion(0));
                op = p.getOp1() + " " + p.getOperators() + " " + p.getOp2();

                tiempo = Integer.parseInt(p.getTiempoRestante());

                for (int i = 0; i < tiempo; i++) {

                    tiempoRespuesta++;
                    ltiempoTranscurrido.setText(Integer.toString(i));
                    lTiempoRestante.setText(Integer.toString(tiempo - i));
                    lContadorGlobal.setText(Integer.toString(contadorGlobal));
                    mostrarProcesos();

                    p.setTiempoTranscurrido(Integer.toString(i));
                    p.setTiempoRestante(Integer.toString(tiempo - i));

                    Thread.sleep(500);

                    if (iniciaContadorBloqueado) {
                        tiempoBloqueados++;
                    }
                    if (tiempoBloqueados == 9 || tiempoBloqueados > 9) {

                        tiempoBloqueados = 1;
                        bloqueadoListo();
                        mostrarBloqueados();
                    }

                    lQuantum.setText(Integer.toString(i));

                    if (p.getQuantum().equals(Integer.toString(i))) {

                        lQuantum.setText(Integer.toString(i));
                        round = true;
                        carrusel();

                        mostrarMemoria();
                        mostrarProcesos();
                        break;

                    }

                    lContadorBloqueados.setText(Integer.toString(tiempoBloqueados));

                    /*
                     Bloque Key
                     */
                    if (keyP) {

                        synchronized (this) {
                            wait();
                        }
                        keyP = false;

                    }

                    if (keyS) {

                        mandarSuspendidos();
                        mostrarSuspendidos();
                        mostrarBloqueados();
                        mostrarMemoria();

                        keyS = false;
                    }

                    if (keyR) {

                        mandarMemoria();
                        mostrarBloqueados();
                        mostrarProcesos();
                        mostrarMemoria();

                        keyR = false;

                    }

                    if (keyM) {

                        synchronized (this) {
                            wait();
                        }
                        keyM = false;

                    }

                    if (keyT) {
                        //Tabla
                        meterDatosAlaTabla();
                        tablaDatos.setVisible(true);
                        synchronized (this) {
                            wait();
                        }
                        keyT = false;

                    }
                    if (keyE) {
                        p.setResult("E");
                        p.setTiempoTranscurrido(Integer.toString(i));
                        op = "X";
                        break;
                    }
                    if (keyI) {

                        if (bloqueados.isEmpty()) {
                            tiempoBloqueados = 1;
                        }

                        bloqueados.add(new Procesos(
                                p.getId(), p.getNombre(), p.getOp1(), p.getOperators(),
                                p.getOp2(), p.getResult(), p.getTiempoMaximo(), p.getLote(),
                                p.getTiempoTranscurrido(), p.getTiempoFinalizacion(),
                                p.getTiempoEspera(), p.getTiempoServicio(), p.getTiempoRetorno(),
                                p.getTiempoRespuesta(), p.getTiempoLlegada(),
                                p.getTiempoRestante(), p.getQuantum(), p.getTiempoBloqueado(), p.getMemoria()
                        ));

                        memoriaBloqueados(p.getId()); // Cambia el color de memoria

                        procesos.remove(0);

                        iniciaContadorBloqueado = true;
                        break;

                    }
                    if (keyN) {
                        registrarNuevo();
                        mostrarNuevos();
                        mostrarProcesos();
                        keyN = false;

                    }
                    
                    if(keyV){
                        
                        moverUltimaPagina();
                        mostrarMemoria();
                        guardarArchivo();
                                
                        keyV=false;
                        
                        
                    }
                    /*
                     Fin
                     */

                    contadorGlobal++;

                }

                if (round) {
                    round = false;
                    break;
                }

                if (keyI) {
                    keyI = false;
                    break;
                }

                if (keyE) {
                    keyE = false;
                    p.setTiempoServicio(p.getTiempoTranscurrido());

                } else {
                    p.setTiempoServicio(p.getTiempoMaximo());

                }

                p.setTiempoFinalizacion(Integer.toString(contadorGlobal));
                tiempoRetorno();//Calcular tiempo retorno
                tiempoEspera();

                /* tTerminados.append(
                 "id: " + p.getId() + "\t"
                 + "Op: " + op + "\t"
                 + "resultado: " + p.getResult() + "\t"
                 + "TLL: " + p.getTiempoLlegada() + "\t"
                 + "TF: " + p.getTiempoFinalizacion() + "\t"
                 + "TRetorno: " + p.getTiempoRetorno() + "\t"
                 + "TServicio: " + p.getTiempoServicio()+ "\t"
                 + "TEspera: " + p.getTiempoEspera() + "\t"
                 + "TRespuesta: " + p.getTiempoRespuesta() + "\n"
                 );*/
                int auxT = Integer.parseInt(p.getTiempoRespuesta());
                int auxT2 = Integer.parseInt(p.getTiempoEspera());

                if (auxT > auxT2) {

                    int tiempoAux;
                    int tiempoAuxServicio = Integer.parseInt(p.getTiempoServicio());
                    int respAux;
                    tiempoAux = Integer.parseInt(p.getTiempoRespuesta());

                    respAux = tiempoAux - tiempoAuxServicio;
                    respAux = (int) Math.random() * 2 + 1;

                    /*if(respAux<0){
                     respAux=tiempoAuxServicio-tiempoAux;
                     respAux=2;
                     }*/
                    p.setTiempoRespuesta(Integer.toString(respAux));

                }

                datosEnTablaTerminados(p.getId(), op, p.getResult(), p.getTiempoLlegada(),
                        p.getTiempoFinalizacion(), p.getTiempoRetorno(), p.getTiempoServicio(),
                        p.getTiempoEspera(), p.getTiempoRespuesta());

                memoriaTerminados(p.getId());
                mostrarMemoria();
                //Al finalizar
                listaAux.remove(p.getId());
                tProcesoEjecucion.setText("");
                terminados();

                numeroProcesos--;
                procesos.remove(0);
                aListos();
                procesosListos--;

                if (procesosListos < 0) {
                    procesosListos = 0;
                }

                lProcesosEspera.setText(Integer.toString(procesosListos));
            }

     //proceso nulo
            while (!bloqueados.isEmpty()) {

                if (!procesos.isEmpty()) {
                    break;
                }

                mostrarMemoria();
                mostrarBloqueados();
                tProcesoEjecucion.setText("");

                lContadorGlobal.setText(Integer.toString(contadorGlobal));
                contadorGlobal++;

                if (keyN) {
                    //Nuevos
                    registrarNuevo();
                    mostrarProcesos();
                    mostrarMemoria();
                    mostrarNuevos();
                    keyN = false;
                }

                if (keyS) {

                    mandarSuspendidos();
                    mostrarSuspendidos();
                    mostrarBloqueados();

                    keyS = false;
                }

                if (keyR) {

                    mandarMemoria();
                    mostrarMemoria();
                    mostrarBloqueados();
                    mostrarProcesos();

                    keyR = false;

                }

                if (keyT) {
                    //Tabla
                    meterDatosAlaTabla();
                    tablaDatos.setVisible(true);
                    synchronized (this) {
                        wait();
                    }
                    keyT = false;

                }
                if (keyM) {

                    synchronized (this) {
                        wait();
                    }
                    keyM = false;

                }

                Thread.sleep(500);
                lContadorBloqueados.setText(Integer.toString(tiempoBloqueados));

                if (tiempoBloqueados == 8 || tiempoBloqueados > 8) {
                    tiempoBloqueados = 1;
                    bloqueadoListo();
                    mostrarBloqueados();
                    break;

                }

                tiempoBloqueados++;

            }
            
            //Suspendidos
            
            while((!suspendidos.isEmpty() && bloqueados.isEmpty()) && procesos.isEmpty()){
                inicializarTabla();
                mostrarMemoria();
                Thread.sleep(500);
                lContadorGlobal.setText(Integer.toString(contadorGlobal));
                contadorGlobal++;
                System.out.println("Entra");
            if (keyR) {

                    mandarMemoria();
                    mostrarMemoria();
                    mostrarBloqueados();
                    mostrarProcesos();

                    keyR = false;
                    break;

                }
            if (keyN) {
                    //Nuevos
                    registrarNuevo();
                    mostrarProcesos();
                    mostrarMemoria();
                    mostrarNuevos();
                    keyN = false;
                }
            if (keyP) {

                        synchronized (this) {
                            wait();
                        }
                        keyP = false;

                    }
            if (keyT) {
                    //Tabla
                    meterDatosAlaTabla();
                    tablaDatos.setVisible(true);
                    synchronized (this) {
                        wait();
                    }
                    keyT = false;

                }
                if (keyM) {

                    synchronized (this) {
                        wait();
                    }
                    keyM = false;

                }

                
                
            }
            

        }
        limpiarTodo();

    }

    public void carrusel() {
        /*
         private String id;
         private String nombre;
         private String op1;
         private String operators;
         private String op2;
         private String result;
         private String tiempoMaximo;
         private String lote;
         private String tiempoTranscurrido;
         private String tiempoFinalizacion;
         private String tiempoEspera;
         private String tiempoServicio;
         private String tiempoRetorno;
         private String tiempoRespuesta;
         private String tiempoLlegada;
         private String tiempoRestante;
         private String quantum;
         private String tiempoBloqueado;
         */
        String id, nombre, op1, operadores, op2, resultado, tm, lot, tt, tf, te, ts, tr, tres, tll,
                trest, q, tb, mem;

        Procesos p = (Procesos) procesos.get(0);
        id = p.getId();
        nombre = p.getNombre();
        op1 = p.getOp1();
        operadores = p.getOperators();
        op2 = p.getOp2();
        resultado = p.getResult();
        tm = p.getTiempoMaximo();
        lot = "";
        tt = p.getTiempoTranscurrido();
        tf = p.getTiempoFinalizacion();
        te = p.getTiempoEspera();
        ts = p.getTiempoServicio();
        tr = p.getTiempoRetorno();
        tres = p.getTiempoRespuesta();
        tll = p.getTiempoLlegada();
        trest = p.getTiempoRestante();
        q = p.getQuantum();
        tb = "";
        mem = p.getMemoria();

        procesos.remove(0);

        procesos.add(new Procesos(
                id, nombre, op1, operadores, op2, resultado, tm, lot,
                tt, tf, te, ts, tr, tres, tll, trest, q, tb, mem
        ));

    }

    public void registrarNuevo() {
        numeroProcesos++;

        int op1, op2, op, tiempoMaximo, memoria;
        String[] operadores = {"+", "/", "*", "%", "+", "sqrt", "-"};
        op1 = (int) (Math.random() * 25 + 1);
        op = (int) (Math.random() * 2 + 0);
        op2 = (int) (Math.random() * 25 + 1);
        tiempoMaximo = (int) (Math.random() * 15 + 5);
        memoria = (int) (Math.random() * 10 + 7);

        if (comprobarEspacio(memoria)) {

            procesos.add(new Procesos(
                    Integer.toString(id),
                    "", Integer.toString(op1), operadores[op],
                    Integer.toString(op2), "", //operaciones
                    Integer.toString(tiempoMaximo), "",
                    "0", "", "", "0", "", "", Integer.toString(contadorGlobal),
                    Integer.toString(tiempoMaximo),
                    Integer.toString(quantum), "0" //tiempos
                    , Integer.toString(memoria)
            ));

            agregarMemoria(id, memoria); // Agregar a memoria

        } else {

            procesosListos += 1;
            lProcesosEspera.setText(Integer.toString(procesosListos));

            listos.add(new Procesos(
                    Integer.toString(id),
                    "", Integer.toString(op1), operadores[op],
                    Integer.toString(op2), "", //operaciones
                    Integer.toString(tiempoMaximo), "",
                    "0", "", "", "0", "", "", Integer.toString(contadorGlobal),
                    Integer.toString(tiempoMaximo),
                    Integer.toString(quantum), "0" //tiempos
                    , Integer.toString(memoria)
            ));

        }

        id++;

    }

    public void terminados() {
        Procesos p = (Procesos) procesos.get(0);
        terminados.add(new Procesos(
                p.getId(), p.getNombre(), p.getOp1(), p.getOperators(),
                p.getOp2(), p.getResult(), p.getTiempoMaximo(), p.getLote(),
                p.getTiempoTranscurrido(), p.getTiempoFinalizacion(),
                p.getTiempoEspera(), p.getTiempoServicio(), p.getTiempoRetorno(),
                p.getTiempoRespuesta(), p.getTiempoLlegada(),
                p.getTiempoRestante(), p.getQuantum(), p.getTiempoBloqueado(), p.getMemoria()
        ));
    }

    public void limpiarTodo() {
        lProcesosEspera.setText("0");
        lContadorBloqueados.setText("0");
        lContadorBloqueados.setText("0");
        lTiempoRestante.setText("0");
        ltiempoTranscurrido.setText("0");
        lQuantum.setText("0");

        tProcesosListos.setText("");
        tBloqueados.setText("");
        tProcesoEjecucion.setText("");
    //tTerminados.setText("");

        numeroProcesos = 0;
        contadorGlobal = 0;
        id = 0;
        procesosListos = 0;
        idAux = "";

        procesos.clear();
        listos.clear();
        bloqueados.clear();
        terminados.clear();
    }

    public void tiempoEspera() {
        int ts, tr, te;
        Procesos p = (Procesos) procesos.get(0);

        ts = Integer.parseInt(p.getTiempoServicio());
        tr = Integer.parseInt(p.getTiempoRetorno());

        te = (tr - ts);

        if (te <= 0) {
            p.setTiempoEspera("0");
        } else {
            p.setTiempoEspera(Integer.toString(te));
        }

    }

    public void tiempoRetorno() {
        int tf, tll, tr;
        Procesos p = (Procesos) procesos.get(0);

        tf = Integer.parseInt(p.getTiempoFinalizacion());
        tll = Integer.parseInt(p.getTiempoLlegada());

        tr = tf - tll;

        p.setTiempoRetorno(Integer.toString(tr));

    }

    public void mostrarBloqueados() {
        tBloqueados.setText("");
        for (int i = 0; i < bloqueados.size(); i++) {

            tBloqueados.append("ID: " + bloqueados.get(i).getId() + "    "
                    + "TR: " + bloqueados.get(i).getTiempoRestante() + "    "
                    + "TME: " + bloqueados.get(i).getTiempoMaximo() + "    "
                    + "Q:  " + bloqueados.get(i).getQuantum() + "  "
                    + "M:  " + bloqueados.get(i).getMemoria() + "\n"
            );
        }
    }

    public void mostrarNuevos() {

        nuevosT.setText("");

        for (int i = 0; i < listos.size(); i++) {

            nuevosT.append("ID: " + listos.get(i).getId() + "    "
                    + "TR: " + listos.get(i).getTiempoRestante() + "    "
                    + "TME: " + listos.get(i).getTiempoMaximo() + "    "
                    + "Q:  " + listos.get(i).getQuantum() + "  "
                    + "M:  " + listos.get(i).getMemoria() + "\n"
            );
        }

    }

    public void bloqueadoListo() {

        if (!bloqueados.isEmpty()) {
            Procesos p = (Procesos) bloqueados.get(0);

            procesos.add(new Procesos(
                    p.getId(), p.getNombre(), p.getOp1(), p.getOperators(),
                    p.getOp2(), p.getResult(), p.getTiempoMaximo(), p.getLote(),
                    p.getTiempoTranscurrido(), p.getTiempoFinalizacion(),
                    p.getTiempoEspera(), p.getTiempoServicio(), p.getTiempoRetorno(),
                    p.getTiempoRespuesta(), p.getTiempoLlegada(),
                    p.getTiempoRestante(), p.getQuantum(), p.getTiempoBloqueado(),
                    p.getMemoria()
            ));

            bloqueados.remove(0);

        }

    }

    public void aListos() {

        if (!listos.isEmpty()) {

            Procesos p = (Procesos) listos.get(0);

            if (comprobarEspacio(Integer.parseInt(p.getMemoria()))) {

                procesos.add(new Procesos(
                        p.getId(), p.getNombre(), p.getOp1(), p.getOperators(),
                        p.getOp2(), p.getResult(), p.getTiempoMaximo(), p.getLote(),
                        p.getTiempoTranscurrido(), p.getTiempoFinalizacion(),
                        p.getTiempoEspera(), p.getTiempoServicio(), p.getTiempoRetorno(),
                        p.getTiempoRespuesta(), Integer.toString(contadorGlobal),
                        p.getTiempoRestante(), p.getQuantum(), p.getTiempoBloqueado(),
                        p.getMemoria()
                ));

                agregarMemoria(Integer.parseInt(p.getId()), Integer.parseInt(p.getMemoria())); //Agrega a memoria 

                listos.remove(0);
            }

        }
    }

    public String operacion(int aux) {
        String signo = procesos.get(aux).getOperators();
        int op1 = Integer.parseInt(procesos.get(aux).getOp1());
        int op2 = Integer.parseInt(procesos.get(aux).getOp2());
        int res;

        if (signo.equals("+")) {
            res = op1 + op2;
        } else if (signo.equals("-")) {
            res = op1 - op2;

        } else if (signo.equals("*")) {
            res = op1 * op2;
        } else if (signo.equals("/")) {
            res = op1 / op2;
        } else if (signo.equals("%")) {
            res = op1 % op2;
        } else if (signo.equals("potencia")) {
            res = (int) Math.pow(op1, op2);
        } else if (signo.equals("sqrt")) {
            res = (int) Math.sqrt(op2);
        } else {
            res = 0;
        }
        return Integer.toString(res);

    }

    public void calcularTiempoRespuesta(int tiempoRespuesta) {
        Procesos p = (Procesos) procesos.get(0);
        if (p.getTiempoRespuesta().equals("")) {
            if (p.getId().equals("0")) {
                p.setTiempoRespuesta("0");
            } else {
                int TLL = Integer.parseInt(p.getTiempoLlegada());
                int res = tiempoRespuesta - TLL;
                //negativos
                if (res < 0) {
                    res = TLL - tiempoRespuesta;

                }
                p.setTiempoRespuesta(Integer.toString(res));
            }
        }

    }

    public void mostrarProcesos() {
        tProcesosListos.setText("");
        for (int i = 0; i < procesos.size(); i++) {

            if (!(idAux.equals(procesos.get(i).getId()))) {

                memoriaListos(procesos.get(i).getId());

                tProcesosListos.append("ID: " + procesos.get(i).getId() + "   "
                        + "TR: " + procesos.get(i).getTiempoRestante() + "   "
                        + "TME: " + procesos.get(i).getTiempoMaximo() + "   "
                        + "Q: " + procesos.get(i).getQuantum() + "   "
                        + "M: " + procesos.get(i).getMemoria() + "\n");

            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Interfaz().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea auxTxt;
    private javax.swing.JButton ejecutar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JLabel lContadorBloqueados;
    private javax.swing.JLabel lContadorGlobal;
    private javax.swing.JLabel lProcesosEspera;
    private javax.swing.JLabel lQuantum;
    private javax.swing.JLabel lTiempoRestante;
    private javax.swing.JLabel ltiempoTranscurrido;
    private javax.swing.JTable memoria;
    private javax.swing.JTextArea nuevosT;
    private javax.swing.JButton registrar;
    private javax.swing.JTextArea suspendidosT;
    private javax.swing.JTextArea tBloqueados;
    private javax.swing.JTextField tNumeroProcesos;
    private javax.swing.JTextArea tProcesoEjecucion;
    private javax.swing.JTextArea tProcesosListos;
    private javax.swing.JTextField tQuantum;
    private javax.swing.JTable tablaDatos;
    private javax.swing.JTable terminadosT;
    // End of variables declaration//GEN-END:variables
}
