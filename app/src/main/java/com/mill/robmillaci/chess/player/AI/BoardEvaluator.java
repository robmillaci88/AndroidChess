package com.mill.robmillaci.chess.player.AI;

import com.mill.robmillaci.chess.board.Board;

public interface BoardEvaluator {
    int evaluate(Board board, int depth);
}
