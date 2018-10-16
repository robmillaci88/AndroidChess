package com.mill.robmillaci.chess.player.AI;

import com.mill.robmillaci.chess.board.Board;
import com.mill.robmillaci.chess.board.Move;
import com.mill.robmillaci.chess.player.MoveTransition;

public class MiniMax implements MoveStrategy {

    private final BoardEvaluator mBoardEvaluator;
    private final int searchDepth;

    public MiniMax(final int searchDepth) {
        mBoardEvaluator = new StandardBoardEvaluator();
        this.searchDepth = searchDepth;
    }

    @Override
    public Move execute(Board board) {

        final long startTime = System.currentTimeMillis();

        Move bestmove = null;

        int highestSeenValue = Integer.MIN_VALUE;
        int lowestSeenValue = Integer.MAX_VALUE;
        int currentValue;

        System.out.println(board.currentPlayer() + " thinking with depth " + this.searchDepth);

        int numMoves  = board.currentPlayer().getLegalMoves().size();

        for (final Move move : board.currentPlayer().getLegalMoves()){

            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()){
                currentValue = board.currentPlayer().getAlliance().isWhite() ? min(moveTransition.getTransitionBoard(),this.searchDepth -1) :
                                                                               max(moveTransition.getTransitionBoard(),this.searchDepth -1);

                if (board.currentPlayer().getAlliance().isWhite() && currentValue >= highestSeenValue){
                    highestSeenValue = currentValue;
                    bestmove = move;
                }else if (board.currentPlayer().getAlliance().isBlack() && currentValue <= lowestSeenValue){
                    lowestSeenValue = currentValue;
                    bestmove = move;
                }
            }
        }

        final long executionTime = System.currentTimeMillis() - startTime;

        return bestmove;
    }


    public int min(final Board board , final int depth){
        if (depth == 0){
            return this.mBoardEvaluator.evaluate(board,depth);
        }

        if(isEndGameScenario(board)) {
            return this.mBoardEvaluator.evaluate(board, depth);
        }

        int lowestSeenValue = Integer.MAX_VALUE;
        for (final Move move : board.currentPlayer().getLegalMoves()){
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()){
                final int currentValue = max(moveTransition.getTransitionBoard(),depth -1);
                if (currentValue <= lowestSeenValue){
                    lowestSeenValue = currentValue;
                }
            }
        }

        return lowestSeenValue;

    }

    public int max(final Board board, final int depth){
        if (depth == 0){
            return this.mBoardEvaluator.evaluate(board,depth);
        }

        if(isEndGameScenario(board)) {
            return this.mBoardEvaluator.evaluate(board, depth);
        }

        int highestSeenValue = Integer.MIN_VALUE;
        for (final Move move : board.currentPlayer().getLegalMoves()){
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()){
                final int currentValue = min(moveTransition.getTransitionBoard(),depth -1);
                if (currentValue >= highestSeenValue){
                    highestSeenValue = currentValue;
                }
            }
        }

        return highestSeenValue;

    }

    public static boolean isEndGameScenario(Board board) {
        return board.currentPlayer().isInCheckMate() || board.currentPlayer().isInStaleMate();
    }

    @Override
    public String toString() {
        return "MiniMax";
    }
}
