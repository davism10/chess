package ui;

public interface ClientObject {
    public String help();
    public String eval(String line);
    public boolean getPost();
    public boolean getPre();
    public boolean getGame();
}
