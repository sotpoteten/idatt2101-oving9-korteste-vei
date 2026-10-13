import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Graf {
    int antNoder;
    int antKanter;
    int antInteressepkt;
    Node[] noder;
    int antLandemerker = 5;
    Node[] landemerker = new Node[antLandemerker];
    int[][] avstanderFraLandemerker;
    int[][] avstanderTilLandemerker;
    final int EARTH_RADIUS = 6371;

    public Graf() {
        lesNoder();
        lesKanter();
        lesInteressepunkter();
        preprosessering();
        //System.out.println("Leser preprosesserte data");
        //lesLandemerkeAvstanderFraFil();
    }

    public static void main(String[] args) {
        Graf g = new Graf();

        /*
        System.out.println("Kjører Djikstra");
        kjørDjikstra(g, 14469, 15149); //Olafsfjördur - Hafnarfjördur
        nullstillNoder(g);
        System.out.println("Kjører ALT");
        kjørALT(g, 14469, 15149); //Olafsfjördur - Hafnarfjördur
        nullstillNoder(g); 
        */

        System.out.println("Kjører Djikstra");
        kjørDjikstra(g, 5009309, 999080); //Selbustrand - GreenStar Hotel Lahti
        nullstillNoder(g);
        System.out.println("Kjører ALT");
        kjørALT(g, 5009309, 999080); //Selbustrand - GreenStar Hotel Lahti
        nullstillNoder(g);
        System.out.println("");

        nullstillNoder(g);
        System.out.println("Kjører Djikstra");
        kjørDjikstra(g, 2800567, 7705656); //Kårvåg - Gjemnes
        nullstillNoder(g);
        System.out.println("Kjører ALT");
        kjørALT(g, 2800567, 7705656); //Kårvåg - Gjemnes
        nullstillNoder(g);
        System.out.println("");

        nullstillNoder(g);
        System.out.println("Kjører Djikstra");
        kjørDjikstra(g, 647826, 136530); //Malmö Brygghus–Nørrebro
        nullstillNoder(g);
        System.out.println("Kjører ALT");
        kjørALT(g, 647826, 136530); //Malmö Brygghus–Nørrebro
        nullstillNoder(g);
        System.out.println("");

        System.out.println("Kjører Djikstra");
        kjørDjikstra(g, 2266026, 7826348); //Orkanger - Trondheim
        nullstillNoder(g);
        System.out.println("Kjører ALT");
        kjørALT(g, 2266026, 7826348); //Orkanger - Trondheim
        nullstillNoder(g);
        System.out.println("");

        System.out.println("Kjører Djikstra");
        kjørDjikstra(g, 7826348, 2948202); //Trondheim - Oslo
        nullstillNoder(g);
        System.out.println("Kjører ALT");
        kjørALT(g, 7826348, 2948202); //Trondheim - Oslo
        nullstillNoder(g);
        System.out.println("");

        

        kjørDjikstraForInteressepunkt(g, 2266026, 4, 5); //Ladestasjoner fra Orkanger
        nullstillNoder(g);
        System.out.println("");
        kjørDjikstraForInteressepunkt(g, 3005466, 16, 5); //Drikkesteder fra Trondheim Camping
        nullstillNoder(g);
        System.out.println("");
        kjørDjikstraForInteressepunkt(g, 3240367, 8, 5); //Spisesteder fra Hotell Östersund
        nullstillNoder(g);
        System.out.println("");
    }

    private void lesNoder() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("noder.txt"));
            System.out.println("Prosesserer noder");
        } catch (FileNotFoundException e) {
            System.out.println("Fant ikke filen");
            System.exit(0);
        }
        try {
            this.antNoder = Integer.parseInt(reader.readLine().trim());
            this.noder = new Node[antNoder];
            String linje = reader.readLine();
            for (int i = 0; i < antNoder; i++) {
                String[] felt = hspilt(linje, 3);
                int nodeNr = Integer.parseInt(felt[0]);
                double breddegrad = Double.parseDouble(felt[1]);
                double lengdegrad = Double.parseDouble(felt[2]);
                noder[i] = new Node(nodeNr, breddegrad, lengdegrad);
                linje = reader.readLine();
            }
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void lesKanter() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("kanter.txt"));
            System.out.println("Prosesserer kanter");
        } catch (FileNotFoundException e) {
            System.out.println("Fant ikke filen");
            System.exit(0);
        }
        try {
            this.antKanter = Integer.parseInt(reader.readLine().trim());
            String linje = reader.readLine();
            for (int i = 0; i < antKanter; i++) {
                String[] felt = hspilt(linje, 5);
                int fraNodeNr = Integer.parseInt(felt[0]);
                int tilNodeNr = Integer.parseInt(felt[1]);
                int kjoretid = Integer.parseInt(felt[2]);
                //int lengde = Integer.parseInt(felt[3]);
                //int fartgrense = Integer.parseInt(felt[4]);
                Node fraNode = noder[fraNodeNr];
                fraNode.kanter.add(new Kant(noder[fraNodeNr], noder[tilNodeNr], kjoretid));
                linje = reader.readLine();
            }
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void lesInteressepunkter() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("interessepkt.txt"));
            System.out.println("Prosesserer interessepunkter");
        } catch (FileNotFoundException e) {
            System.out.println("Fant ikke filen");
            System.exit(0);
        }
        try {
            this.antInteressepkt = Integer.parseInt(reader.readLine().trim());
            String linje = reader.readLine();
            for (int i = 0; i < antInteressepkt; i++) {
                String[] felt = hspilt(linje, 2);
                int nodeNr = Integer.parseInt(felt[0]);
                int kode = Integer.parseInt(felt[1]);
                int start = 0;
                int slutt = 0;
                for (int j = 0; j < linje.length(); j++) {
                    if (linje.charAt(j) == '"') {
                        if (start == 0) {
                            start = j;
                        } else {
                            slutt = j;
                        }
                    }
                }
                String navn = linje.substring(start + 1, slutt);
                noder[nodeNr].kode = kode;
                noder[nodeNr].navn = navn;
                
                linje = reader.readLine();
            }
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void preprosessering() {
        Node start = noder[0];
        avstanderFraLandemerker = new int[antNoder][antLandemerker];
        avstanderTilLandemerker = new int[antNoder][antLandemerker];

        //Finner startnode: noden lengst bort fra node 0
        dijkstraForLandemerke(start);
        int maksTid = 0;
        for (int i = 0; i < antNoder; i++) {
            if (noder[i].tid > maksTid) {
                maksTid = noder[i].tid;
                landemerker[0] = noder[i];
            }
        }
        nullstillNoder();

        //Finner landemerker lengst vekk i luftlinje fra de tidligere landemerkene
        for (int i = 1; i < antLandemerker; i++) {
            double maksAvstand = 0;
            Node nesteLandemerke = null;
            for (Node n : noder) {
                double sumAvstandFraLandemerker = 0;
                for (int k = i - 1; k >= 0; k--) {
                    sumAvstandFraLandemerker += calculateDistance(n.breddegrad, n.lengdegrad, landemerker[k].breddegrad, landemerker[k].lengdegrad);
                }
                if (sumAvstandFraLandemerker > maksAvstand) {
                    maksAvstand = sumAvstandFraLandemerker;
                    nesteLandemerke = n;
                }
            }
            landemerker[i] = nesteLandemerke;
        }
        
        for (int i = 0; i < antLandemerker; i++) {
            System.out.println(landemerker[i].breddegrad + ", " + landemerker[i].lengdegrad);
            while (dijkstra(landemerker[i], start) == -1) {
                int nyIndeks = landemerker[i].nodeNr + 5;
                if (nyIndeks > antNoder) {
                    nyIndeks = 0;
                }
                landemerker[i] = noder[nyIndeks];
                nullstillNoder();
            }
        }

        for (int i = 0; i < antLandemerker; i++) {
            System.out.println("Finner avstander fra landemerke nr " + i);
            dijkstraForLandemerke(landemerker[i]);
            for (int j = 0; j < antNoder; j++) {
                avstanderFraLandemerker[j][i] = noder[j].tid;
            }
            nullstillNoder();
        }


        //Legger til omvendte kanter
        for (int i = 0; i < antNoder; i++) {
            for (Kant k : noder[i].kanter) {
                noder[k.tilNode.nodeNr].leggTilOmvendtKant(new Kant(noder[k.tilNode.nodeNr], noder[k.fraNode.nodeNr], k.kjoretid));
            }
        }

        for (int i = 0; i < antLandemerker; i++) {
            System.out.println("Finner avstander til landemerke nr " + i);
            dijkstraForLandemerkeOmvendt(landemerker[i]);
            for (int j = 0; j < antNoder; j++) {
                avstanderTilLandemerker[j][i] = noder[j].tid;
            }
            nullstillNoder();
        }

        //skrivLandemerkeAvstanderTilFil();
    }

    public void skrivLandemerkeAvstanderTilFil() {
        try {
            FileWriter writer1 = new FileWriter(new File("avstanderFraLandemerker.csv"));
            FileWriter writer2 = new FileWriter(new File("avstanderTilLandemerker.csv"));

            for (int i = 0; i < antNoder; i++) {
                for (int j = 0; j < antLandemerker; j++) {
                    writer1.write(avstanderFraLandemerker[i][j] + ",");
                    writer2.write(avstanderTilLandemerker[i][j] + ",");
                }
                writer1.write("\n");
                writer2.write("\n");
            }
            writer1.close();
            writer2.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void lesLandemerkeAvstanderFraFil() {
        avstanderFraLandemerker = new int[antNoder][antLandemerker];
        avstanderTilLandemerker = new int[antNoder][antLandemerker];

        Scanner in1 = null;
        Scanner in2 = null;
        try {
            in1 = new Scanner(new FileReader("avstanderFraLandemerker.csv"));
            in2 = new Scanner(new FileReader("avstanderTilLandemerker.csv"));

            int cnt = 0;
            while (in1.hasNextLine() && in2.hasNextLine() && cnt < antNoder) {
                String[] linje1 = in1.nextLine().split(",");
                String[] linje2 = in2.nextLine().split(",");
                for (int j = 0; j < antLandemerker; j++) {
                    avstanderFraLandemerker[cnt][j] = Integer.parseInt(linje1[j]);
                    avstanderTilLandemerker[cnt][j] = Integer.parseInt(linje2[j]);
                }
                cnt++;
            }  
        } catch (IOException e) {
            System.out.println("Noe gikk galt");
            System.exit(0);
        }

        
    }

    double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);
        double lon1Rad = Math.toRadians(lon1);
        double lon2Rad = Math.toRadians(lon2);
    
        double x = (lon2Rad - lon1Rad) * Math.cos((lat1Rad + lat2Rad) / 2);
        double y = (lat2Rad - lat1Rad);
        double distance = Math.sqrt(x * x + y * y) * EARTH_RADIUS;
    
        return distance;
    }

    public String[] hspilt(String linje, int antall) {
        String[] felt = new String[antall];
        int j = 0;
        int lengde = linje.length();
        for (int i = 0; i < antall; i++) {
            while (linje.charAt(j) <= ' ') j++;

            int ordstart = j;
            while (j < lengde && linje.charAt(j) > ' ') j++;
            felt[i] = linje.substring(ordstart, j);
        }
        return felt;
    }

    public int dijkstra(Node fra, Node til) {
        PriorityQueue<Node> kø = new PriorityQueue<>((a, b) -> a.tid - b.tid);
        fra.tid = 0;
        kø.add(fra);
        fra.iKø = true;
        til.endePunkt = true;
        int cnt = 0;
        while (!kø.isEmpty()) {
            Node n = kø.poll();
            cnt++;
            if (n.endePunkt) return cnt;
            for (Kant k : n.kanter) {
                if (k.tilNode.tid > n.tid + k.kjoretid) {
                    if (k.tilNode.iKø) {
                        kø.remove(k.tilNode);
                    }
                    k.tilNode.tid = n.tid + k.kjoretid;
                    k.tilNode.forgjenger = n;
                    k.tilNode.iKø = true;
                    kø.add(k.tilNode);
                }
            }
        }
        return -1;
    }

    public Node[] dijkstraForInteressepunkt(Node fra, int type, int antall) {
        PriorityQueue<Node> kø = new PriorityQueue<>((a, b) -> a.tid - b.tid);
        fra.tid = 0;
        kø.add(fra);
        fra.iKø = true;
        Node[] interessepunkter = new Node[antall];
        int cnt = 0;
        while (!kø.isEmpty()) {
            Node n = kø.poll();
            if (n.kode != -1 && (n.kode & type) == type) {
                interessepunkter[cnt] = n;
                cnt++;
            }
            if (cnt == antall) {
                break;
            }
            for (Kant k : n.kanter) {
                if (k.tilNode.tid > n.tid + k.kjoretid) {
                    if (!k.tilNode.iKø) {
                        kø.remove(k.tilNode);
                    }
                    k.tilNode.tid = n.tid + k.kjoretid;
                    k.tilNode.forgjenger = n;
                    k.tilNode.iKø = true;
                    kø.add(k.tilNode);
                }
            }
        }
        return interessepunkter;
    }

    public void dijkstraForLandemerke(Node fra) {
        PriorityQueue<Node> kø = new PriorityQueue<>((a, b) -> a.tid - b.tid);
        fra.tid = 0;
        kø.add(fra);
        while (!kø.isEmpty()) {
            Node n = kø.poll();
            for (Kant k : n.kanter) {
                if (k.tilNode.tid > n.tid + k.kjoretid) {
                    kø.remove(k.tilNode);
                    k.tilNode.tid = n.tid + k.kjoretid;
                    kø.add(k.tilNode);
                }
            }
        }
    }

    public void dijkstraForLandemerkeOmvendt(Node fra) {
        PriorityQueue<Node> kø = new PriorityQueue<>((a, b) -> a.tid - b.tid);
        fra.tid = 0;
        kø.add(fra);
        while (!kø.isEmpty()) {
            Node n = kø.poll();
            for (Kant k : n.omvendteKanter) {
                if (k.tilNode.tid > n.tid + k.kjoretid) {
                    kø.remove(k.tilNode);
                    k.tilNode.tid = n.tid + k.kjoretid;
                    kø.add(k.tilNode);
                }
            }
        }
    }

    public int ALT(Node fra, Node til) {
        PriorityQueue<Node> kø = new PriorityQueue<>((a, b) -> (a.estimat + a.tid) - (b.estimat + b.tid));
        fra.tid = 0;
        for (int i = 0; i < antLandemerker; i++) {
            int diff1 = avstanderFraLandemerker[til.nodeNr][i] - avstanderFraLandemerker[fra.nodeNr][i];
            int diff2 = avstanderTilLandemerker[fra.nodeNr][i] - avstanderTilLandemerker[til.nodeNr][i];
            if (diff1 > fra.estimat) {
                fra.estimat = diff1;
            } else if (diff2 > fra.estimat) {
                fra.estimat = diff2;
            }
        }
        kø.add(fra);
        fra.iKø = true;
        til.endePunkt = true;
        int cnt = 0;
        while (!kø.isEmpty()) {
            Node n = kø.poll();
            cnt++;
            if (n.endePunkt) return cnt;
            for (Kant k : n.kanter) {
                if (k.tilNode.tid > n.tid + k.kjoretid) {
                    if (k.tilNode.iKø) {
                        kø.remove(k.tilNode);
                    }
                    k.tilNode.tid = n.tid + k.kjoretid;
                    k.tilNode.forgjenger = n;
                    k.tilNode.iKø = true;
                    if (k.tilNode.estimat == -1) {
                        for (int i = 0; i < antLandemerker; i++) {
                            int diff1 = avstanderFraLandemerker[til.nodeNr][i] - avstanderFraLandemerker[k.tilNode.nodeNr][i];
                            int diff2 = avstanderTilLandemerker[k.tilNode.nodeNr][i] - avstanderTilLandemerker[til.nodeNr][i];
                            if (diff1 > k.tilNode.estimat) {
                                k.tilNode.estimat = diff1;
                            } else if (diff2 > k.tilNode.estimat) {
                                k.tilNode.estimat = diff2;
                            }
                        }
                        if (k.tilNode.estimat < 0) {
                            k.tilNode.estimat = 0;
                        }
                    }
                    kø.add(k.tilNode);
                }
            }
        }
        return -1;
    }

    public static void kjørDjikstra(Graf g, int nodeFra, int nodeTil) {
        Node start = g.noder[nodeFra];
        Node mål = g.noder[nodeTil];
        Date startTid = new Date();
        int cnt = g.dijkstra(start, mål);
        Date sluttTid = new Date();
        int sek = mål.tid / 100;
        int s = sek % 60;
        int h = sek / 60;
        int m = h % 60;
        h = h / 60;
        System.out.println("Djikstra:");
        System.out.println("Ant noder undersøkt: " + cnt);
        System.out.println("Tid fra " + start.navn + " til " + mål.navn + ": " + h + " timer, " + m + " minutter, " + s + " sekunder");
        System.out.println("Tid brukt på algorimten: " + (sluttTid.getTime() - startTid.getTime()) + " ms");
        skrivRuteTilFil("Output/" + start.navn + mål.navn + "Djik.csv", start, mål);
    }

    public static void kjørDjikstraForInteressepunkt(Graf g, int nodeFra, int type, int antall) {
        String punkt = "";
        if (type == 1) punkt = "stedsnavn";
        else if (type == 2) punkt = "bensinstasjoner";
        else if (type == 4) punkt = "ladestasjoner";
        else if (type == 8) punkt = "spisesteder";
        else if (type == 16) punkt = "drikkesteder";
        else if (type == 32) punkt = "overnattingssteder";

        Node start = g.noder[nodeFra];
        Date startTid = new Date();
        Node[] interessepunkter = g.dijkstraForInteressepunkt(start, type, antall);
        Date sluttTid = new Date();
        System.out.println("Interessepunkt:");
        System.out.println(antall + " nærmeste " + punkt + " fra " + start.navn);
        for (Node n : interessepunkter) {
            int sek = n.tid / 100;
            int s = sek % 60;
            int h = sek / 60;
            int m = h % 60;
            h = h / 60;
            System.out.println(n.navn + ": " + h + " timer, " + m + " minutter, " + s + " sekunder unna");
        }
        System.out.println("Tid brukt på algorimten: " + (sluttTid.getTime() - startTid.getTime()) + " ms");
        skrivStederTilFil("Output/" + punkt + "Fra" + start.navn + ".csv" , interessepunkter);
    }

    public static void kjørALT(Graf g, int nodeFra, int nodeTil) {
        Node start = g.noder[nodeFra];
        Node mål = g.noder[nodeTil];
        Date startTid = new Date();
        int cnt = g.ALT(start, mål);
        Date sluttTid = new Date();
        int sek = mål.tid / 100;
        int s = sek % 60;
        int h = sek / 60;
        int m = h % 60;
        h = h / 60;
        System.out.println("ALT:");
        System.out.println("Ant noder undersøkt: " + cnt);
        System.out.println("Tid fra " + start.navn + " til " + mål.navn + ": " + h + " timer, " + m + " minutter, " + s + " sekunder");
        System.out.println("Tid brukt på algorimten: " + (sluttTid.getTime() - startTid.getTime()) + " ms");
        skrivRuteTilFil("Output/" + start.navn + mål.navn + "ALT.csv", start, mål);
    }

    public static void skrivRuteTilFil(String filnavn, Node start, Node mål) {
        try {
            FileWriter writer = new FileWriter(new File(filnavn));
            writer.write("Breddegrad, Lengdegrad\n");
            writer.write(mål.breddegrad + "," + mål.lengdegrad + "\n");
            Node tmp = mål;
            while(tmp.forgjenger != null && tmp.forgjenger != start) {
                tmp = tmp.forgjenger;
                writer.write(tmp.breddegrad + "," + tmp.lengdegrad + "\n");
            }
            writer.write(start.breddegrad + "," + start.lengdegrad + "\n");
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void skrivStederTilFil(String filnavn, Node[] steder) {
        try {
            FileWriter writer = new FileWriter(new File(filnavn));
            writer.write("Breddegrad, Lengdegrad\n");
            for (Node n : steder) {
                writer.write(n.breddegrad + "," + n.lengdegrad + "\n");
            }
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void nullstillNoder(Graf g) {
        for (Node n : g.noder) {
            n.tid = 1000000000;
            n.estimat = -1;
            n.forgjenger = null;
            n.iKø = false;
            n.endePunkt = false;
        }
    }

    public void nullstillNoder() {
        for (Node n : noder) {
            n.tid = 1000000000;
            n.estimat = -1;
            n.forgjenger = null;
            n.iKø = false;
            n.endePunkt = false;
        }
    }
}