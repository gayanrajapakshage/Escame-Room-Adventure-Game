package AdventureModel.PowerUps;

import AdventureModel.Player;

public interface PowerUp {
    public double getCost();
    public void activate(Player player);
    public String getText();
}
