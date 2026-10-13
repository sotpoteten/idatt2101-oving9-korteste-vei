public class Kant {
    Node fraNode;
    Node tilNode;
    int kjoretid;
    int lengde;
    int fartgrense;

    public Kant (Node fraNode, Node tilNode, int kjoretid) {
        this.fraNode = fraNode;
        this.tilNode = tilNode;
        this.kjoretid = kjoretid;
    }

    public Kant (Node fraNode, Node tilNode, int kjoretid, int lengde) {
        this.fraNode = fraNode;
        this.tilNode = tilNode;
        this.kjoretid = kjoretid;
        this.lengde = lengde;
    }

    public Kant (Node fraNode, Node tilNode, int kjoretid, int lengde, int fartgrense) {
        this.fraNode = fraNode;
        this.tilNode = tilNode;
        this.kjoretid = kjoretid;
        this.lengde = lengde;
        this.fartgrense = fartgrense;
    }
}
