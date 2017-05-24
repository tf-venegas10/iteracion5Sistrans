
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class Crear {



	public static void main(String[] args) {

		crearEspectaculo();
		
	}


	public static void crearUsuarios(){
		String csvFile = "./data/usuarios.csv";
		FileWriter writer;
		//String csvFile2 = "./data/clientes.csv";
		//FileWriter writer2;

		try {
			writer = new FileWriter(csvFile);
			//writer2 = new FileWriter(csvFile2);

			for (int i = 25; i<1000010; i++){
				//int randomNum = ThreadLocalRandom.current().nextInt(1, 999999);
				int rol_user = -1;
				if (i<30){
					rol_user=3;
				}
				if (i<50){
					rol_user=4;
				}
				else{
					rol_user=2;
				}
				Escritor.writeLine(writer, Arrays.asList(""+i,"Usuario"+i, "Usuario"+i+"@correoprueba.com", ""+rol_user));
			}
			writer.flush();
			writer.close();
			//writer2.flush();
			//writer2.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void crearRequerimientos()
	{
		String csvFile = "./data/req.csv";
		FileWriter writer;
		try {
			writer = new FileWriter(csvFile);
			for(int i = 21; i<1000010;i++ )
			{
				String descripcion = getSaltString();
				Escritor.writeLine(writer, Arrays.asList(""+i,descripcion));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	protected static String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
	
	public static void crearSitio()
	{
		String csvFile = "./data/sitios.csv";
		FileWriter writer;
		try {
			writer = new FileWriter(csvFile);
			char[] chars = "AC".toCharArray();
			String[] strings = {"Nocturno", "Diurno"};
			char[] chars2 = "SN".toCharArray();
			Random random = new Random();
			for (int i = 22; i<1000010;i++ )
			{
				char tipo = chars[random.nextInt(chars.length)];
				int capacidad = ThreadLocalRandom.current().nextInt(50, 300);
				String horario = strings[random.nextInt(strings.length)];
				char prote = chars2[random.nextInt(chars2.length)];
				Escritor.writeLine(writer, Arrays.asList(""+i, tipo+"", capacidad+"", horario, prote+""));
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void crearSillas(){
		String csvFile = "./data/sillas.csv";
		FileWriter writer;
		try {
			writer = new FileWriter(csvFile);

			int id =20;
			for (int i = 20; i<1020; i++){
				for (int j=20; j<1020; j++){
					int localidad = ThreadLocalRandom.current().nextInt(1, 5);
					Escritor.writeLine(writer, Arrays.asList(id+"", "1", ""+localidad, i+"",j+""));
					id++;
				}
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public static void crearBoletas(){
		String csvFile = "./data/boletas.csv";
		FileWriter writer;
		String csvFile2 = "./data/boletaDetalle.csv";
		FileWriter writer2;
		String csvFile3 = "./data/boletaSilla.csv";
		FileWriter writer3;
		try {
			writer = new FileWriter(csvFile);
			writer2 = new FileWriter(csvFile2);
			writer3 = new FileWriter(csvFile3);

			for (int idBoleta = 15, idSilla=20;idBoleta<1000000; idBoleta++, idSilla++){

				int asist = ThreadLocalRandom.current().nextInt(0, 10);
				String asistencia = "Y";
				if (asist<=3)asistencia="N";

				int canc = ThreadLocalRandom.current().nextInt(0, 10);
				String cancelada = "N";
				if (canc<=3 && asistencia.equals("N"))cancelada="Y";

				Escritor.writeLine(writer, Arrays.asList(idBoleta+"","2","1",asistencia, cancelada, "N"));

				int idCliente = ThreadLocalRandom.current().nextInt(40, 999999);

				Escritor.writeLine(writer2, Arrays.asList(idBoleta+"",idCliente+"", "Y"));
				Escritor.writeLine(writer3, Arrays.asList(idBoleta+"","1", idSilla+""));
			}

			writer.flush();
			writer.close();
			writer2.flush();
			writer2.close();
			writer3.flush();
			writer3.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void crearEspectaculo(){

		String csvFile = "./data/espectaculo.csv";
		FileWriter writer;
		String csvFile2 = "./data/espectaculo_categoria.csv";
		FileWriter writer2;
		try{
			writer = new FileWriter(csvFile);
			writer2 = new FileWriter(csvFile2);
			for(int i = 4; i < 256; i++ ){
				int idOrganizador ;
				String otroIdioma = "";
				String idioma = "";
				int categoria = ThreadLocalRandom.current().nextInt(1, 9);
				int costoRealizacion = ThreadLocalRandom.current().nextInt(1000000, 100000000);
				int duration = ThreadLocalRandom.current().nextInt(30, 170);
				idOrganizador=ThreadLocalRandom.current().nextInt(10, 31);
				if(i > 4 && i < 123 ){
					otroIdioma = "t";
					idioma = "ingles";
				}
				else{
					otroIdioma = "f";
					idioma = "espa�ol";
				}
				Escritor.writeLine(writer, Arrays.asList((i+1)+"",
						"espectaculo"+(i+1), duration+"",idioma,"descripcion"+(i+1), costoRealizacion+"",
						otroIdioma,idOrganizador+""));
				Escritor.writeLine(writer2, Arrays.asList((i+1)+"",categoria+""));
			}
			writer.flush();
			writer.close();
			writer2.flush();
			writer2.close();

		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static void crearCompanias(){
		String csvFile = "./data/companias.csv";
		String csvFile2 = "./data/compania_organizador.csv";
		String csvFile3 = "./data/compania_espectaculo.csv";
		String csvFile4 = "./data/compania_estadia.csv";
		FileWriter writer;
		FileWriter writer2;
		FileWriter writer3;
		FileWriter writer4;
		try{
			writer= new FileWriter(csvFile);
			writer2 = new FileWriter(csvFile2);
			writer3 = new FileWriter(csvFile3);
			writer4 = new FileWriter(csvFile4);
			for(int i = 4 ; i<100; i++){
				String ciudad = "";
				String pais = "";
				int organizador = ThreadLocalRandom.current().nextInt(1, 40);
				int espectaculo = ThreadLocalRandom.current().nextInt(5, 257);
				String fechaInicio = "10/12/2017";
				String fechaFin = "";
				if(i > 3 && i < 25){
					ciudad = "Caracas";
					pais = "Venezuela";
					fechaFin = "10/25/2017";
				}	
				else if(i>25 && i > 50){
					ciudad = "Cali";
					pais = "Colombia";
					fechaFin = "10/27/2017";
				}
				else{
					ciudad = "Bogota";
					pais = "Colombia";
					fechaFin = "10/29/2017";
				}
				Escritor.writeLine(writer, Arrays.asList((i+1)+"","Compania"+(i+1),pais,"carlos"+(i+1),"www.compania"+(i+1)+".com",ciudad));
				Escritor.writeLine(writer2, Arrays.asList(organizador+"",(i+1)+""));
				Escritor.writeLine(writer3,  Arrays.asList((i+1)+"",espectaculo+""));
				Escritor.writeLine(writer4, Arrays.asList((i+1)+"",fechaInicio,fechaFin));

			}
			writer.flush();
			writer.close();
			writer2.flush();
			writer2.close();
			writer3.flush();
			writer3.close();
			writer4.flush();
			writer4.close();

		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public static void crearPreferencias(){
		String csvFile = "./data/preferencias2.csv";
		FileWriter writer;
		try{
			writer = new FileWriter(csvFile);
			for(int i = 40; i<1000010; i++){
				String categoria = "";
				if(i > 39 && i< 125000 )
					categoria = "1";
				else if(i > 125000 && i< 250000 )
					categoria="2";
				else if(i > 250000 && i< 375000 )
					categoria = "3";
				else if(i > 375000 && i< 500000 )
					categoria = "4";
				else if(i > 500000 && i< 625000 )
					categoria = "5";
				else if (i > 625000	&& i< 750000 )
					categoria = "6";
				else if (i > 750000 && i< 875000 )
					categoria = "7";
				else 
					categoria = "8";

				Escritor.writeLine(writer, Arrays.asList(i+"",categoria));
			}
			writer.flush();
			writer.close();

		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public static void crearFunciones(){
		String csvFile = "./data/funciones.csv";
		FileWriter writer;
			try{
			writer = new FileWriter(csvFile);
			for (int i=5; i<256; i++){
				for (int j=1;j<5;j++){
					int sitio = ThreadLocalRandom.current().nextInt(1, 40);
					Date fecha = new Date();
					long sumafecha = ThreadLocalRandom.current().nextLong(10000000, 999999999);
					long nfecha = fecha.getTime()+sumafecha;
					int hora = ThreadLocalRandom.current().nextInt(7, 21)*100;
					fecha.setTime(nfecha);
					SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yy");
					Escritor.writeLine(writer, Arrays.asList(j+"",i+"",sitio+"", dt.format(fecha),hora+"","f"));
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
