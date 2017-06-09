package game;

import javafx.geometry.Point2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.*;

/**
 * 07.05.2017
 *
 * @author SWirries
 */
public class Landschaftskarte {
    private Wiesenstueck[] wiestenstuecke = null;
    private Stadtteil[] stadtteile = null;
    private Strassenabschnitt[] strassenabschnitte = null;
    private Kloster kloster = null;
    private String name = "";
    private int[] legeNr;
    private double rotationDegree = 0;

    private Image image;
    private ImageView imageView;

    private double x,y,width,height;

    public Landschaftskarte() {
        //TODO REMOVE
        //Wird nur zum Testen benötigt
    }

    public Landschaftskarte(String name, Wiesenstueck[] wiestenstuecke, Kloster kloster) {
        this.wiestenstuecke = wiestenstuecke;
        this.kloster = kloster;
        this.name = name;
        init();
    }

    public Landschaftskarte(String name, Wiesenstueck[] wiestenstuecke, Strassenabschnitt[] strassenabschnitte, Kloster kloster) {
        this.wiestenstuecke = wiestenstuecke;
        this.strassenabschnitte = strassenabschnitte;
        this.kloster = kloster;
        this.name = name;
        init();
    }

    public Landschaftskarte(String name, Stadtteil[] stadtteile) {
        this.stadtteile = stadtteile;
        this.name = name;
        init();
    }

    public Landschaftskarte(String name, Wiesenstueck[] wiestenstuecke, Stadtteil[] stadtteile, Strassenabschnitt[] strassenabschnitte) {
        this.wiestenstuecke = wiestenstuecke;
        this.stadtteile = stadtteile;
        this.strassenabschnitte = strassenabschnitte;
        this.name = name;
        init();
    }

    public Landschaftskarte(String name, Wiesenstueck[] wiestenstuecke, Stadtteil[] stadtteile) {
        this.wiestenstuecke = wiestenstuecke;
        this.stadtteile = stadtteile;
        this.name = name;
        init();
    }

    public Landschaftskarte(String name, Wiesenstueck[] wiestenstuecke, Strassenabschnitt[] strassenabschnitte) {
        this.wiestenstuecke = wiestenstuecke;
        this.strassenabschnitte = strassenabschnitte;
        this.name = name;
        init();
    }

    public Wiesenstueck[] getWiestenstuecke() {
        return wiestenstuecke;
    }

    public Stadtteil[] getStadtteile() {
        return stadtteile;
    }

    public Strassenabschnitt[] getStrassenabschnitte() {
        return strassenabschnitte;
    }

    public Kloster getKloster() {
        return kloster;
    }

    public boolean addNeighbor(Landschaftskarte landschaftskarte, HimmelsrichtungT himmelsrichtungT, boolean connect){

        /**
         * aus Sicht der Karte die
         * ich füge mir einen Nachbarn hinzu der in Himmelsrichtung-lich liegt
         */
        switch (himmelsrichtungT){
            case NORD: //nördlich ich bin für ihn suedlich
                return addNordSuedNeighbor(landschaftskarte,this, HimmelsrichtungT.NORD, connect);

            case OST: //oestlich ich bin für ihn westlich
                return addOstWestNeighbor(landschaftskarte,this, HimmelsrichtungT.OST, connect);

            case SUED://suedlich ich bin für ihn noerdlich
                return addNordSuedNeighbor(this,landschaftskarte, HimmelsrichtungT.SUED, connect);

            case WEST: //Westlich ich bin für ihn oestlich
                return addOstWestNeighbor(this,landschaftskarte, HimmelsrichtungT.WEST, connect);

        }
        return false;
    }

