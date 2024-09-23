import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.HashSet;


public class Tile {
    private Rectangle tile;         // Representación gráfica de la ficha
    private boolean hasGlue;        // Si la ficha tiene pegamento
    private ArrayList<Tile> connectedTiles; // Fichas conectadas con pegamento
    private boolean moved;          // Indica si la ficha se ha movido
    private int x;                  // Coordenada X de la ficha en el tablero
    private int y;                     // Coordenada Y de la ficha en el tablero
    private Map<Tile, List<Tile>> adjList;

    /**
     * Constructor de la clase Tile
     *
     * @param color Color de la ficha
     */
    public Tile(String color) {
        this.tile = new Rectangle();
        this.tile.changeSize(40, 40);
        this.tile.changeColor(color);
        this.hasGlue = false;
        this.connectedTiles = new ArrayList<>();
        this.moved = false;
    }

    /**
     * Establece las coordenadas (x, y) de la ficha y mueve el objeto gráfico a esa posición.
     *
     * @param x Coordenada X en el tablero
     * @param y Coordenada Y en el tablero
     */
    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
        tile.moveHorizontal(x * 50);
        tile.moveVertical(y * 50);
    }

    public void addGlue(Tile[][] board) {
        hasGlue = true;
        connectAdjacentTiles(board);
    }

    public void createGraph(Tile[][] board) {
        adjList = new HashMap<>();
        buildGraph(board);
    }

    private void connectAdjacentTiles(Tile[][] board) {
        int rows = board.length;
        int cols = board[0].length;

        // Conectar con las fichas arriba, abajo, izquierda y derecha
        connectIfValid(board, x - 1, y); // arriba
        connectIfValid(board, x + 1, y); // abajo
        connectIfValid(board, x, y - 1); // izquierda
        connectIfValid(board, x, y + 1); // derecha
    }

    private void connectIfValid(Tile[][] board, int i, int j) {
        if (i >= 0 && i < board.length && j >= 0 && j < board[0].length && board[i][j] != null) {
            Tile neighbor = board[i][j];
            if (neighbor != null && !connectedTiles.contains(neighbor)) {
                connectedTiles.add(neighbor);
                neighbor.connectedTiles.add(this); // Conexión bidireccional
            }
        }
    }

    private void buildGraph(Tile[][] board) {
        int height = board.length;
        int width = board[0].length;

        // Crear nodos y adyacencias
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Tile current = board[i][j];
                if (current != null) {
                    adjList.putIfAbsent(current, new ArrayList<>());

                    // Conexiones iniciales con vecinos
                    addNeighbor(board, current, i - 1, j); // Ficha arriba
                    addNeighbor(board, current, i + 1, j); // Ficha abajo
                    addNeighbor(board, current, i, j - 1); // Ficha izquierda
                    addNeighbor(board, current, i, j + 1); // Ficha derecha
                }
            }
        }
    }

    private void addNeighbor(Tile[][] board, Tile current, int i, int j) {
        if (i >= 0 && i < board.length && j >= 0 && j < board[0].length && board[i][j] != null) {
            adjList.get(current).add(board[i][j]);
        }
    }

    public void addGlued(Tile current, String direction, Tile[][] board) {
        int x = current.getX();
        int y = current.getY();
        Tile neighbor = null;

        switch (direction.toLowerCase()) {
            case "up":
                if (x > 0) neighbor = board[x - 1][y];
                break;
            case "down":
                if (x < board.length - 1) neighbor = board[x + 1][y];
                break;
            case "left":
                if (y > 0) neighbor = board[x][y - 1];
                break;
            case "right":
                if (y < board[0].length - 1) neighbor = board[x][y + 1];
                break;
            default:
                System.out.println("Dirección no válida");
                return;
        }

        if (neighbor != null) {
            // Agregar conexión entre las dos fichas
            adjList.get(current).add(neighbor);
            adjList.get(neighbor).add(current);
        } else {
            System.out.println("No hay ficha en la dirección especificada.");
        }
    }

    public Set<Tile> findConnectedComponent() {
        Set<Tile> visited = new HashSet<>();
        Stack<Tile> stack = new Stack<>();
        stack.push(this);

        while (!stack.isEmpty()) {
            Tile current = stack.pop();
            if (!visited.contains(current)) {
                visited.add(current);
                for (Tile neighbor : current.connectedTiles) {
                    stack.push(neighbor);
                }
            }
        }
        return visited; // Todas las fichas conectadas con esta ficha
    }


    /**
     * Muestra la ficha en la interfaz gráfica.
     */
    public void makeVisible() {
        tile.makeVisible();
    }

    public void makeInvisible() {
        tile.makeInvisible();
    }

    public void moveHorizontal(int distance) {
        tile.moveHorizontal(distance);
    }

    /**
     * Añade pegamento a la ficha y busca fichas adyacentes para conectarlas.
     */
    public boolean hasGlue() {
        return hasGlue;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * Mueve la ficha y las fichas conectadas si hay pegamento.
     *
     * @param x Nueva coordenada X
     * @param y Nueva coordenada Y
     */
    public void move(int dx, int dy) {
        if (hasGlue) {
            // Mueve todas las fichas conectadas
            Set<Tile> connectedComponent = findConnectedComponent();
            for (Tile tile : connectedComponent) {
                int newX = tile.getX() + dx;
                int newY = tile.getY() + dy;
                tile.setXY(newX, newY); // Mueve cada ficha conectada
            }
        } else {
            // Mueve solo la ficha individual
            setXY(x + dx, y + dy);
        }
    }

    public void printGraph() {
        for (int i = 0; i < adjList.size(); i++) {
            System.out.println("Adjacency list of vertex " + i);
            System.out.print(i + " is connected to: ");
            for (Tile v : adjList.get(i)) {
                System.out.print(v + " ");
            }
            System.out.println();
        }
    }

    public String getColor() {
        return tile.getColor();
    }

    public boolean getMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }
}


