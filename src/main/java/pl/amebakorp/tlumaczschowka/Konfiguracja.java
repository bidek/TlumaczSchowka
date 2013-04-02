package pl.amebakorp.tlumaczschowka;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author bidek
 */
public class Konfiguracja {

    public class WyjatekKonfiguracja extends RuntimeException {

        public WyjatekKonfiguracja(String string, Throwable thrwbl) {
            super(string, thrwbl);
        }
    }
    private Properties properties;

    public Konfiguracja() {
        this.properties = new Properties();
    }

    public String podajWartosc(String klucz) {
        return this.properties.getProperty(klucz);
    }

    public void wczytajZPliku() {
        try {
            properties.load(new FileInputStream("config.properties"));
        } catch (FileNotFoundException ex) {
            throw new WyjatekKonfiguracja("Nie znaleziono pliku konfiguracyjnego", ex);
        } catch (IOException ex) {
            throw new WyjatekKonfiguracja("Bład wczytywania pliku konfiguracyjneg", ex);
        }

    }
}
