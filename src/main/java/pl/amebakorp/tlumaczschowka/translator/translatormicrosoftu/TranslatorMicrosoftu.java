/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.amebakorp.tlumaczschowka.translator.translatormicrosoftu;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;
import pl.amebakorp.tlumaczschowka.Tlumacz;

/**
 *
 * @author bidek
 */
public class TranslatorMicrosoftu implements Tlumacz {

    private final String clientId;
    private final String clientSecret;

    public TranslatorMicrosoftu(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        Translate.setClientId(clientId);
        Translate.setClientSecret(clientSecret);
    }

    public String tlumacz(String napis) {
        try {
            return Translate.execute(napis, Language.ENGLISH, Language.POLISH);
        } catch (Exception ex) {
            throw new WyjatekTlumaczaMicrosoftu("Błąd translatora microsofotu", ex);
        }
    }

    public boolean czyDaSiePobracToken() {
        try {
            Translate.getToken(clientId, clientSecret);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
