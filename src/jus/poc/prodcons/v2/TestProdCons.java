package jus.poc.prodcons.v2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;
import java.util.Properties;

import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Simulateur;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

public class TestProdCons extends Simulateur {
	
	public int nbProd;
	public int nbCons;
	public int nbBuffer;
	public int tempsMoyenProduction;
	public int deviationTempsMoyenProduction;
	public int tempsMoyenConsommation;
	public int deviationTempsMoyenConsommation;
	public int nombreMoyenDeProduction;
	public int deviationNombreMoyenDeProduction;
	public int nombreMoyenNbExemplaire;
	public int deviationNombreMoyenNbExemplaire;
    private HashMap<Integer, _Consommateur> consommateurs = new HashMap();
    private HashMap<Integer, _Producteur> producteurs = new HashMap();
	
	
	public TestProdCons(Observateur observateur) {
		super(observateur);
	}

	@Override
	protected void run() throws Exception {
		this.init("src/jus/poc/prodcons/options/options1.xml");
		Tampon t = new ProdCons(nbBuffer);
		int i=0;
		Aleatoire aleaCons = new TirageAlea(tempsMoyenConsommation,deviationTempsMoyenConsommation);
		Aleatoire aleaTempsProd = new TirageAlea(tempsMoyenProduction, deviationTempsMoyenProduction);
		Aleatoire aleaNbreAProduire = new TirageAlea(nombreMoyenDeProduction, deviationNombreMoyenDeProduction);
		
		for(i=0;i<nbCons;i++)
		{
			Consommateur c = new Consommateur(observateur, tempsMoyenConsommation, deviationTempsMoyenConsommation, t, aleaCons);
			consommateurs.put(c.identification(), c);
			c.setDaemon(true);
			c.start();
		}
		
		for(i=0;i<nbProd;i++)
		{
			Producteur p = new Producteur(observateur, tempsMoyenProduction, deviationTempsMoyenProduction, aleaNbreAProduire.next(), t, aleaTempsProd);
			producteurs.put(p.identification(), p);
			p.start();
		}
		
		

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
		//properties.loadFromXML(ClassLoader.getSystemResourceAsStream(file));
		properties.loadFromXML(new FileInputStream(file));
		String key;
		int value;
		Class<?> thisOne = getClass();
		for(Map.Entry<Object,Object> entry : properties.entrySet()) {
			key = (String)entry.getKey();
			value = Integer.parseInt((String)entry.getValue());
			System.out.println("key " + key+ " valeurs : "+value);
			thisOne.getDeclaredField(key).set(this,value);
		}
	}


	public static void main(String[] args) throws InvalidPropertiesFormatException, IOException {
		new TestProdCons(new Observateur()).start();

	}

}
