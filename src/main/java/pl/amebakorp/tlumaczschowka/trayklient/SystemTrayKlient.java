/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.amebakorp.tlumaczschowka.trayklient;

import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import javax.swing.ImageIcon;

/**
 *
 * @author bidek
 */
public class SystemTrayKlient {

    private final TrayIcon trayIkona;
    private final String ikonaNormalnaSciezka = "ikony/normalna.gif";
    private final String ikonaLadowaniaSciezka = "ikony/loading.gif";
    private final String ikonaBlockSciezka = "ikony/block.png";
    private final ImageIcon normalnaIkona;
    private final ImageIcon ladowanieIkona;
    private final ImageIcon blockIkona;
    private final CheckboxMenuItem czySzukacCheckbox;
    private final MenuItem wyjdzMenuItem;

    private void dodajeIkoneDoZasobnika() {
        try {
            SystemTray.getSystemTray().add(trayIkona);
        } catch (AWTException ex) {
            throw new SystemTrayException("Nie udało sie zainicjowac klienta zasobnika systemowego", ex);
        }
    }

    private void przygotujMenu() {
        PopupMenu popMenu = new PopupMenu();
        popMenu.add(czySzukacCheckbox);
        popMenu.add(wyjdzMenuItem);
        trayIkona.setPopupMenu(popMenu);

    }

    class SystemTrayException extends RuntimeException {

        public SystemTrayException(String string, Throwable thrwbl) {
            super(string, thrwbl);
        }
    }

    public SystemTrayKlient() {
        this.ladowanieIkona = new ImageIcon(getClass().getClassLoader().getResource(ikonaLadowaniaSciezka));
        this.normalnaIkona = new ImageIcon(getClass().getClassLoader().getResource(ikonaNormalnaSciezka));
        this.blockIkona = new ImageIcon(getClass().getClassLoader().getResource(ikonaBlockSciezka));
        this.trayIkona = new TrayIcon(normalnaIkona.getImage());
        this.czySzukacCheckbox = new CheckboxMenuItem("Wyszukiwać?");
        this.czySzukacCheckbox.setState(true);
        this.wyjdzMenuItem = new MenuItem("Wyjdź");
        przygotujMenu();
        dodajeIkoneDoZasobnika();
    }

    public void wyswietlKomunikat(String tytul, String komunikat) {
        trayIkona.displayMessage(tytul, komunikat, TrayIcon.MessageType.NONE);
    }

    public void wyswietlBlad(String tytul, String komunikat) {
        trayIkona.displayMessage(tytul, komunikat, TrayIcon.MessageType.ERROR);
    }

    public void ustawIkoneLadowania() {
        trayIkona.setImage(ladowanieIkona.getImage());
    }

    public void ustawIkoneNormalna() {
        trayIkona.setImage(normalnaIkona.getImage());
    }

    public void ustawIkoneBlock() {
        trayIkona.setImage(blockIkona.getImage());
    }

    public void dodajZdarzenieWyjdz(ActionListener lister) {
        wyjdzMenuItem.addActionListener(lister);
    }

    public void dodajZdarzenieCzySzukac(ItemListener listener) {
        czySzukacCheckbox.addItemListener(listener);
    }
}
