import java.util.ArrayList;

public class Parqueo {
    private Espacio[] parqueoVehiculos;
    private Movimiento movimientos[];
    private ArrayList<Vehiculo> historial;
    int VehiculosIngresados=0;
    ArrayList<Vehiculo> cierre = new ArrayList<>();

    public Parqueo() {
        parqueoVehiculos = new Espacio[25];
        movimientos = new Movimiento[200];
        historial = new ArrayList<>(); // HISTORIAL DE INGRESOS
        for (int i = 0; i < parqueoVehiculos.length; i++) {
            parqueoVehiculos[i] = new Espacio();
        }
    }

    @Override
    public String toString() {
        String strValue = "***** PARQUEO *****\n";
        for (int i = 0; i < parqueoVehiculos.length; i++) {
            strValue += (i+1) + ". " + parqueoVehiculos[i].toString() + "\n";
        }
        return strValue;
    }

    private int buscarEspacioLibre() {

        for (int i = 0; i < 200; i++) {
            if (!this.parqueoVehiculos[i].isOcupado()) {
                return i;
            }
        }


        return -1;
    }

    public Vehiculo ingresoDeVehiculo(String placa) {
        int espacioLibre = buscarEspacioLibre();
        if (espacioLibre == -1) {
            return null;
        }
        Vehiculo vehiculo = new Vehiculo(placa);
        historial.add(vehiculo);
        this.parqueoVehiculos[espacioLibre].setOcupado(true);
        this.parqueoVehiculos[espacioLibre].setVehiculo(vehiculo);
        return vehiculo;
    }

    public boolean placaYaRegistrada(String placa) {
        for (Espacio espacio : this.parqueoVehiculos) {
            if (espacio.isOcupado() && espacio.getVehiculo().getPlaca().equals(placa)) {
                return true;
            }
        }
        return false;
    }
    public Vehiculo buscarVehiculoPorPlaca(String placa) {
        for (Espacio espacio : this.parqueoVehiculos) {
            if (espacio.isOcupado() && espacio.getVehiculo().getPlaca().equals(placa)) {
                return espacio.getVehiculo();
            }
        }
        return null;
    }

    public Vehiculo eliminarVehiculo(String placa) {
        ArrayList<String> placas = new ArrayList<>();
        Vehiculo saliendo = null;
        for (Espacio espacio : this.parqueoVehiculos) {
            if (espacio.isOcupado() && espacio.getVehiculo().getPlaca().equals(placa)) {
                Vehiculo vehiculo = espacio.getVehiculo();
                if (!placas.contains(placa)){
                    saliendo=vehiculo;
                    cierre.add(vehiculo);
                    placas.add(placa);
                    VehiculosIngresados++;
                }
                espacio.setVehiculo(null);
                espacio.setOcupado(false);

            }
        }
        if (saliendo!=null){
            return saliendo;
        }
        else {
            return null;
        }
    }



    public int VehiculosIngresados(){
        return VehiculosIngresados;
    }
    public ArrayList<Vehiculo> Colsulta() {
        ArrayList<String> placas = new ArrayList<>();
        int cont=1;
        for (Espacio espacio : this.parqueoVehiculos) {
            if (espacio.isOcupado()) {
                Vehiculo vehiculo = espacio.getVehiculo();
                String placa = vehiculo.getPlaca();
                if (!placas.contains(placa)){
                    System.out.println("Parqueo "+ cont + ": " + vehiculo);
                    placas.add(placa);}
                else{
                    System.out.println("Parqueo "+ cont + ": " +"Ocupado");
                }

            }
            else{
                System.out.println("Parqueo "+ cont + ": " +"Parqueo Libre");
            }
            cont++;
        }

        return null;
    }
}