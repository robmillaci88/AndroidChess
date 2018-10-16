package com.mill.robmillaci.chess.player.AI;

import com.mill.robmillaci.chess.board.Board;
import com.mill.robmillaci.chess.board.Move;

public interface MoveStrategy {

    Move execute (Board board);

}
