package com.mill.robmillaci.chess.player.AI;

import com.mill.robmillaci.chess.board.Board;
import com.mill.robmillaci.chess.pieces.Piece;
import com.mill.robmillaci.chess.player.Player;

public final class StandardBoardEvaluator implements BoardEvaluator {

    private static final int CHECK_BONUS = 50;
    private static final int CHECK_MATE_BONUS = 10000 ;
    private static final int DEPTH_BONUS = 100;

    @Override
    public int evaluate(Board board, final int depth) {
        return scorePlayer(board,board.whitePlayer(),depth) - scorePlayer(board,board.blackPlayer(),depth);
    }

    private int scorePlayer(final Board board, final Player player, final int depth) {
        return  pieceValue(player) +
                mobility(player) +
                check(player) +
                checkmate(player,depth);
               //+ castled(player);
    }

//    private static int castled(Player player) {
//        return player.isCastled();
//    }

    private static int checkmate(Player player, int depth) {
        return player.getOpponent().isInCheckMate() ? CHECK_MATE_BONUS * depthBonus(depth): 0;
    }

    private static int depthBonus(int depth) {
        return depth == 0 ? 1 : DEPTH_BONUS * depth;
    }

    private static int check(Player player) {
        return player.getOpponent().isInCheck() ? CHECK_BONUS : 0;
    }


    private static int mobility(final Player player) {
        return player.getLegalMoves().size();
    }

    private static int pieceValue (final Player player){
        int pieceValueScore = 0;
        for (final Piece piece : player.getActivePieces()){
            pieceValueScore += piece.getPieceValue();
        }
        return pieceValueScore;
    }
}