    private boolean addNordSuedNeighbor(Landschaftskarte landNord, Landschaftskarte landSued, HimmelsrichtungT himmelsrichtungT, boolean connect){

        boolean wiesenOk = false;
        boolean stassenOk = false;
        boolean stadtOk = false;

        Set<Wiesenstueck> wiesenstueckSet1 = new HashSet<>();
        Set<Wiesenstueck> wiesenstueckSet2 = new HashSet<>();
        Set<Stadtteil> stadtteilSet = new HashSet<>();
        Set<Strassenabschnitt> strassenabschnittSet = new HashSet<>();

        Wiesenstueck[] stueckeNord = landNord.getWiestenstuecke();
        Strassenabschnitt[] abschnitteNord = landNord.getStrassenabschnitte();
        Stadtteil[] teileNord = landNord.getStadtteile();
        Kloster klosterNord = landNord.getKloster();

        Wiesenstueck[] stueckeSued = landSued.getWiestenstuecke();
        Strassenabschnitt[] abschnitteSued = landSued.getStrassenabschnitte();
        Stadtteil[] teileSued = landSued.getStadtteile();
        Kloster klosterSued = landSued.getKloster();

        if(klosterNord != null) klosterNord.addFillFreeField();
        if(klosterSued != null) klosterSued.addFillFreeField();
//        else System.out.println("game.Kloster NULL");

        //TODO man könnte davon ausgehen, dass nur richtig gelegt wird
        //TODO prüfen ob der break keien False Prositiv erzeugt
        if(stueckeNord != null || stueckeSued != null){
            boolean bNordWest = false;
            Wiesenstueck wsNordWest = null;
            boolean bNordOst = false;
            Wiesenstueck wsNordOst= null;
            boolean bSuedWest = false;
            Wiesenstueck wsSuedWest= null;
            boolean bSuedOst = false;
            Wiesenstueck wsSuedOst= null;
            if(stueckeNord != null) {
                for(Wiesenstueck wsNord : stueckeNord){
                    if(!bSuedWest){
                        bSuedWest = Arrays.asList(wsNord.getOffeneHalbKanten()).indexOf(HalbKantenT.SuedWest) >= 0;
                        if(bSuedWest) wsSuedWest = wsNord;
                    }
                    if(!bSuedOst){
                        bSuedOst = Arrays.asList(wsNord.getOffeneHalbKanten()).indexOf(HalbKantenT.SuedOst) >= 0;
                        if(bSuedOst) wsSuedOst = wsNord;
                    }

                }
            }
            if(stueckeSued != null) {
                for(Wiesenstueck wsSued : stueckeSued){
                    if(!bNordWest) {
                        bNordWest = Arrays.asList(wsSued.getOffeneHalbKanten()).indexOf(HalbKantenT.NordWest) >= 0;
                        if(bNordWest) wsNordWest = wsSued;
                    }

                    if(!bNordOst) {
                        bNordOst = Arrays.asList(wsSued.getOffeneHalbKanten()).indexOf(HalbKantenT.NordOst) >= 0;
                        if(bNordOst) wsNordOst = wsSued;
                    }
                }
            }
            if(bNordOst == bSuedOst && bNordWest == bSuedWest){
                wiesenOk = true;
                try {
                    //Es können NullPointer aufteten wenn die Karten keine Wiesen an den Positionen haben.
                    if(wsSuedOst.equals(wsSuedWest) || wsNordOst.equals(wsNordWest)) {
                        wiesenstueckSet1.add(wsSuedOst);
                        wiesenstueckSet1.add(wsSuedWest);
                        wiesenstueckSet1.add(wsNordOst);
                        wiesenstueckSet1.add(wsNordWest);
                    }else {
                        wiesenstueckSet1.add(wsSuedOst);
                        wiesenstueckSet2.add(wsSuedWest);
                        wiesenstueckSet1.add(wsNordOst);
                        wiesenstueckSet2.add(wsNordWest);
                    }
                } catch (Exception e) {
                }
            }
//            else System.out.println("SW"+bSuedWest+" SO"+bSuedOst);

        }else{
//            System.out.println("Wiesen NULL");
            wiesenOk = true;
        }

        if(abschnitteNord != null || abschnitteSued != null){
            boolean bNord = false;
            boolean bSued = false;
            if (abschnitteNord != null) {
                for(Strassenabschnitt strNord : abschnitteNord){
                    if (!bSued) {
                        bSued = Arrays.asList(strNord.getHimmelsrichtungenT()).indexOf(HimmelsrichtungT.SUED) >=0;
                        if (bSued) strassenabschnittSet.add(strNord);
                    }
                }
            }
            if (abschnitteSued != null) {
                for(Strassenabschnitt strSued : abschnitteSued){
                    if (!bNord){
                        bNord = Arrays.asList(strSued.getHimmelsrichtungenT()).indexOf(HimmelsrichtungT.NORD) >=0;
                        if (bNord) strassenabschnittSet.add(strSued);
                    }
                }
            }

            if(bNord == bSued){
                stassenOk =true;
//                System.out.println("Straßen passen!");
            }
//            else System.out.println("N"+bNord + " S"+ bSued);
        }else{
            stassenOk = true;
//            System.out.println("Straße NULL");
        }

        if(teileNord != null || teileSued != null){
            boolean bNord = false;
            boolean bSued = false;
            if (teileNord != null) {
                for(Stadtteil sdtNord : teileNord){
                    if (!bSued) {
                        bSued = Arrays.asList(sdtNord.getOffeneKanten()).indexOf(HimmelsrichtungT.SUED) >=0;
                        if(bSued) stadtteilSet.add(sdtNord);
                    }
                }
            }
            if (teileSued != null) {
                for(Stadtteil stdSued : teileSued){
                    if (!bNord) {
                        bNord = Arrays.asList(stdSued.getOffeneKanten()).indexOf(HimmelsrichtungT.NORD) >=0;
                        if(bNord) stadtteilSet.add(stdSued);
                    }
                }
            }
            if(bNord == bSued){
                stadtOk = true;
//                        System.out.println("Städte passen!");
            }
//                    else System.out.println("N"+bNord + " S"+ bSued);
        }else {
            stadtOk = true;
        }

        if (wiesenOk && stadtOk && stassenOk){
//            System.out.println("Karte kann gelegt werden!");
            if (connect){
                if(stueckeNord != null && stueckeSued != null){
                    connectWiesen(new ArrayList<>(wiesenstueckSet1));
                    connectWiesen(new ArrayList<>(wiesenstueckSet2));
                }
                if(abschnitteNord != null && abschnitteSued != null) connectStrassen(new ArrayList<>(strassenabschnittSet));
                if(teileNord != null && teileSued != null) connectStaedte(new ArrayList<>(stadtteilSet), himmelsrichtungT);
            }
            return true;
        }else{
            System.out.println("Karte kann NICHT gelegt werden! W:" + wiesenOk + " Sdt:" + stadtOk + " Str:"+stassenOk);
            return false;
        }

    }

