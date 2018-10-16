package com.mill.robmillaci.chess.pieces;

import com.mill.robmillaci.chess.board.Board;
import com.mill.robmillaci.chess.board.Move;

import java.util.Collection;

public abstract class Piece {

    protected final int piecePosition;
    protected final Alliance pieceAlliance;
    protected final boolean isFirstMove;
    protected final PieceType pieceType;
    private final int cachedHashCode;
    private int pieceValue;

    Piece(final PieceType pieceType, Alliance pieceAlliance, int piecePosition, final boolean isFirstMove) {
        this.pieceType = pieceType;
        this.pieceAlliance = pieceAlliance;
        this.piecePosition = piecePosition;
        this.isFirstMove = isFirstMove;
        this.cachedHashCode = computeHashCode();
        this.pieceValue = pieceType.getPieceValue();
    }

    private int computeHashCode() {
        int result = pieceType.hashCode();
        result = 31 * result + pieceAlliance.hashCode();
        result = 31 * result + piecePosition;
        result = 31 * result + (isFirstMove ? 1 : 0);
        return result;
    }

    public int getPieceValue(){
        return this.pieceValue;
    }

    public Alliance getPieceAlliance() {
        return pieceAlliance;
    }

    public boolean isFirstMove() {
        return this.isFirstMove;
    }

    public abstract Collection<Move> calculateLegalMoves(final Board board);

    public abstract Piece movePiece(Move move);

    public int getPiecePosition() {
        return this.piecePosition;
    }

    public PieceType getPieceType() {
        return this.pieceType;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Piece)) {
            return false;
        }
        final Piece otherPiece = (Piece) obj;
        return piecePosition == otherPiece.getPiecePosition() && pieceType == otherPiece.getPieceType()
                && pieceAlliance == otherPiece.getPieceAlliance() && isFirstMove == otherPiece.isFirstMove;
    }

    @Override
    public int hashCode() {
        return this.cachedHashCode;
    }


    public enum PieceType {
        PAWN(100,"P") {
            @Override
            public boolean isKing() {
                return false;
            }
            @Override
            public boolean isRook() {
                return false;
            }
        },
        KNIGHT(300, "N") {
            @Override
            public boolean isKing() {
                return false;
            }
            @Override
            public boolean isRook() {
                return false;
            }
        },
        BISHOP(300, "B") {
            @Override
            public boolean isKing() {
                return false;
            }
            @Override
            public boolean isRook() {
                return false;
            }
        },
        ROOK(500, "R") {
            @Override
            public boolean isKing() {
                return false;
            }
            @Override
            public boolean isRook() {
                return true;
            }
        },
        QUEEN(900, "Q") {
            @Override
            public boolean isKing() {
                return false;
            }
            @Override
            public boolean isRook() {
                return false;
            }
        },
        KING(10000, "K") {
            @Override
            public boolean isKing() {
                return true;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        };
        private final int pieceValue;
        private String pieceName;

        PieceType(int i, final String pieceName) {
            this.pieceName = pieceName;
            this.pieceValue = i;
        }

        public int getPieceValue() {
            return pieceValue;
        }

        @Override
        public String toString() {
            return this.pieceName;
        }

        public abstract boolean isKing();
        public abstract boolean isRook();
    }


}
