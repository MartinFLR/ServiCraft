package servicraft.controller;

import servicraft.model.DockerMinecraft;
import servicraft.vista.ServiCraft;

public class Main {

	public static void main(String[] args) {
		DockerMinecraft modelo = new DockerMinecraft();
		ServiCraft vista = new ServiCraft();
		Controller controlador = new Controller(vista, modelo);
		vista.setVisible(true);

	}

}
