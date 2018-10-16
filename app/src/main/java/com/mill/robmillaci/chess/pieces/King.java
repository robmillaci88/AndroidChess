package com.mill.robmillaci.chess.pieces;

import com.google.common.collect.ImmutableList;
import com.mill.robmillaci.chess.board.Board;
import com.mill.robmillaci.chess.board.BoardUtils;
import com.mill.robmillaci.chess.board.Move;
import com.mill.robmillaci.chess.board.Tile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class King extends Piece {
    private final static int[] CANDIDATE_MOVE_COORDINATES = {-9,-8,-7,-1,1,7,8,9};

    public King(Alliance pieceAlliance,int piecePosition) {
        super(PieceType.KING,pieceAlliance, piecePosition,true);
    }

    public King(final Alliance pieceAlliance, final int piecePosition,final boolean isFirstMove){
        super(PieceType.KING,pieceAlliance, piecePosition,isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES){
            final int candidateDestinationCoordinate= this.piecePosition + currentCandidateOffset;


            if (isFirstColumnExclusion(this.piecePosition,currentCandidateOffset)
                    || isEightColumnExclusion(this.piecePosition,currentCandidateOffset)){
                continue;
            }
            if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                if (!candidateDestinationTile.isTileOccupied()) {
                    legalMoves.add(new Move.MajorMove(board,this,candidateDestinationCoordinate));
                } else {
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();

                    if (this.pieceAlliance != pieceAlliance) {
                        legalMoves.add(new Move.MajorAttackMove(board,this,candidateDestinationCoordinate,pieceAtDestination));
                    }
                }
            }
        }

        return ImmutableList.copyOf(legalMoves);
    }
    @Override
    public King movePiece(Move move) {
        return new King(move.getMovedPiece().getPieceAlliance(),move.getDestinationCoordinate());
    }
    @Override
    public String toString() {
        return PieceType.KING.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition]
                && (candidateOffset == -9
                || candidateOffset == -1
                || candidateOffset == 7);
    }

    private static boolean isEightColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.EIGHTH_COLUMN[currentPosition]
                && ((candidateOffset == -7)
                || candidateOffset == 1
                || candidateOffset == 9);
    }
}
