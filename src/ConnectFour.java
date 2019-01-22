import java.util.Scanner;

enum GameOver {
    NOT_GAME_OVER,
    P1_WON,
    P2_WON,
    DRAW,
}

class ConnectFour {
    private boolean isRunning;

    private Scanner scanner;

    private String name1;
    private String name2;

    private boolean p1Turns;

    private static final int WIDTH = 7;
    private static final int HEIGHT = 6;
    private static final char P1_CHAR = 'X';
    private static final char P2_CHAR = 'O';
    private static final char EMPTY_CHAR = ' ';
    private char[] fields = new char[ WIDTH * HEIGHT ];

    ConnectFour() {
        isRunning = true;
        p1Turns = true;
        scanner = new Scanner( System.in );
        getPlayerNames();
        clearFields();
    }

    private void clearFields() {
        for( int i = 0; i < fields.length; i++ )
            fields[i] = EMPTY_CHAR;
    }

    void run() {
        GameOver winner = GameOver.NOT_GAME_OVER;
        while( this.isRunning ) {
            this.render();
            this.update();
            winner = this.testGameOver();
            if( winner != GameOver.NOT_GAME_OVER )
                isRunning = false;
        }
        displayWinMessage( winner );
    }

    private void displayWinMessage( GameOver winner ) {
        render();
        if( winner == GameOver.DRAW ) {
            System.out.println( "IT'S A DRAW!" );
            return;
        }
        System.out.printf( "%s WON!",  ( winner == GameOver.P1_WON ) ? name1 : name2 );
    }

    private GameOver testGameOver() {
        for( int y = 0; y < HEIGHT; y++ )
            for( int x = 0; x < WIDTH; x++ )
                if( fields[getIndex( x, y )] != EMPTY_CHAR )
                    if( testHorizontal( x, y ) || testVertical( x, y ) || testUpDiagonal( x, y ) || testDownDiagonal( x, y ) )
                        return ( fields[getIndex( x, y )] == ConnectFour.P1_CHAR ) ? GameOver.P1_WON : GameOver.P2_WON;
        return GameOver.NOT_GAME_OVER;
    }

    private boolean testHorizontal( int x, int y ) {
        if( x > WIDTH - 4 )
            return false;
        return fields[getIndex( x, y )] == fields[getIndex( x + 1, y )] &&
               fields[getIndex( x, y )] == fields[getIndex( x + 2, y )] &&
               fields[getIndex( x, y )] == fields[getIndex( x + 3, y )];
    }

    private boolean testVertical( int x, int y ) {
        if( y > HEIGHT - 4 )
            return false;
        return fields[getIndex( x, y )] == fields[getIndex( x, y + 1 )] &&
               fields[getIndex( x, y )] == fields[getIndex( x, y + 2 )] &&
               fields[getIndex( x, y )] == fields[getIndex( x, y + 3 )];
    }

    private boolean testUpDiagonal( int x, int y ) {
        if( y > HEIGHT - 4  || x > WIDTH - 4 )
            return false;
        return fields[getIndex( x, y )] == fields[getIndex( x + 1, y + 1 )] &&
                fields[getIndex( x, y )] == fields[getIndex( x + 2, y + 2 )] &&
                fields[getIndex( x, y )] == fields[getIndex( x + 3, y + 3 )];

    }

    private boolean testDownDiagonal( int x, int y ) {
        if( y < 3  || x > WIDTH - 4 )
            return false;
        return fields[getIndex( x, y )] == fields[getIndex( x + 1, y - 1 )] &&
               fields[getIndex( x, y )] == fields[getIndex( x + 2, y - 2 )] &&
               fields[getIndex( x, y )] == fields[getIndex( x + 3, y - 3 )];

    }

    private void getPlayerNames() {
        String prompt = "Player %d, what is your name? ";
        System.out.printf( prompt, 1 );
        this.name1 = scanner.nextLine();
        System.out.printf( prompt, 2 );
        this.name2 = scanner.nextLine();
    }

    private int getX() {
        String turnPrompt = "%s, where do you want to place your stone?\n";
        System.out.printf( turnPrompt, (p1Turns) ? name1 : name2 );
        return scanner.nextInt() - 1;
    }

    private void update() {
        int x;
        int y;
        do {
            do {
                x = getX();
            }
            while( x < 0 || x > WIDTH - 1 );
            y = findLowestY( x );
        }
        while( y < 0 || y > HEIGHT - 1 );
        fields[getIndex( x, y )] = (p1Turns) ? P1_CHAR : P2_CHAR;
        p1Turns = !p1Turns;
    }

    private int findLowestY( int x ) {
        for( int y = HEIGHT - 1; y >= 0; y-- )
            if( fields[getIndex( x, y )] == EMPTY_CHAR )
                return y;
        return -1;
    }

    private int getIndex( int x, int y ) {
        return y * WIDTH + x;
    }

    private void render() {
        clearScreen();
        printLines();
        printField();
        printLines();
        printNumbers();
    }

    private void printField() {
        for( int y = 0; y < HEIGHT; y++ ) {
            System.out.print( '|' );
            for( int x = 0; x < WIDTH; x++ ) {
                System.out.print( fields[getIndex( x, y )] );
                System.out.print( '|' );
            }
            System.out.println();
        }
    }

    private void printLines() {
        for( int x = 0; x < (2 * WIDTH) + 1; x++ )
            System.out.print( '-' );
        System.out.println();
    }

    private void printNumbers() {
        System.out.print( ' ' );
        for( int x = 0; x < WIDTH; x++ ) {
            System.out.print( x + 1 );
            System.out.print( ' ' );
        }
        System.out.println();
    }

    private static void clearScreen() {
        for( int i = 0; i < 100; i++ )
            System.out.println();
    }
}
