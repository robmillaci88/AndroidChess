package com.mill.robmillaci.chess.player;

import com.google.common.collect.ImmutableList;
import com.mill.robmillaci.chess.board.Board;
import com.mill.robmillaci.chess.board.Move;
import com.mill.robmillaci.chess.board.Tile;
import com.mill.robmillaci.chess.pieces.Alliance;
import com.mill.robmillaci.chess.pieces.Piece;
import com.mill.robmillaci.chess.pieces.Rook;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BlackPlayer extends Player{
    public BlackPlayer(Board board, Collection<Move> whiteStandardLegalMoves, Collection<Move> blackStandardLegalMoves) {
        super(board,blackStandardLegalMoves,whiteStandardLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.BLACK;
    }

    @Override
    public Player getOpponent() {
        return this.board.whitePlayer();
    }

    @Override
    protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals, final Collection<Move> opponentLegals) {
        final List<Move> kingCastles = new ArrayList<Move>();
        if (this.playerKing.isFirstMove() && !this.isInCheck()) {

            // Blacks kings side castle
            if (!this.board.getTile(5).isTileOccupied() && !this.board.getTile(6).isTileOccupied()) {
                final Tile rookTile = this.board.getTile(7);

                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                    if (Player.calculateAttacksOnTile(5, opponentLegals).isEmpty() &&
                            Player.calculateAttacksOnTile(6, opponentLegals).isEmpty() &&
                            rookTile.getPiece().getPieceType().isRook()) {

                        kingCastles.add(new Move.KingSideCastleMove(this.board,this.playerKing,6,
                                                                   (Rook)rookTile.getPiece(),
                                                      rookTile.getTileCoordinate(),
                                                  5));
                    }
                }
            }

            if (!this.board.getTile(1).isTileOccupied() &&
                    !this.board.getTile(2).isTileOccupied() &&
                    !this.board.getTile(3).isTileOccupied()) {

                final Tile rookTile = this.board.getTile(0);
                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove() &&
                        Player.calculateAttacksOnTile(2,opponentLegals).isEmpty() &&
                        Player.calculateAttacksOnTile(3,opponentLegals).isEmpty() &&
                        rookTile.getPiece().getPieceType().isRook()) {

                    //TODO add castle move
                    kingCastles.add(new Move.QueenSideCastleMove(this.board,this.playerKing,
                                              2,
                                                                (Rook)rookTile.getPiece(),
                                                   rookTile.getTileCoordinate(),
                                               3));
                }
            }
        }
        return ImmutableList.copyOf(kingCastles);
    }
}
