package com.mill.robmillaci.chess.pieces;

import com.google.common.collect.ImmutableList;
import com.mill.robmillaci.chess.board.Board;
import com.mill.robmillaci.chess.board.BoardUtils;
import com.mill.robmillaci.chess.board.Move;
import com.mill.robmillaci.chess.board.Move.MajorAttackMove;
import com.mill.robmillaci.chess.board.Move.MajorMove;
import com.mill.robmillaci.chess.board.Tile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Queen extends Piece {
    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {-9,-8,-7,-1,1,7,8,9};

    public Queen(Alliance pieceAlliance,int piecePosition) {
        super(PieceType.QUEEN,pieceAlliance, piecePosition,true);
    }

    public Queen(final Alliance pieceAlliance, final int piecePosition,final boolean isFirstMove){
        super(PieceType.QUEEN,pieceAlliance, piecePosition,isFirstMove);
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
    public Queen movePiece(Move move) {
        return new Queen(move.getMovedPiece().getPieceAlliance(),move.getDestinationCoordinate());
    }
    @Override
    public String toString() {
        return PieceType.QUEEN.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -1 || candidateOffset == -9 || candidateOffset == 7);
    }

    private static boolean isEightColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -7 || candidateOffset == 1 || candidateOffset == 9);
    }
}