    private boolean addOstWestNeighbor(Landschaftskarte landOst, Landschaftskarte landWest, HimmelsrichtungT himmelsrichtungT, boolean connect){

        boolean wiesenOk = false;
        boolean stassenOk = false;
        boolean stadtOk = false;

        Set<Wiesenstueck> wiesenstueckSet1 = new HashSet<>();
        Set<Wiesenstueck> wiesenstueckSet2 = new HashSet<>();
        Set<Stadtteil> stadtteilSet = new HashSet<>();
        Set<Strassenabschnitt> strassenabschnittSet = new HashSet<>();


        Wiesenstueck[] stueckOst = landOst.getWiestenstuecke();
        Strassenabschnitt[] abschnitteOst = landOst.getStrassenabschnitte();
        Stadtteil[] teileOst = landOst.getStadtteile();
        Kloster klosterOst = landOst.getKloster();

        Wiesenstueck[] stueckWest = landWest.getWiestenstuecke();
        Strassenabschnitt[] abschnitteWest = landWest.getStrassenabschnitte();
        Stadtteil[] teileWest = landWest.getStadtteile();
        Kloster klosterWest = landWest.getKloster();

        if(klosterOst != null) klosterOst.addFillFreeField();
        if(klosterWest != null) klosterWest.addFillFreeField();
//        else System.out.println("game.Kloster NULL");

        //TODO man könnte davon ausgehen, dass nur richtig gelegt wird
        //TODO prüfen ob der break keien False Prositiv erzeugt
        if(stueckOst != null || stueckWest != null){
            boolean bOstNord = false;
            Wiesenstueck wsOstNord = null;
            boolean bOstSued = false;
            Wiesenstueck wsOstSued = null;
            boolean bWestNord = false;
            Wiesenstueck wsWestNord = null;
            boolean bWestSued = false;
            Wiesenstueck wsWestSued = null;
            if (stueckWest != null) {
                for(Wiesenstueck wsWest : stueckWest){
                    if(!bOstNord) {
                        bOstNord = Arrays.asList(wsWest.getOffeneHalbKanten()).indexOf(HalbKantenT.OstNord) >= 0;
                        if(bOstNord) wsOstNord = wsWest;
                    }
                    if(!bOstSued) {
                        bOstSued = Arrays.asList(wsWest.getOffeneHalbKanten()).indexOf(HalbKantenT.OstSued) >= 0;
                        if(bOstSued) wsOstSued = wsWest;
                    }
                }
            }
            if (stueckOst != null) {
                for(Wiesenstueck wsOst : stueckOst){
                    if(!bWestNord) {
                        bWestNord = Arrays.asList(wsOst.getOffeneHalbKanten()).indexOf(HalbKantenT.WestNord) >= 0;
                        if(bWestNord) wsWestNord = wsOst;
                    }
                    if(!bWestSued) {
                        bWestSued = Arrays.asList(wsOst.getOffeneHalbKanten()).indexOf(HalbKantenT.WestSued) >= 0;
                        if(bWestSued) wsWestSued = wsOst;
                    }
                }
            }
            if(bOstSued == bWestSued && bOstNord == bWestNord){
                wiesenOk = true;
                //Es koennen NullPointer auftreten wenn keien Wiesen an diesen Kanten
                try {
                    if (wsOstNord.equals(wsOstSued) || wsWestNord.equals(wsWestSued)){
                        wiesenstueckSet1.add(wsOstNord);
                        wiesenstueckSet1.add(wsOstSued);
                        wiesenstueckSet1.add(wsWestNord);
                        wiesenstueckSet1.add(wsWestSued);
                    }else {
                        wiesenstueckSet1.add(wsOstNord);
                        wiesenstueckSet2.add(wsOstSued);
                        wiesenstueckSet1.add(wsWestNord);
                        wiesenstueckSet2.add(wsWestSued);
                    }
                } catch (Exception e) {
                }

            }
//            else System.out.println("SW"+bOstSued+" SO"+bOstSued);

        }else{
//            System.out.println("Wiesen NULL");
            wiesenOk = true;
        }

        if(abschnitteOst != null || abschnitteWest != null){
            boolean bOst = false;
            boolean bWest = false;
            if (abschnitteOst != null) {
                for(Strassenabschnitt strOst : abschnitteOst){
                    if (!bWest) {
                        bWest = Arrays.asList(strOst.getHimmelsrichtungenT()).indexOf(HimmelsrichtungT.WEST) >=0;
                        if (bWest) strassenabschnittSet.add(strOst);
                    }
                }
            }
            if (abschnitteWest != null) {
                for(Strassenabschnitt strWest : abschnitteWest){
                    if (!bOst) {
                        bOst = Arrays.asList(strWest.getHimmelsrichtungenT()).indexOf(HimmelsrichtungT.OST) >=0;
                        if (bOst) strassenabschnittSet.add(strWest);
                    }
                }
            }
            if(bOst == bWest){
                stassenOk =true;
//                        System.out.println("Straßen passen!");
            }
//                    else System.out.println("N"+bNord + " S"+ bSued);
        }else{
            stassenOk = true;
//            System.out.println("Straße NULL");
        }

//        System.out.println(teileOst.length  + " " + teileWest.length);
        if(teileOst != null || teileWest != null){
            boolean bOst = false;
            boolean bWest = false;
            if (teileOst != null) {
                for(Stadtteil stdOst : teileOst){
                    if (!bWest) {
                        bWest = Arrays.asList(stdOst.getOffeneKanten()).indexOf(HimmelsrichtungT.WEST) >=0;
                        if (bWest) stadtteilSet.add(stdOst);
                    }
                }
            }
            if (teileWest != null) {
                for(Stadtteil stdWest : teileWest){
                    if (!bOst) {
                        bOst = Arrays.asList(stdWest.getOffeneKanten()).indexOf(HimmelsrichtungT.OST) >=0;
                        if (bOst) stadtteilSet.add(stdWest);
                    }
                }
            }
            if(bOst == bWest){
                stadtOk =true;
//                        System.out.println("Städte passen!");
            }else System.out.println("O"+bOst + " S"+ bWest);
        }else {
            stadtOk = true;
        }

        if (wiesenOk && stadtOk && stassenOk){
//            System.out.println("Karte kann gelegt werden!");
            if(connect){
                if (stueckOst != null && stueckWest != null){
                    connectWiesen(new ArrayList<>(wiesenstueckSet1));
                    connectWiesen(new ArrayList<>(wiesenstueckSet2));
                }
                if (abschnitteOst != null && abschnitteWest != null) connectStrassen(new ArrayList<>(strassenabschnittSet));
                if (teileOst != null && teileWest != null) connectStaedte(new ArrayList<>(stadtteilSet),himmelsrichtungT);
            }
            return true;
        }else{
            System.out.println("Karte kann NICHT gelegt werden! W:" + wiesenOk + " Sdt:" + stadtOk + " Str:"+stassenOk);
            return false;
        }

    }

