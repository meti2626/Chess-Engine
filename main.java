import java.util.*;

public class ChessEngine {
    static char[][] board;

    public static void main(String[] args) {
        initializeBoard();
        printBoard();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Your move (e.g., e2e4): ");
            String move = scanner.nextLine();
            makeMove(move);
            printBoard();

            System.out.println("AI thinking...");
            String aiMove = findBestMove(3); // depth-3 minimax
            System.out.println("AI plays: " + aiMove);
            makeMove(aiMove);
            printBoard();
        }
    }

    static void initializeBoard() {
        board = new char[][] {
            {'r','n','b','q','k','b','n','r'},
            {'p','p','p','p','p','p','p','p'},
            {'.','.','.','.','.','.','.','.'},
            {'.','.','.','.','.','.','.','.'},
            {'.','.','.','.','.','.','.','.'},
            {'.','.','.','.','.','.','.','.'},
            {'P','P','P','P','P','P','P','P'},
            {'R','N','B','Q','K','B','N','R'}
        };
    }

    static void printBoard() {
        for (int i = 0; i < 8; i++) {
            System.out.print((8 - i) + " ");
            for (int j = 0; j < 8; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h");
    }

    static void makeMove(String move) {
        int fromRow = 8 - Character.getNumericValue(move.charAt(1));
        int fromCol = move.charAt(0) - 'a';
        int toRow = 8 - Character.getNumericValue(move.charAt(3));
        int toCol = move.charAt(2) - 'a';

        board[toRow][toCol] = board[fromRow][fromCol];
        board[fromRow][fromCol] = '.';
    }

    static String findBestMove(int depth) {
        int bestScore = Integer.MIN_VALUE;
        String bestMove = "";
        List<String> moves = generateMoves(true); // AI = white

        for (String move : moves) {
            char[][] tempBoard = copyBoard();
            makeMove(move);
            int score = minimax(depth - 1, false);
            board = tempBoard;

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        return bestMove;
    }

    static int minimax(int depth, boolean isMaximizing) {
        if (depth == 0) return evaluateBoard();

        List<String> moves = generateMoves(isMaximizing);
        int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (String move : moves) {
            char[][] tempBoard = copyBoard();
            makeMove(move);
            int score = minimax(depth - 1, !isMaximizing);
            board = tempBoard;

            if (isMaximizing)
                bestScore = Math.max(score, bestScore);
            else
                bestScore = Math.min(score, bestScore);
        }

        return bestScore;
    }

    static List<String> generateMoves(boolean white) {
        List<String> moves = new ArrayList<>();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                char piece = board[r][c];
                if (piece == '.') continue;

                if (Character.isUpperCase(piece) != white) continue;

                // Basic example: only pawns
                if (Character.toLowerCase(piece) == 'p') {
                    int dir = white ? -1 : 1;
                    int nextR = r + dir;
                    if (nextR >= 0 && nextR < 8 && board[nextR][c] == '.') {
                        moves.add(toMoveString(r, c, nextR, c));
                    }
                }

                // Expand this to handle more pieces...
            }
        }
        return moves;
    }

    static int evaluateBoard() {
        int score = 0;
        for (char[] row : board) {
            for (char piece : row) {
                score += getPieceValue(piece);
            }
        }
        return score;
    }

    static int getPieceValue(char piece) {
        switch (Character.toLowerCase(piece)) {
            case 'p': return Character.isUpperCase(piece) ? 10 : -10;
            case 'n': return Character.isUpperCase(piece) ? 30 : -30;
            case 'b': return Character.isUpperCase(piece) ? 30 : -30;
            case 'r': return Character.isUpperCase(piece) ? 50 : -50;
            case 'q': return Character.isUpperCase(piece) ? 90 : -90;
            case 'k': return Character.isUpperCase(piece) ? 900 : -900;
        }
        return 0;
    }

    static char[][] copyBoard() {
        char[][] copy = new char[8][8];
        for (int i = 0; i < 8; i++)
            copy[i] = board[i].clone();
        return copy;
    }

    static String toMoveString(int r1, int c1, int r2, int c2) {
        return "" + (char)('a' + c1) + (8 - r1) + (char)('a' + c2) + (8 - r2);
    }
}
