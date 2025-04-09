package ui;

public interface ClientObject {
    public String help();
    public String eval(String line);
    public boolean getPost();
    public boolean getPre();
    public boolean getGame();
    public void connectAuthToken(String authToken);
    public void setObserve(Boolean observe);
    public boolean isObserved();
}