    private void connectWiesen(ArrayList<Wiesenstueck> wsList){
        //TODO Wiesenstücke die nicht verbunden sind werden zusammen getan
        Wiese mainWiese = new Wiese();

        for(Wiesenstueck ws : wsList){
            if(ws.getWiese().getAnzahlWiesenstuecke() > mainWiese.getAnzahlWiesenstuecke()){
                mainWiese = ws.getWiese();
            }
        }
//        System.out.println("Main Vor:"+mainWiese.getAnzahlWiesenstuecke());
        for(Wiesenstueck ws : wsList){
            if(!mainWiese.contains(ws)){
                Wiese currentWiese = ws.getWiese();
                ArrayList<Wiesenstueck> allStueck = currentWiese.getWiesenstuecke();
                for(Wiesenstueck element : allStueck){
                    //TODO auf Fehler prüfen ConcurrentModificationException wenn aktiv (Liste kann nicht geändert werden während Schleife
//                    currentWiese.removeWiesenstuecke(element);
                    element.setWiese(mainWiese);
                    mainWiese.addWiesenstueck(element);
                }
            }
        }
        mainWiese.isBesetzt();

//        System.out.println("game.Wiese: "+mainWiese.getAnzahlWiesenstuecke());
    }

    private void connectStaedte(ArrayList<Stadtteil> sdtList, HimmelsrichtungT himmelsrichtungT){
        Stadt mainStadt = new Stadt();
        for(Stadtteil sdt : sdtList){
            if(sdt.getStadt().getAnzahlStadtteile() > mainStadt.getAnzahlStadtteile()){
//                System.out.println("main "+ mainStadt.getAnzahlStadtteile() + " other "+sdt.getStadt().getAnzahlStadtteile());
                mainStadt  = sdt.getStadt();
            }
        }

        for(Stadtteil sdt : sdtList){
            if(!mainStadt.contains(sdt)){
                Stadt currentStadt = sdt.getStadt();
                mainStadt.integrateStadt(currentStadt,himmelsrichtungT);
//                ArrayList<game.Stadtteil> allStueck = currentStadt.getStadtteile();
//                for(game.Stadtteil element : allStueck){
//                    //TODO auf Fehler prüfen ConcurrentModificationException wenn aktiv (Liste kann nicht geändert werden während Schleife
////                    currentWiese.removeWiesenstuecke(element);
//                    element.setStadt(mainStadt);
//                    mainStadt.addStadteil(element, himmelsrichtungT);
//                }

            }
        }
        mainStadt.isBesetzt();

//        System.out.println("Staedte: "+mainStadt.getAnzahlStadtteile());
    }

