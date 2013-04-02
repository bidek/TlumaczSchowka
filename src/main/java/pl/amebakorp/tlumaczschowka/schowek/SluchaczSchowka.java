/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.amebakorp.tlumaczschowka.schowek;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.FlavorListener;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import pl.amebakorp.tlumaczschowka.ZnalezionoNapisSluchacz;

/**
 *
 * @author bidek
 */
public class SluchaczSchowka implements FlavorListener {

    private Clipboard clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
    private List<ZnalezionoNapisSluchacz> sluchacze;
    private boolean czyObslugiwac;

    public SluchaczSchowka() {
        this.sluchacze = new ArrayList();
        this.czyObslugiwac = false;
        clipBoard.addFlavorListener(this);
    }

    public void flavorsChanged(FlavorEvent fe) {
        try {
            if (czyObslugiwac) {
                obsluzZmiane();
            }
        } catch (IllegalStateException ex) {
            Logger.getLogger(SluchaczSchowka.class.getName()).throwing("SluchaczSchowka", "flavorsChanged", ex);
        } catch (UnsupportedFlavorException ex) {
            Logger.getLogger(SluchaczSchowka.class.getName()).throwing("SluchaczSchowka", "flavorsChanged", ex);
        } catch (IOException ex) {
            Logger.getLogger(SluchaczSchowka.class.getName()).throwing("SluchaczSchowka", "flavorsChanged", ex);
        }
    }

    private synchronized void obsluzZmiane() throws UnsupportedFlavorException, IOException {
        String string = (String) clipBoard.getData(DataFlavor.stringFlavor);
        if (string != null && clipBoard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
            Transferable transferable = clipBoard.getContents(null);
            String napis = (String) transferable.getTransferData(DataFlavor.stringFlavor);
            clipBoard.setContents(transferable, null);

            znalezionoNapis(napis);
        }

    }

    private void znalezionoNapis(String napis) {
        for (ZnalezionoNapisSluchacz znalezionoNapisSluchacz : sluchacze) {
            znalezionoNapisSluchacz.znalezionoNapis(napis);
        }
    }

    public void dodajZnalezionoNapisSluchacz(ZnalezionoNapisSluchacz znalezionoNapisSluchacz) {
        sluchacze.add(znalezionoNapisSluchacz);
    }

    public boolean czyObslugiwac() {
        return this.czyObslugiwac;
    }

    public void obslugujZdarzenia() {
        this.czyObslugiwac = true;
    }

    public void przestanObslugiwacZdarzenia() {
        this.czyObslugiwac = false;
    }
}
