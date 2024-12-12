package spaceinvaders;

public final class App {
    private App() {
    }

    public static void main(String[] args) {
        GameWindow frame = new GameWindow();
        frame.open();
    }
}