    private void connectStrassen(ArrayList<Strassenabschnitt> strList){
        Strasse mainStrasse = new Strasse();
        for(Strassenabschnitt str : strList){
            if(str.getStrasse().getAnzahlStrassenabschnitte() > mainStrasse.getAnzahlStrassenabschnitte()){
                mainStrasse = str.getStrasse();
            }
        }
        for(Strassenabschnitt str : strList){
            if(!mainStrasse.contains(str)){
                Strasse currentStrasse = str.getStrasse();
                ArrayList<Strassenabschnitt> allAbschnitt = currentStrasse.getStrassenabschnitte();
                for(Strassenabschnitt element : allAbschnitt){
                    //TODO auf Fehler prüfen ConcurrentModificationException wenn aktiv (Liste kann nicht geändert werden während Schleife
//                    currentStrasse.removeStrassenabschnitt(element);
                    element.setStrasse(mainStrasse);
                    mainStrasse.addStrassenabschnitt(element);
                }
            }
        }
        mainStrasse.isBesetzt();
//        System.out.println("game.Strasse: "+ mainStrasse.getAnzahlStrassenabschnitte());
    }

    @Override
    public String toString() {
        return super.toString() + " Typ "+name.toUpperCase();
    }

    public void rotate(boolean direction, int cout){
        for (int i = 0; i<cout;i++) {
            if(strassenabschnitte != null) {
                for(Strassenabschnitt str : strassenabschnitte){
                    str.rotate(direction);
                }
            }
            if(stadtteile != null) {
                for(Stadtteil sdt : stadtteile){
                    sdt.rotate(direction);
                }
            }
            if (wiestenstuecke != null) {
                for(Wiesenstueck stk : wiestenstuecke){
                    stk.rotate(direction);
                }
            }
            //TODO rotate Image
            if(direction){
                rotationDegree = (rotationDegree + 90)%360;
            }else {
                rotationDegree = (rotationDegree - 90)%360;
            }
            imageView.setRotate(rotationDegree);
        }
    }

