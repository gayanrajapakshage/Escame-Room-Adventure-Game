package AdventureModel;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class PlayerStatusObserver {
    private Player player;
    private VBox statusSubscriber;
    private ImageView rep50_80;
    private ImageView rep50;
    private ImageView rep80;

    /**
     * PlayerStatusObserver Constructor
     * ----------------------------------
     *
     * @param player the player being observed
     * @param playerStatusView the VBox in the game model being observed
     * @param rep50_80 the icon to use for reputation label when 50 <= rep <= 80
     * @param rep50 the icon to use for reputation label when rep < 50
     */
    public PlayerStatusObserver(Player player, VBox playerStatusView, ImageView rep50_80,
                                ImageView rep50, ImageView rep80){
        this.player = player;
        this.statusSubscriber = playerStatusView;
        this.rep50_80 = rep50_80;
        this.rep50 = rep50;
        this.rep80 = rep80;
    }


    /**
     * PlayerStatusObserver constructor with no parameters
     * ------------------------------------------------------------------------
     */
    public PlayerStatusObserver(){

    }

    /**
     * This method update the GUI with player's current status, returns nothing
     * It should be called whenever the player's health, reputation, and coins
     * could be changed.
     * ------------------------------------------------------------------------
     */
    public void update(){
        for (Node n : statusSubscriber.getChildren()){
            if (n.getId().equals("coinLabel")){
                ((Label) n).setText(String.valueOf(this.player.getCoins()));
            } else if (n.getId().equals("healthBar")){
                double widthH = this.player.getHealth()/100 *140;
                if (this.player.getHealth() <= 0){
                    widthH = 0;
                }
                ((Rectangle) n).setWidth(widthH);
            } else if (n.getId().equals("repBar")){
                double widthR = this.player.getRep()/100 *140;
                if (this.player.getRep() <= 0){
                    widthR = 0;
                }
                ((Rectangle) n).setWidth(widthR);
            } else if (n.getId().equals("repLabel")) {
                if (this.player.getRep() < 80 && this.player.getRep() >= 50){
                    ((Label) n).setGraphic(this.rep50_80);
                } else if (this.player.getRep() < 50){
                    ((Label) n).setGraphic(this.rep50);
                } else if (this.player.getRep() > 80){
                    ((Label) n).setGraphic(this.rep80);
                }
                ((Label) n).setText(this.player.getRep() + "/100");
                if (this.player.getRep() < 0){
                    ((Label) n).setText("0/100");
                }
            } else if (n.getId().equals("healthLabel")) {
                ((Label) n).setText(this.player.getHealth() + "/100");
                if (this.player.getHealth() < 0){
                    ((Label) n).setText("0/100");
                }
            }
        }
    }

}
