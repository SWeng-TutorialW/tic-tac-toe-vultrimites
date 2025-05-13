package il.cshaifasweng.OCSFMediatorExample.entities;
import java.io.Serializable;

public class gameMove implements Serializable{

    private static final long serialVersionUID = 1L;
    private int row;
    private int col;
    private String playerSymbol;    // X or O

    public gameMove(int row, int col, String playerSymbol) {
        this.row = row;
        this.col = col;
        this.playerSymbol = playerSymbol;
    }

    public int getRow() {
        return row;
    }

    public int getCol(){
        return col;
    }

    public String getPlayerSymbol() {
        return playerSymbol;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setPlayerSymbol(String playerSymbol) {
        this.playerSymbol = playerSymbol;
    }
}