    public void getInformation(){
        System.out.println("--INFO--");
        System.out.println(this);
        if (wiestenstuecke != null) {
            for (Wiesenstueck ws : wiestenstuecke){
                System.out.println(ws.getWiese() + " Anzahl:" + ws.getWiese().getAnzahlWiesenstuecke() + " Wert: " +
                ws.getWiese().getTotalWert());
            }
        }
        if (strassenabschnitte != null) {
            for(Strassenabschnitt str : strassenabschnitte){
                System.out.println(str.getStrasse() + " Anzahl:" + str.getStrasse().getAnzahlStrassenabschnitte() +
                        " Fertig: " + str.getStrasse().isAbgeschlossen() +
                        " Ausrichtung:"+str.getStartRichtung() + " " + str.getEndRichtung());
            }
        }
        if (stadtteile != null) {
            for (Stadtteil sdt : stadtteile){
                String stadtteilRota = "";
                for(HimmelsrichtungT t :sdt.getOffeneKanten()){
                    stadtteilRota += " " + t;
                }

                System.out.println(sdt.getStadt() + " Anzahl:" + sdt.getStadt().getAnzahlStadtteile() + " Fertig: " +
                        sdt.getStadt().isAbgeschlossen() +" Ausrichtung:"+ stadtteilRota);
            }
        }
        if (kloster != null){
            System.out.println(kloster);
        }
        System.out.println("--END--");
    }

