package com.mill.robmillaci.chess.player.AI;

import android.os.AsyncTask;
import android.util.Log;

import com.mill.robmillaci.chess.board.Board;
import com.mill.robmillaci.chess.board.Move;

public class AIThinkTank extends AsyncTask<Move, String, Move> {
    Board inPlayBoard;
    minimaxcomplete callback;
    int depth;

    public AIThinkTank(Board gameBoard, minimaxcomplete callback,int depth) {
        this.inPlayBoard = gameBoard;
        this.callback = callback;
        this.depth = depth;
    }

    @Override
    protected Move doInBackground(Move... moves) {
        final MoveStrategy miniMax = new MiniMax(depth);

        final Move bestMove = miniMax.execute(inPlayBoard);

        return bestMove;
    }

    @Override
    protected void onPostExecute(Move move) {
        Log.d("minimax", "onPostExecute: called");
        callback.getBestMove(move);
    }

   public interface minimaxcomplete{

        void getBestMove(Move move);
    }
}
