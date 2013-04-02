package pl.amebakorp.tlumaczschowka;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import pl.amebakorp.tlumaczschowka.schowek.SluchaczSchowka;
import pl.amebakorp.tlumaczschowka.trayklient.SystemTrayKlient;
import javax.swing.JOptionPane;
import pl.amebakorp.tlumaczschowka.translator.translatormicrosoftu.TranslatorMicrosoftu;

/**
 * Hello world!
 *
 */
public class TlumaczSchowka {

    private final SluchaczSchowka sluchaczSchowka;
    private final SystemTrayKlient sysTrayKlient;

    public TlumaczSchowka() {
        sysTrayKlient = new SystemTrayKlient();
        sluchaczSchowka = new SluchaczSchowka();
    }

    public void uruchom() {
        try {
            Konfiguracja konfiguracja = new Konfiguracja();
            konfiguracja.wczytajZPliku();
            
            Tlumacz translator = podajTranslator(konfiguracja);
            
            dodajObslugeZdarzeniaZnalezionoNapis(translator);
            dodajObslugeZdarzeniaKliknietoWyjdz();
            dodajObslugeZdarzeniaKliknietoCzySzukac();
            
            sluchaczSchowka.obslugujZdarzenia();
            
        } catch (Konfiguracja.WyjatekKonfiguracja ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            System.exit(-1);
        }
    }

    private Tlumacz podajTranslator(Konfiguracja konfiguracja) {
        String clientId = konfiguracja.podajWartosc("clientId");
        String clientSecret = konfiguracja.podajWartosc("clientSecret");
        return new TranslatorMicrosoftu(clientId, clientSecret);
    }

    private void obsluzeTlumaczenieNapisu(Tlumacz tlumacz, String napis) {
        try {
            sysTrayKlient.ustawIkoneLadowania();
            String przetlumaczony = tlumacz.tlumacz(napis);
            sysTrayKlient.ustawIkoneNormalna();
            sysTrayKlient.wyswietlKomunikat(napis, przetlumaczony);
        } catch (Exception ex) {
            sysTrayKlient.ustawIkoneNormalna();
            sysTrayKlient.wyswietlBlad("Wyjatek", ex.getLocalizedMessage());
        }
    }

    private void dodajObslugeZdarzeniaZnalezionoNapis(final Tlumacz tlumacz) {
        sluchaczSchowka.dodajZnalezionoNapisSluchacz(new ZnalezionoNapisSluchacz() {
            public void znalezionoNapis(String napis) {
                obsluzeTlumaczenieNapisu(tlumacz, napis);
            }
        });
    }

    private void dodajObslugeZdarzeniaKliknietoWyjdz() {
        sysTrayKlient.dodajZdarzenieWyjdz(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                obsluzeWyjscie();
            }
        });
    }

    private void obsluzeWyjscie() {
        System.exit(0);
    }

    private void obsluzPozwolenieSzukania() {
        sluchaczSchowka.obslugujZdarzenia();
        sysTrayKlient.ustawIkoneNormalna();
    }

    private void obsluzZakazSzukania() {
        sluchaczSchowka.przestanObslugiwacZdarzenia();
        sysTrayKlient.ustawIkoneBlock();
    }

    private void dodajObslugeZdarzeniaKliknietoCzySzukac() {
        sysTrayKlient.dodajZdarzenieCzySzukac(new ItemListener() {
            public void itemStateChanged(ItemEvent ie) {
                if (ie.getStateChange() == ItemEvent.SELECTED) {
                    obsluzPozwolenieSzukania();
                } else {
                    obsluzZakazSzukania();
                }
            }
        });
    }
}
