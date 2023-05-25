package ro.pub.cs.systems.eim.model2colocviudesen;

public class Pokemon {
    private final String powers;
    private final String url;
    private final String abilities;


    public Pokemon(String powers, String url, String abilities) {
        this.powers = powers;
        this.url = url;
        this.abilities = abilities;
    }

    public String getPowers() {
        return powers;
    }

    public String getUrl() {
        return url;
    }

    public String getAbilities() {
        return abilities;
    }
}
