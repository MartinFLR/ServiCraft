package servicraft.controller;

import servicraft.model.DockerMinecraft;
import servicraft.vista.ServicraftVista;

public class Main {

	public static void main(String[] args) {
		DockerMinecraft modelo = new DockerMinecraft();
		ServicraftVista vista = new ServicraftVista();
		Controller controlador = new Controller(vista, modelo);
		vista.setVisible(true);

	}

}
