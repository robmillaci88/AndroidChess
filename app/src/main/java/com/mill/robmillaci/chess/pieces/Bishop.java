package com.mill.robmillaci.chess.pieces;

import android.util.Log;

import com.google.common.collect.ImmutableList;
import com.mill.robmillaci.chess.board.Board;
import com.mill.robmillaci.chess.board.BoardUtils;
import com.mill.robmillaci.chess.board.Move;
import com.mill.robmillaci.chess.board.Tile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.mill.robmillaci.chess.board.Move.*;

public class Bishop extends Piece {

    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {-9,-7,7,9};

    public Bishop(Alliance piecePosition, int pieceAlliance) {
        super(PieceType.BISHOP, piecePosition, pieceAlliance,true);
    }

    public Bishop(final Alliance pieceAlliance, final int piecePosition,final boolean isFirstMove){
        super(PieceType.BISHOP,pieceAlliance, piecePosition,isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves  = new ArrayList<>();

        for (final int candidateCoordinateOffset : CANDIDATE_MOVE_VECTOR_COORDINATES){
            int candidateDestinationCoordinate = this.piecePosition;

            while (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){

                if (isFirstColumnExclusion(candidateDestinationCoordinate,candidateCoordinateOffset)
                        || isEightColumnExclusion(candidateDestinationCoordinate,candidateCoordinateOffset)){
                    break;
                }
                candidateDestinationCoordinate += candidateCoordinateOffset;
                if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){

                    final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                    if (!candidateDestinationTile.isTileOccupied()) {
                        legalMoves.add(new MajorMove(board,this,candidateDestinationCoordinate));
                    } else {
                        final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                        final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();

                        if (this.pieceAlliance != pieceAlliance) {
                            legalMoves.add(new MajorAttackMove(board,this,candidateDestinationCoordinate,pieceAtDestination));
                        }
                        break;
                    }
                }
            }
        }

        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Bishop movePiece(Move move) {
        return new Bishop(move.getMovedPiece().getPieceAlliance(),move.getDestinationCoordinate());
    }

    @Override
    public String toString() {
        return PieceType.BISHOP.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -9 || candidateOffset == 7);
    }

    private static boolean isEightColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -7 || candidateOffset == 9);
    }
}
