package com.mill.robmillaci.chess.board;


import com.google.common.collect.ImmutableMap;
import com.mill.robmillaci.chess.pieces.Piece;

import java.util.HashMap;
import java.util.Map;

public abstract class Tile {
   protected final int tileCoordinate;

   private static final Map<Integer,EmptyTile> EMPTY_TILES_CACHE = createAllPossibleEmptyTiles();

    private static Map<Integer,EmptyTile> createAllPossibleEmptyTiles() {
        final Map<Integer,EmptyTile> emptyTileMap = new HashMap<>();

        for(int i = 0 ; i < BoardUtils.NUM_TILES ; i++){
            emptyTileMap.put(i,new EmptyTile(i));
        }
        return ImmutableMap.copyOf(emptyTileMap);
    }

    public static Tile createTile(final int tileCoordinate, final Piece piece){
        return piece != null ? new OccupiedTile(tileCoordinate,piece) : EMPTY_TILES_CACHE.get(tileCoordinate);
    }

   private Tile(final int tileCoordinate) {
        this.tileCoordinate = tileCoordinate;
    }

    public abstract boolean isTileOccupied();

    public abstract Piece getPiece();

    public int getTileCoordinate() {
        return this.tileCoordinate;
    }


    public static final class EmptyTile extends Tile{

        private EmptyTile(final int coordinate){
            super(coordinate);
        }

        @Override
        public boolean isTileOccupied(){
            return false;
        }

        @Override
        public Piece getPiece() {
            return null;
        }

        @Override
        public String toString() {
            return "-";
        }
    }


    public static final class OccupiedTile extends Tile{

        private final Piece pieceOnTile;

       private OccupiedTile(final int tileCoordinate, final Piece pieceOnTile) {
            super(tileCoordinate);
            this.pieceOnTile = pieceOnTile;
        }

        @Override
        public boolean isTileOccupied() {
            return true;
        }

        @Override
        public Piece getPiece() {
            return this.pieceOnTile;
        }

        @Override
        public String toString() {
            return getPiece().getPieceAlliance().isBlack() ?getPiece().toString().toLowerCase() :
                    getPiece().toString();
        }
    }



}
