import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class Puzzle {

    private int height;
    private int width;
    private Tile[][] board;
    private Tile[][] lastBoard;
    private String[] order;
    private boolean[][] hole;

    /**
     * Constructor de la clase Puzzle.
     * Crea dos tableros con el mismo tamaño y configura sus pwosiciones iniciales.
     */
    public Puzzle(int height, int width) {
        this.height = height;
        this.width = width;
        this.board = new Tile[height][width];
        this.lastBoard = new Tile[height][width];
        this.hole = new boolean[height][width];
        createBoards();
    }

    public Puzzle(String[] ending){
        height = ending.length;
        width = ending[0].length();
        createlastBoardByOrder(ending);
        createBoardsAutomatically();
    }

    public Puzzle(String[] starting, String[] ending) {
        height = ending.length;
        width = ending[0].length();
        createlastBoardByOrder(ending);
        createBoardByOrder(starting);
    }


    /**
     * Muestra ambos tableros de forma visible, incluyendo las fichas y los espacios vacíos.
     */
    public void makeVisible() {
        int x = 0;
        int y = 0;
        background(height, width);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                // Mostrar fichas del tablero principal
                if (board[i][j] != null) {
                    board[i][j].makeVisible();
                } else {
                    board[i][j] = null;
                }

                // Mostrar fichas del último tablero
                if (lastBoard[i][j] != null) {
                    lastBoard[i][j].makeVisible();
                } else {
                    lastBoard[i][j] = null;
                }
            }
        }
    }

    private void createlastBoardByOrder(String[] cadena) {
        int numFilas = cadena.length;
        int numColumnas = cadena[0].length();
        this.lastBoard = new Tile[numFilas][numColumnas];

        for (int i = 0; i < numFilas; i++) {
            for (int j = 0; j < numColumnas; j++) {
                char c = cadena[i].charAt(j);
                // Configuración del tablero principal (board)
                switch (c) {
                    case 'r':
                        lastBoard[i][j] = new Tile("red");
                        break;
                    case 'b':
                        lastBoard[i][j] = new Tile("blue");
                        break;
                    case 'g':
                        lastBoard[i][j] = new Tile("green");
                        break;
                    case 'y':
                        lastBoard[i][j] = new Tile("yellow");
                        break;
                    case '.':
                        lastBoard[i][j] = null; // Espacio vacío
                        break;
                    default:
                        throw new IllegalArgumentException("Carácter no reconocido: " + c);
                }

                if (lastBoard[i][j] != null) {
                    lastBoard[i][j].setXY(j, i); // Coordenadas para lastBoard
                    lastBoard[i][j].moveHorizontal(500); // Desplazar el segundo tablero
                }
            }
        }
    }

    private void createBoardByOrder(String[] cadena) {
        int numFilas = cadena.length;
        int numColumnas = cadena[0].length();
        this.board = new Tile[numFilas][numColumnas];

        for (int i = 0; i < numFilas; i++) {
            for (int j = 0; j < numColumnas; j++) {
                char c = cadena[i].charAt(j);

                // Configuración del tablero principal (board)
                switch (c) {
                    case 'r':
                        board[i][j] = new Tile("red");
                        break;
                    case 'b':
                        board[i][j] = new Tile("blue");
                        break;
                    case 'g':
                        board[i][j] = new Tile("green");
                        break;
                    case 'y':
                        board[i][j] = new Tile("yellow");
                        break;
                    case '.':
                        board[i][j] = null; // Espacio vacío
                        break;
                    default:
                        throw new IllegalArgumentException("Carácter no reconocido: " + c);
                }

                if (board[i][j] != null) {
                    board[i][j].setXY(i, j); // Coordenadas para lastBoard
                }
            }
        }
    }



    private void background(int height, int width){
        Rectangle background = new Rectangle();
        Rectangle backgroundS = new Rectangle();
        background.changeSize(width * 51, height * 51);
        backgroundS.changeSize(width * 51, height * 51);
        background.changeColor("Grey");
        backgroundS.changeColor("Grey");
        background.makeVisible();
        background.moveTo(62,14);
        backgroundS.moveTo(565,14);
        backgroundS.makeVisible();
    }
    /**
     * Crea los tableros con fichas de colores y espacios vacíos.
     */
    private void createBoards() {
        String[] colors = {"red", "yellow", "blue", "green"};
        int totalTiles = height * width;
        int colorTiles = totalTiles / 2;
        int emptyTiles = totalTiles - colorTiles;

        ArrayList<Tile> tiles = new ArrayList<>();
        ArrayList<Tile> tilesFinal = new ArrayList<>();

        // Crear fichas de colores para ambos tableros
        for (int i = 0; i < colorTiles; i++) {
            Tile tile = new Tile(colors[i % 4]);
            Tile tileFinal = new Tile(colors[i % 4]);
            tiles.add(tile);
            tilesFinal.add(tileFinal);
        }

        // Añadir espacios vacíos
        for (int i = 0; i < emptyTiles; i++) {
            tiles.add(null);
            tilesFinal.add(null);
        }

        // Mezclar las fichas
        Collections.shuffle(tiles);
        Collections.shuffle(tilesFinal);

        // Asignar las fichas a ambos tableros
        int temp = 0;
        int tempF = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                board[i][j] = tiles.get(temp++);
                lastBoard[i][j] = tilesFinal.get(tempF++);
                if (board[i][j] != null) {
                    board[i][j].setXY(i, j);
                }
                if (lastBoard[i][j] != null) {
                    lastBoard[i][j].setXY(i, j);
                    lastBoard[i][j].moveHorizontal(500);
                }
            }
        }
    }

    private void createBoardsAutomatically() {
        String[] colors = {"red", "yellow", "blue", "green"};
        int totalTiles = height * width;
        int colorTiles = totalTiles / 2;
        int emptyTiles = totalTiles - colorTiles;

        ArrayList<Tile> tiles = new ArrayList<>();

        // Crear fichas de colores para el tablero `board`
        for (int i = 0; i < colorTiles; i++) {
            Tile tile = new Tile(colors[i % 4]);
            tiles.add(tile);
        }

        // Añadir espacios vacíos
        for (int i = 0; i < emptyTiles; i++) {
            tiles.add(null);
        }

        // Mezclar las fichas
        Collections.shuffle(tiles);

        // Asignar las fichas al tablero `board`
        int temp = 0;
        this.board = new Tile[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                board[i][j] = tiles.get(temp++);
                if (board[i][j] != null) {
                    board[i][j].setXY(i, j); // Coordenadas para el tablero principal
                }
            }
        }
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void setTile(int row, int column, Tile tile) {
        if (row >= 0 && row < height && column >= 0 && column < width) {
            board[row][column] = tile;  // Insertar la ficha en el tablero
        }
    }

    public void addTile(int row, int column,String color){
        if (row >= 0 && row < height && column >= 0 && column < width) {
            // Crear una nueva ficha (Tile) con el color dado
            Tile newTile = new Tile(color);

            // Establecer las coordenadas de la nueva ficha
            newTile.setXY(row, column);

            // Añadir la ficha al tablero
            setTile(row, column, newTile);

            // Hacer visible la nueva ficha en la interfaz gráfica
            newTile.makeVisible();
        } else {
            System.out.println("Posición fuera de los límites del tablero.");
        }
    }

    public void deleteTile(int row, int column) {
        // Verificar que la posición esté dentro de los límites del tablero
        if (row >= 0 && row < height && column >= 0 && column < width) {
            Tile tile = getTile(row, column);
            if (tile != null) {
                board[row][column] = null;// Eliminar la ficha del tablero
                tile.makeInvisible();
            }
        }
    }
    public void relocateTile(int oldRow,int oldColumn, int newRow, int newColumn) {
        // Verificar que las posiciones estén dentro de los límites del tablero
        if (oldRow >= 0 && oldRow < height && oldColumn >= 0 && oldColumn < width &&
                newRow >= 0 && newRow < height && newColumn >= 0 && newColumn < width) {

            Tile tile = getTile(oldRow, oldColumn);
            if (tile != null && board[newRow][newColumn] == null) {
                // Mover la ficha al nuevo lugar en el tablero
                deleteTile(oldRow,oldColumn);
                String color = tile.getColor();
                addTile(newRow,newColumn,color);

                // Actualizar la posición gráfica de la ficha
                // tile.setXY(newRow, newColumn);
            }
        }
    }

    public void tilt(char direction) {
        switch (direction) {
            case 'u': // Mover hacia arriba
                for (int row = 0; row < height; row++) {
                    for (int col = 0; col < width; col++) {
                        Tile tile = getTile(row, col);
                        if (tile != null) {
                            moveTileUp(row, col); // Mueve ficha hacia arriba si es posible
                        }
                    }
                }
                break;
            case 'd': // Mover hacia abajo
                for (int row = height-1; row >= 0; row--) {
                    for (int col = 0; col < width; col++) {
                        Tile tile = getTile(row, col);
                        if (tile != null) {
                            moveTileDown(row, col); // Mueve ficha hacia abajo si es posible
                        }
                    }
                }
                break;
            case 'l': // Mover hacia la izquierda
                for (int col = 0; col < width; col++) {
                    for (int row = 0; row < height; row++) {
                        Tile tile = getTile(row, col);
                        if (tile != null) {
                            moveTileLeft(row, col); // Mueve ficha hacia la izquierda si es posible
                        }
                    }
                }
                break;
            case 'r': // Mover hacia la derecha
                for (int col = width-1; col >= 0; col--) {
                    for (int row = 0; row < height; row++) {
                        Tile tile = getTile(row, col);
                        if (tile != null) {
                            moveTileRight(row, col); // Mueve ficha hacia la izquierda si es posible
                        }
                    }
                }
                break;
            default:
                System.out.println("Dirección no válida");
        }
    }

    private void moveTileUp(int fila, int columna) {
        for (int i = fila-1;i >= 0;i--){
            if (hole[i][columna]) {
                deleteTile(fila, columna);
                break;
            }
            if (i == 0){
                relocateTile(fila, columna, i, columna);
                break;
            }
            Tile tile = getTile(i-1, columna);
            if (tile != null && !hole[i-1][columna]) {
                relocateTile(fila, columna, i, columna);
                break;
            }

        }
    }

    private void moveTileDown(int fila, int columna) {
        for (int i = fila+1;i < height;i++){
            if (hole[i][columna]) {
                deleteTile(fila, columna);
                break;
            }
            if (i == height-1){
                relocateTile(fila, columna, i, columna);
                break;
            }
            Tile tile = getTile(i+1, columna);
            if (tile != null && !hole[i+1][columna]) {
                relocateTile(fila, columna, i, columna);
                break;
            }
        }
    }

    private void moveTileLeft(int row, int col) {
        for (int j = col-1; j >= 0; j--) {
            if (hole[row][j]) {
                deleteTile(row, col);
                break;
            }
            if (j == 0) {
                relocateTile(row, col, row, j);
                break;
            }
            Tile tile = getTile(row, j-1);
            if (tile != null && !hole[row][j-1]) {
                relocateTile(row, col, row, j);
                break;
            }
        }
    }

    private void moveTileRight(int row, int col) {
        for (int j = col+1; j < width; j++) {
            if (hole[row][j]) {
                deleteTile(row, col);
                break;
            }
            if (j == width-1) {
                relocateTile(row, col, row, j);
                break;
            }
            Tile tile = getTile(row, j+1);
            if (tile != null && !hole[row][j+1]) {
                relocateTile(row, col, row, j);
                break;
            }
        }
    }


    public void makeHole(int row, int col) {
        if (row > height || row < 0 || col > width || col < 0 || hole[row][col]) return;
        hole[row][col] = true;
        Tile tile = getTile(row, col);
        if (tile != null) {
            deleteTile(row, col);
        }
        addTile(row, col, "white");
    }


    /**
     * Añade pegamento a una ficha en el tablero principal en la posición dada.
     */
    public void addGlue(int x, int y) {
        if (x >= 0 && x < height && y >= 0 && y < width) {
            if (board[x][y] != null) {
                board[x][y].addGlue(board);
            }
        }
    }

    public Tile getTile(int x, int y) {
        if (x >= 0 && x < height && y >= 0 && y < width) {
            return board[x][y];
        }
        return null;
    }
}