    public void setGefolgsmann(Gefolgsmann gefolgsmann){
        //TODO der Gefolgsman wird manchmal nicht richtig gesetzt
        Landschaftsteil output = null;
        ArrayList<String> choices = new ArrayList<>();
        HashMap<String, Landschaftsteil> auswahl = new HashMap<>();
        boolean isKloster =false;
        if(stadtteile != null){
            for(Stadtteil sdt : stadtteile){
                if (!sdt.getStadt().isBesetzt()) {
                    //TODO REMOVE
                    System.out.println("Stadt:"+sdt.getStadt().isBesetzt() + " " +sdt.getStadt().getAnzahlStadtteile());
                    choices.add(sdt.toString());
                    auswahl.put(sdt.toString(),sdt);
                }
            }
        }
        if(wiestenstuecke != null){
            for(Wiesenstueck stueck: wiestenstuecke){
                if (!stueck.getWiese().isBesetzt()) {
                    choices.add(stueck.toString());
                    auswahl.put(stueck.toString(),stueck);
                }
            }
        }
        if(strassenabschnitte != null){
            for(Strassenabschnitt str : strassenabschnitte){
                if (!str.getStrasse().isBesetzt()) {
                    choices.add(str.toString());
                    auswahl.put(str.toString(),str);
                }
            }
        }
        if (kloster != null){
            choices.add(kloster.toString());
            auswahl.put(kloster.toString(),kloster);
        }
        if(choices.size() == 0) return;
        ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0),choices);
        dialog.setTitle("Gebiet festlegen");
        dialog.setHeaderText("Gebiet zum Plazieren wählen:");

        Optional<String> result = dialog.showAndWait();
        if(result.isPresent()){
//            auswahl.get(result.get()).setBesetzer(gefolgsmann);
            output = auswahl.get(result.get());
        }

        HimmelsrichtungT himmelsrichtungT = HimmelsrichtungT.STOP;
        HalbKantenT[] offeneHalbKanten = null;
        if(output != null && output.getClass().equals(Strassenabschnitt.class)){
            Strassenabschnitt strassenabschnitt = (Strassenabschnitt) output;
            himmelsrichtungT = strassenabschnitt.getStartRichtung();
            strassenabschnitt.setBesetzer(gefolgsmann);
        }
        if(output != null && output.getClass().equals(Stadtteil.class)){
            Stadtteil stadtteil = (Stadtteil) output;
            himmelsrichtungT = stadtteil.getOffeneKanten()[0];
            stadtteil.setBesetzer(gefolgsmann);
        }
        if(output != null && output.getClass().equals(Wiesenstueck.class)){
            Wiesenstueck wiesenstueck = (Wiesenstueck) output;
            offeneHalbKanten = wiesenstueck.getOffeneHalbKanten();
            wiesenstueck.setBesetzer(gefolgsmann);
        }
        if(output != null && output.getClass().equals(Kloster.class)){
            isKloster = true;
            Kloster kloster = (Kloster) output;
            kloster.setBesetzer(gefolgsmann);
        }
