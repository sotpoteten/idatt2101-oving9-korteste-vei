import java.util.ArrayList;

public class Node {
    double breddegrad;
    double lengdegrad;
    int nodeNr;
    public Node forgjenger = null;
    public ArrayList<Kant> kanter = new ArrayList<>();
    public ArrayList<Kant> omvendteKanter = new ArrayList<>();
    public int tid = 1000000000;
    public int estimat = -1;
    public boolean endePunkt = false;
    public boolean iKø = false;
    public int kode = -1;
    public String navn = "";

    public Node(int nodeNr, double breddegrad, double lengdegrad) {
        this.nodeNr = nodeNr;
        this.breddegrad = breddegrad;
        this.lengdegrad = lengdegrad;
    }

    public void leggTilOmvendtKant(Kant k) {
        omvendteKanter.add(k);
    }
}
