package com.mill.robmillaci.chess.player;

import com.mill.robmillaci.chess.board.Board;
import com.mill.robmillaci.chess.board.Move;

 public class MoveTransition {

    private final Board transitionBoard;
    private final Move move;
    private final MoveStatus mMoveStatus;

    public MoveTransition(final Board transitionBoard,final Move move,final MoveStatus moveStatus) {
        this.transitionBoard = transitionBoard;
        this.move = move;
        mMoveStatus = moveStatus;
    }

    public MoveStatus getMoveStatus() {
        return this.mMoveStatus;
    }

     public Board getTransitionBoard() {
         return this.transitionBoard;
     }
 }
