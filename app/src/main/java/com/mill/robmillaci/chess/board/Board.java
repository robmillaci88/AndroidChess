package com.mill.robmillaci.chess.board;

import android.util.Log;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.mill.robmillaci.chess.pieces.Alliance;
import com.mill.robmillaci.chess.pieces.Bishop;
import com.mill.robmillaci.chess.pieces.King;
import com.mill.robmillaci.chess.pieces.Knight;
import com.mill.robmillaci.chess.pieces.Pawn;
import com.mill.robmillaci.chess.pieces.Piece;
import com.mill.robmillaci.chess.pieces.Queen;
import com.mill.robmillaci.chess.pieces.Rook;
import com.mill.robmillaci.chess.player.BlackPlayer;
import com.mill.robmillaci.chess.player.Player;
import com.mill.robmillaci.chess.player.WhitePlayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
    private final List<Tile> gameBoard;
    private final Collection<Piece> whitePieces;
    private final Collection<Piece> blackPieces;

    private final WhitePlayer whiteplayer;
    private final BlackPlayer blackplayer;
    private final Player currentPlayer;

    private final Pawn enPassantPawn;


    private Board(Builder builder){
        this.gameBoard = createGameBoard(builder);
        this.whitePieces = calculateActivePieces(this.gameBoard,Alliance.WHITE);
        this.blackPieces = calculateActivePieces(this.gameBoard,Alliance.BLACK);
        this.enPassantPawn = builder.enPassantPawn;

        final Collection<Move> whiteStandardLegalMoves = calculateLegalMoves(this.whitePieces);
        final Collection<Move> blackStandardLegalMoves = calculateLegalMoves(this.blackPieces);

        this.whiteplayer = new WhitePlayer(this,whiteStandardLegalMoves,blackStandardLegalMoves);
        this.blackplayer = new BlackPlayer(this,whiteStandardLegalMoves,blackStandardLegalMoves);
        this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.whiteplayer,this.blackplayer);
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0 ; i < BoardUtils.NUM_TILES; i++){
            final String tileText = this.gameBoard.get(i).toString();
            stringBuilder.append(String.format("%3s",tileText));
            if((i+1) % BoardUtils.NUM_TILES_PER_ROW == 0){
                stringBuilder.append("\n");
            }
        }
        return stringBuilder.toString();
    }

    public Player whitePlayer(){
        return this.whiteplayer;
    }

    public Player blackPlayer(){
        return this.blackplayer;
    }

    public Collection<Piece> getBlackPieces(){
        return this.blackPieces;
    }

    public Collection<Piece> getWhitePieces(){
        return this.whitePieces;
    }

    public Pawn getEnPassantPawn(){
        return this.enPassantPawn;
    }

    private Collection<Move> calculateLegalMoves(Collection<Piece> pieces) {
        final List<Move> legalMoves = new ArrayList<>();
        for (final Piece piece : pieces){
            legalMoves.addAll(piece.calculateLegalMoves(this));
        }
        return Collections.unmodifiableCollection(legalMoves);

    }

    private static Collection<Piece> calculateActivePieces(final List<Tile> gameBoard, final Alliance alliance) {

        final List<Piece> activePieces = new ArrayList<>();

        for (final Tile tile : gameBoard){
            if (tile.isTileOccupied()){
                final Piece piece = tile.getPiece();
                if (piece.getPieceAlliance() == alliance){
                    activePieces.add(piece);
                }
            }
        }
        return ImmutableList.copyOf(activePieces);
    }

    public Tile getTile(final int tileCoordinate){
        return gameBoard.get(tileCoordinate);
    }

    private static List<Tile> createGameBoard(final Builder builder) {
        final Tile[] tiles = new Tile[BoardUtils.NUM_TILES];
        for (int i = 0 ; i < BoardUtils.NUM_TILES; i++){
            tiles[i] = Tile.createTile(i,builder.boardConfig.get(i));
        }
        return ImmutableList.copyOf(tiles);
    }

    public static Board createStandardBoard(){
        final Builder builder = new Builder();

        //Black layout
        builder.setPiece(new Rook(Alliance.BLACK,0));
        builder.setPiece(new Knight(Alliance.BLACK,1));
        builder.setPiece(new Bishop(Alliance.BLACK,2));
        builder.setPiece(new Queen(Alliance.BLACK,3));
        builder.setPiece(new King(Alliance.BLACK,4));
        builder.setPiece(new Bishop(Alliance.BLACK,5));
        builder.setPiece(new Knight(Alliance.BLACK,6));
        builder.setPiece(new Rook(Alliance.BLACK,7));
        builder.setPiece(new Pawn(Alliance.BLACK,8));
        builder.setPiece(new Pawn(Alliance.BLACK,9));
        builder.setPiece(new Pawn(Alliance.BLACK,10));
        builder.setPiece(new Pawn(Alliance.BLACK,11));
        builder.setPiece(new Pawn(Alliance.BLACK,12));
        builder.setPiece(new Pawn(Alliance.BLACK,13));
        builder.setPiece(new Pawn(Alliance.BLACK,14));
        builder.setPiece(new Pawn(Alliance.BLACK,15));

        //White layout
        builder.setPiece(new Pawn(Alliance.WHITE,48));
        builder.setPiece(new Pawn(Alliance.WHITE,49));
        builder.setPiece(new Pawn(Alliance.WHITE,50));
        builder.setPiece(new Pawn(Alliance.WHITE,51));
        builder.setPiece(new Pawn(Alliance.WHITE,52));
        builder.setPiece(new Pawn(Alliance.WHITE,53));
        builder.setPiece(new Pawn(Alliance.WHITE,54));
        builder.setPiece(new Pawn(Alliance.WHITE,55));
        builder.setPiece(new Rook(Alliance.WHITE,56));
        builder.setPiece(new Knight(Alliance.WHITE,57));
        builder.setPiece(new Bishop(Alliance.WHITE,58));
        builder.setPiece(new Queen(Alliance.WHITE,59));
        builder.setPiece(new King(Alliance.WHITE,60));
        builder.setPiece(new Bishop(Alliance.WHITE,61));
        builder.setPiece(new Knight(Alliance.WHITE,62));
        builder.setPiece(new Rook(Alliance.WHITE,63));

        //White to move
        builder.setMoveMaker(Alliance.WHITE);
        return builder.build();
    }

    public Player currentPlayer() {
        return this.currentPlayer;
    }

    public Iterable<Move> getAllLegalMoves() {
        return Iterables.unmodifiableIterable(Iterables.concat(this.whiteplayer.getLegalMoves(),this.blackplayer.getLegalMoves()));
    }

    public static class Builder {

        Map<Integer,Piece> boardConfig = new HashMap<>();
        Alliance nextMoveMaker;
        private Pawn enPassantPawn;

        public Builder(){

        }

        public Builder setPiece(final Piece piece){
            this.boardConfig.put(piece.getPiecePosition(),piece);
            return this;
        }

        public void removePiece(final Piece piece){
            this.boardConfig.remove(piece.getPiecePosition(),piece);
        }

        public Builder setMoveMaker(final Alliance nextMoveMaker){
            this.nextMoveMaker = nextMoveMaker;
            return this;
        }

        public Board build(){
            return new Board(this);
        }

        public void setEnPassantPawn(Pawn enPassantPawn) {
            this.enPassantPawn = enPassantPawn;
        }
    }
}
