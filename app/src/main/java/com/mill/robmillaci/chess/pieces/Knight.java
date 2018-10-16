package com.mill.robmillaci.chess.pieces;

import com.google.common.collect.ImmutableList;
import com.mill.robmillaci.chess.board.Board;
import com.mill.robmillaci.chess.board.BoardUtils;
import com.mill.robmillaci.chess.board.Move;
import com.mill.robmillaci.chess.board.Move.AttackMove;
import com.mill.robmillaci.chess.board.Move.MajorAttackMove;
import com.mill.robmillaci.chess.board.Move.MajorMove;
import com.mill.robmillaci.chess.board.Tile;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATES = {-17, -15, -10, -6, 6, 10, 15, 17};

    public Knight( Alliance pieceAlliance, int piecePosition) {
        super(PieceType.KNIGHT,pieceAlliance, piecePosition,true);
    }
    public Knight(final Alliance pieceAlliance, final int piecePosition,final boolean isFirstMove){
        super(PieceType.KNIGHT,pieceAlliance, piecePosition,isFirstMove);
    }
    @Override
    public List<Move> calculateLegalMoves(Board board) {
        int candidateDestinationCoordinate;
        final List<Move> legalMoves = new ArrayList<>();
        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) {
            candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;

            if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {

                if (isFirstColumnExclusion(this.piecePosition, currentCandidateOffset)
                        || isSecondColumnExclusion(this.piecePosition, currentCandidateOffset)
                        || isSeventhColumnExclusion(this.piecePosition, currentCandidateOffset)
                        || isEightColumnExclusion(this.piecePosition, currentCandidateOffset)) {
                    continue;
                }

                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                if (!candidateDestinationTile.isTileOccupied()) {
                    legalMoves.add(new MajorMove(board,this,candidateDestinationCoordinate));
                } else {
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();

                    if (this.pieceAlliance != pieceAlliance) {
                        legalMoves.add(new MajorAttackMove(board,this,candidateDestinationCoordinate,pieceAtDestination));
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }
    @Override
    public Knight movePiece(Move move) {
        return new Knight(move.getMovedPiece().getPieceAlliance(),move.getDestinationCoordinate());
    }
    @Override
    public String toString() {
        return PieceType.KNIGHT.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition]
                && (candidateOffset == -17
                || candidateOffset == -10
                || candidateOffset == 6
                || candidateOffset == 15);
    }

    private static boolean isSecondColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.SECOND_COLUMN[currentPosition]
                && ((candidateOffset == -10)
                || candidateOffset == 6);
    }

    private static boolean isSeventhColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.SEVENTH_COLUMN[currentPosition]
                && ((candidateOffset == -6)
                || candidateOffset == 10);
    }

    private static boolean isEightColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.EIGHTH_COLUMN[currentPosition]
                && (candidateOffset == -15
                || candidateOffset == -6
                || candidateOffset == 10
                || candidateOffset == 17);
    }
}
