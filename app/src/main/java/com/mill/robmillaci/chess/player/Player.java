package com.mill.robmillaci.chess.player;

import android.util.Log;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.mill.robmillaci.chess.board.Board;
import com.mill.robmillaci.chess.board.Move;
import com.mill.robmillaci.chess.pieces.Alliance;
import com.mill.robmillaci.chess.pieces.King;
import com.mill.robmillaci.chess.pieces.Piece;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Player {

    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> legalMoves;
    private final boolean isInCheck;

    public Player(final Board board, final Collection<Move> legalMoves, final Collection<Move> opponentMoves) {
        this.board = board;
        this.playerKing = establishKing();
        this.legalMoves = ImmutableList.copyOf(Iterables.concat(legalMoves, calculateKingCastles(legalMoves, opponentMoves)));
        this.isInCheck = !Player.calculateAttacksOnTile(this.playerKing.getPiecePosition(), opponentMoves).isEmpty();
    }

    protected static Collection<Move> calculateAttacksOnTile(int piecePosition, Collection<Move> moves) {
        final List<Move> attackMoves = new ArrayList<>();
        for (final Move move : moves) {
            if (piecePosition == move.getDestinationCoordinate()) {
                attackMoves.add(move);
            }
        }

        return ImmutableList.copyOf(attackMoves);
    }

    private King establishKing() {
        Collection<Piece> playerPieces = getActivePieces();

        for (final Piece piece : playerPieces) {
            if (piece.getPieceType().isKing()) {
                return (King) piece;
            }
        }
        throw new RuntimeException("Should not reach here! Board is invalid. Cannot find king!");
    }

    public boolean isMoveLegal(Move move) {
        return this.legalMoves.contains(move);
    }

    public boolean isInCheckMate() {
        return this.isInCheck && !hasEscapeMoves();
    }

    public boolean isInStaleMate() {
        return !this.isInCheck && !hasEscapeMoves();
    }

    public boolean isInCheck() {
        return this.isInCheck;
    }

    public King getPlayerKing() {
        return playerKing;
    }

    public Collection<Move> getLegalMoves() {
        return legalMoves;
    }


    protected boolean hasEscapeMoves() {
        for (final Move move : this.legalMoves) {
            final MoveTransition transition = makeMove(move);
            if (transition.getMoveStatus().isDone()) {
                return true;
            }
        }
        return false;
    }


    public boolean inCastled() {
        return false;
    }

    public MoveTransition makeMove(final Move move) {
        Log.d("Major", "makeMove: making move ");
        if (!isMoveLegal(move)) {
            return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
        }

        Board transitionBoard = move.execute();

        Log.d("BOARD", "makeMove: is board null?  " + transitionBoard.toString());

        final Collection<Move> kingAttacks = Player.calculateAttacksOnTile(transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
                transitionBoard.currentPlayer().getLegalMoves());
        if (!kingAttacks.isEmpty()) {
            return new MoveTransition(this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }
        return new MoveTransition(transitionBoard, move, MoveStatus.DONE);
    }

    public abstract Collection<Piece> getActivePieces();

    public abstract Alliance getAlliance();

    public abstract Player getOpponent();

    protected abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move> opponentLegals);

}
