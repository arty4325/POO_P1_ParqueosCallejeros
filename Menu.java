import java.util.Scanner;

public class Menu {
    public Parqueo parqueo;
    private Scanner scanner;

    public Menu() {
        parqueo = new Parqueo();
        scanner = new Scanner(System.in);
    }



    public void mostrarMenu() {
        int opcion = 0;
        do {
            try {
                System.out.println("Menú");
                System.out.println("1. Ingresar Vehículo");
                System.out.println("2. Consultar Vehículo");
                System.out.println("3. Salida de Vehículo");
                System.out.println("4. Consultar parqueo");
                System.out.println("7. Salir");
                System.out.println("Ingrese una opción:");
                opcion = Integer.parseInt(scanner.nextLine());

                String placa;
                Vehiculo vehiculo;


                switch (opcion) {
                    case 1:
                        menuIngreso();
                        break;
                    case 2:
                        System.out.println("Ingrese la placa del vehículo: ");
                        placa = scanner.nextLine();
                        vehiculo = parqueo.buscarVehiculoPorPlaca(placa);
                        if (vehiculo != null) {
                            System.out.println("Vehículo encontrado");
                            System.out.println(vehiculo.toString());
                        } else {
                            System.out.println("Vehículo no encontrado");
                        }
                        System.out.println();
                        break;
                    case 3:
                        System.out.println("Ingrese la placa del vehículo: ");
                        placa = scanner.nextLine();
                        vehiculo = parqueo.eliminarVehiculo(placa);
                        if (vehiculo != null) {
                            System.out.println("Vehículo eliminado");
                        } else {
                            System.out.println("Vehículo no encontrado");
                        }
                        break;
                    case 4:
                        parqueo.Colsulta();
                        break;
                    case 7:
                        System.out.println("Saliendo...");
                        break;
                    default:
                        System.out.println("Opción inválida");
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número entero.");
            }
        } while (opcion != 7);
    }

    public void menuIngreso (){
        System.out.println("");
        System.out.println("*** Ingreso de vehículo ***");
        System.out.println("");
        String placa;
        do {
            System.out.println("Ingrese la placa del vehículo: ");
            placa = scanner.nextLine();
            if (parqueo.placaYaRegistrada(placa)) {
                System.out.println("La placa " + placa + " ya está registrada en el parqueo.");
            }
        } while (parqueo.placaYaRegistrada(placa));


        //ingreso del vehiculo mediante el paruqeo
        Vehiculo vehiculoIngreso = parqueo.ingresoDeVehiculo(placa);

        if (vehiculoIngreso != null){
            System.out.println("Vehiculo placa " + vehiculoIngreso.getPlaca() + " ingresado con éxito");
        }
        else
            System.out.println("No hay espacio disponible");
    }

}
