package terminplaner;

import adressbuch.Kontakt;
import adressbuch.ViewHelper;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;


/**
 * Repraesentiert einen Terminplaner mit einem Besitzer, der Termine ueber
 * den Server an alle angemeldeten Teilnehmer schicken kann.
 *
 * @author beuth
 */
public class Terminplaner extends TerminVerwaltung {

    private Kontakt besitzer;
    Terminplaner planer;

    /**
     * Initialisiert den Terminplaner. Hier wird der Client erzeugt und der
     * Besitzer gesetzt.
     *
     * @param k Besitzer des Planers
     */
    public Terminplaner(Kontakt k) {
        besitzer = k;
        setTestData();
    }

    public Kontakt getBesitzer() {
        return besitzer;
    }

    public void setBesitzer(Kontakt besitzer) {
        this.besitzer = besitzer;
    }

    /**
     * Darf der Termin von dem Besitzer geaendert werden. Ist nur erlaubt, wenn der
     * Besitzer des Terminplaners auch der Besitzer des Termins ist.
     *
     * @param t Termin, der geprueft werden soll.
     * @return true, wenn er geaendert werden darf, false sonst.
     */
    public boolean updateErlaubt(Termin t) {
        if (t.getBesitzer() == null) {
            return true;
        } else {
            return besitzer.equals(t.getBesitzer());
        }

    }

    /**
     * Fuegt dem Planer einen neuen Termin hinzu. Sollte noch kein Besitzer
     * eingetrage sein, so wird der Besitzer des Planers gesetzt.
     *
     * @param t neuer Termin.
     * @throws TerminUeberschneidungException
     */
    public void newTermin(Termin t) throws TerminUeberschneidungException {
        if (t.getBesitzer() == null) {
            t.setBesitzer(besitzer);
        }
        addTermin(t);
    }

    /**
     * Ein neuer/editierter/empfangener Termin soll in den Planer eingefuegt werden.
     * Handelt es sich um einen Termin ohne Besitzer oder mit einem anderen Besitzer als dem
     * des Planers, so wird er als neuer Termin hinzugefuegt. Ansonsten muss der
     * Termin im Planer aktualisiert werden.
     *
     * @param neu einzufuegender Termin.
     * @throws TerminUeberschneidungException
     */
    public void setTermin(Termin neu) throws TerminUeberschneidungException {
        if (neu != null && !besitzer.equals(neu.getBesitzer())) {
            newTermin(neu);
        } else {
            updateTermin(neu);
        }
    }


    private void setTestData() {
        LocalDate d = LocalDate.now();
        Kontakt dave = new Kontakt("david", "08459 100000", "david@gmx.de");
        try {
            Termin t1 = new Termin("Termin1", d, LocalTime.of(9, 0), LocalTime.of(10, 0));
            Termin t2 = new Termin("Termin2", d, LocalTime.of(10, 30), LocalTime.of(11, 0));
            Termin t3 = new Termin("Termin3", d, LocalTime.of(11, 15), LocalTime.of(12, 30));
            Termin t4 = new Termin("Termin4", d, LocalTime.of(13, 15), LocalTime.of(14, 30));
            t1.setBesitzer(besitzer);
            t2.setBesitzer(besitzer);
            t3.setBesitzer(besitzer);
            t4.setBesitzer(dave);
            addTermin(t1);
            addTermin(t2);
            addTermin(t3);
            addTermin(t4);
        } catch (UngueltigerTerminException e) {
            System.out.println(e);
        }
    }

    // Diese Methode soll alle Termine des Terminplaners in der mitgelieferten Datei(file) in ser. Form abspeichern.
    // Auftretende Exception an PlanerViewController weiterleiten, um dort in einem Fehlerdialog angezeigt zu werden
    public void save(File file) throws IOException {
        if (file == null) return;
        String filePath = file.getAbsolutePath();

        //FOS verbindet sich mit der Datei (übergebenen Pfad der Datei)
        FileOutputStream fileStream = null;
        try {
            fileStream = new FileOutputStream(filePath);
        } catch (FileNotFoundException ex) {
            ViewHelper.showError("Datei konnte nicht gefunden werden.");
            System.out.println(ex);
        }

        ObjectOutputStream os = new ObjectOutputStream(fileStream);
        // OOS schreibt alle Termine in die Datei
        os.writeObject(getAllTermine());
        //Datei wird geschlossen und mit ihr alle darunterliegenden Steams
        os.close();

    }

    /* Die Methode soll aus der übergebenen Datei probieren, alle Termine einzulesen
     * Falls erfolgreich, dann soll der Terminplaner zurückgesetzt werden und die eingelesenen Termine hinzugefügt
    */

    public void load(File file) throws IOException {
        String filePath = file.getAbsolutePath();
        FileInputStream fileStream = null;
        try {
            fileStream = new FileInputStream(filePath);
        } catch (FileNotFoundException ex) {
            ViewHelper.showError("Datei konnte nicht gefunden werden.");
            System.out.println(ex);
        }
        ObjectInputStream os = new ObjectInputStream(fileStream);

        // Alle Termine werden aus dem Objekt des Stroms ausgelesen und in ein Termin[] gesteckt
        try {
            Termin[] alleTermine = (Termin[]) os.readObject();
            // Mit Hilfe der For Schleife wird jeder einzelne Termin des Arrays durchiteriert und im Terminkalender hinzufügt
            for (Termin termin : alleTermine) {
                try {
                    addTermin(termin);
                } catch (TerminUeberschneidungException ex) {
                    ViewHelper.showError("Termine überschneiden sich.");
                }
            }
        } catch (ClassNotFoundException ex) {
            System.out.println(ex);
        }
        os.close();
    }
}