//        if(output!=null) output.setBesetzer(gefolgsmann);

        //weitere
        double gfX = 0, gfY = 0;
        if(isKloster){
            gfX = (1/2d)*83d-25d/2d;
            gfY = (1/2d)*83d-24d/2d;
        }else if(himmelsrichtungT != HimmelsrichtungT.STOP){
            switch (himmelsrichtungT){
                case NORD:
                    gfX = (1/2d)*83d-25d/2d;
                    gfY = (1/4d)*83d-24d/2d;
                    break;
                case SUED:
                    gfX = (1/2d)*83d-25d/2d;
                    gfY = (3/4d)*83d-24d/2d;
                    break;
                case OST:
                    gfX = (3/4d)*83d-25d/2d;
                    gfY = (1/2d)*83d-24d/2d;
                    break;
                case WEST:
                    gfX = (1/4d)*83d-25d/2d;
                    gfY = (1/2d)*83d-24d/2d;
                    break;
            }

        }else if(offeneHalbKanten != null){
            //TODO Position anpassen, ist bei Ecken ok aber bei R,S,Q schlecht
//            if(offeneHalbKanten.length == 1){
                switch (offeneHalbKanten[0]){
                    case NordWest:
                        gfX = (1/4d)*83d-25d/2d;
                        gfY = (1/4d)*83d-24d/2d;
                        break;
                    case NordOst:
                        gfX = (3/4d)*83d-25d/2d;
                        gfY = (1/4d)*83d-24d/2d;
                        break;
                    case OstNord:
                        gfX = (3/4d)*83d-25d/2d;
                        gfY = (1/4d)*83d-24d/2d;
                        break;
                    case OstSued:
                        gfX = (3/4d)*83d-25d/2d;
                        gfY = (3/4d)*83d-24d/2d;
                        break;
                    case SuedOst:
                        gfX = (3/4d)*83d-25d/2d;
                        gfY = (3/4d)*83d-24d/2d;
                        break;
                    case SuedWest:
                        gfX = (1/4d)*83d-25d/2d;
                        gfY = (3/4d)*83d-24d/2d;
                        break;
                    case WestSued:
                        gfX = (1/4d)*83d-25d/2d;
                        gfY = (3/4d)*83d-24d/2d;
                        break;
                    case WestNord:
                        gfX = (1/4d)*83d-25d/2d;
                        gfY = (1/4d)*83d-24d/2d;
                        break;
                }
//            }
        }
        if(gfX != 0 || gfY != 0){
            //TODO REMOVE
//            System.out.println(getX() + " " +getY() + " W"+getWidth() +" H"+getHeight() + " gfX"+gfX+" gfY"+gfY + " Rolle:"+gefolgsmann.getRolle());
            gefolgsmann.setAbsolutePosition(new Point2D(this.getX()*this.getWidth()+gfX, this.getY()*this.getHeight()+gfY));
        }
    }

    public Image getImage(){
        return imageView.snapshot(new SnapshotParameters(),null);
    }

    public int[] getLegeNr() {
        return this.legeNr;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getWidth() {
        return image.getWidth();
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return image.getHeight();
    }

    public void setHeight(double height) {
        this.height = height;
    }

    private void init(){
        switch (name){
            case "A":
                image = Stapel.imageA;
                break;
            case "B":
                image = Stapel.imageB;
                break;
            case "C":
                image = Stapel.imageC;
                break;
            case "D":
                image = Stapel.imageD;
                break;
            case "E":
                image = Stapel.imageE;
                break;
            case "F":
                image = Stapel.imageF;
                break;
            case "G":
                image = Stapel.imageG;
                break;
            case "H":
                image = Stapel.imageH;
                break;
            case "I":
                image = Stapel.imageI;
                break;
            case "J":
                image = Stapel.imageJ;
                break;
            case "K":
                image = Stapel.imageK;
                break;
            case "L":
                image= Stapel.imageL;
                break;
            case "M":
                image = Stapel.imageM;
                break;
            case "N":
                image = Stapel.imageN;
                break;
            case "O":
                image = Stapel.imageO;
                break;
            case "P":
                image = Stapel.imageP;
                break;
            case "Q":
                image = Stapel.imageQ;
                break;
            case "R":
                image = Stapel.imageR;
                break;
            case "S":
                image = Stapel.imageS;
                break;
            case "T":
                image = Stapel.imageT;
                break;
            case  "U":
                image = Stapel.imageU;
                break;
            case "V":
                image = Stapel.imageV;
                break;
            case "W":
                image = Stapel.imageW;
                break;
            case "X":
                image = Stapel.imageX;
        }
        imageView = new ImageView(image);
    }
}
