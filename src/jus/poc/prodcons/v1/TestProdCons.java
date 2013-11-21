package jus.poc.prodcons.v1;

import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;
import java.util.Properties;

import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Simulateur;

public class TestProdCons extends Simulateur {
	
	public int nbProd;
	public int nbCOns;
	public int nbBuffer;
	public int tempsMoyenProduction;
	public int deviationTempsMoyenProduction;
	public int tempsMoyenConsommation;
	public int deviationTempsMoyenConsommation;
	public int nombreMoyenDeProduction;
	public int deviationNombreMoyenDeProduction;
	public int nombreMoyenNbExemplaire;
	public int deviationNombreMoyenNbExemplaire;
	
	
	public TestProdCons(Observateur observateur) {
		super(observateur);
	}

	@Override
	protected void run() throws Exception {
		init("jus/poc/prodcons/options/option1.xml");

	}
	
	/**
	* Retreave the parameters of the application.
	* @param file the final name of the file containing the options.
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws IOException 
	 * @throws InvalidPropertiesFormatException 
	*/
	protected void init(String file) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InvalidPropertiesFormatException, IOException {
		Properties properties = new Properties();
		properties.loadFromXML(ClassLoader.getSystemResourceAsStream(file));
		String key;
		int value;
		Class<?> thisOne = getClass();
		for(Map.Entry<Object,Object> entry : properties.entrySet()) {
			key = (String)entry.getKey();
			value = Integer.parseInt((String)entry.getValue());
			thisOne.getDeclaredField(key).set(this,value);
		}
	}

	

	public static void main(String[] args) {
		new TestProdCons(new Observateur()).start();

	}

}
