package com.mill.robmillaci.chess;

import com.mill.robmillaci.chess.board.Board;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class BoardTest {
    @Test
    public void initialBoard(){
        final Board board = Board.createStandardBoard();
        assertEquals(board.currentPlayer().getLegalMoves().size(),20);
        assertEquals(board.currentPlayer().getOpponent().getLegalMoves().size(),20);
        assertFalse(board.currentPlayer().isInCheck());
        assertFalse(board.currentPlayer().isInCheckMate());
        //assertFalse(board.currentPlayer().isCastled());
        //assertTrue(board.currentPlayer().isKingSideCastleCapable());
        //assertTrue(board.currentPlayer().isQueenSideCastleCapable());
        assertEquals(board.currentPlayer(),board.whitePlayer());
        assertEquals(board.currentPlayer().getOpponent(),board.blackPlayer());
        assertFalse(board.currentPlayer().getOpponent().isInCheck());
        assertFalse(board.currentPlayer().getOpponent().isInCheckMate());
        //assertFalse(board.currentPlayer().getOpponent().isCastled());
        //assertTrue(board.currentPlayer().getOpponent().isKingSideCastleCapable());
        //assertTrue(board.currentPlayer().getOpponent().isQueenSideCastleCapable());
        //assertEquals(new StandardBoardEvaluator().evaluate(board,0),0);
    }



}