package com.mill.robmillaci.chess.board;

import android.util.Log;

import com.mill.robmillaci.chess.pieces.Pawn;
import com.mill.robmillaci.chess.pieces.Piece;
import com.mill.robmillaci.chess.pieces.Rook;

import static com.mill.robmillaci.chess.board.Board.Builder;

public abstract class Move {
    final Board board;
    final Piece movedPiece;
    final int destinationCoordinate;
    protected final boolean isFirstMove;

    public static final Move NULL_MOVE = new NullMove();

    private Move(final Board board, final Piece movedPiece, final int destinationCoordinate) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
        this.isFirstMove = movedPiece.isFirstMove();
    }

    private Move(final Board board, final int destinationCoordinate) {
        this.board = board;
        this.destinationCoordinate = destinationCoordinate;
        this.movedPiece = null;
        this.isFirstMove = false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.destinationCoordinate;
        result = prime * result + this.movedPiece.hashCode();
        result = prime * result + this.movedPiece.getPiecePosition();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Move)) {
            return false;
        }

        final Move otherMove = (Move) obj;
        return getCurrentCoordinate() == otherMove.getCurrentCoordinate() &&
               getDestinationCoordinate() == otherMove.getDestinationCoordinate() &&
               getMovedPiece().equals(otherMove.getMovedPiece());
    }

    public int getCurrentCoordinate() {
        return this.movedPiece.getPiecePosition();
    }

    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }

    public Piece getMovedPiece() {
        return movedPiece;
    }

    public boolean isAttack() {
        return false;
    }

    public boolean isCastlingMove() {
        return false;
    }

    public Piece getAttackPiece() {
        return null;
    }


    public Board execute() {

        Log.d("Major", "execute: move.execute is called");
        final Builder builder = new Builder();

        for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
            if (!this.movedPiece.equals(piece)) {
                builder.setPiece(piece);
            }
        }
        for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
            builder.setPiece(piece);
        }
        //move the moved piece!
        builder.setPiece(this.movedPiece.movePiece(this));
        builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());

        return builder.build();
    }

    public static class MajorAttackMove extends AttackMove{

        public MajorAttackMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate, attackedPiece);
            Log.d("Major", "MajorAttackMove: called");
            Log.d("Major", "MajorAttackMove: moved piece is " + movedPiece);
            Log.d("Major", "MajorAttackMove: destination is " + destinationCoordinate);
            Log.d("Major", "MajorAttackMove: attacked piece is " + attackedPiece.toString());

        }

        @Override
        public boolean equals(Object other) {
            return this == other || other instanceof MajorMove && (super.equals(other));
        }

        @Override
        public String toString() {
            return movedPiece.getPieceType() + BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }


    public static final class MajorMove extends Move {
        public MajorMove(final Board board, final Piece movedPiece, final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof MajorMove && (super.equals(other));
        }

        @Override
        public String toString() {
            return movedPiece.getPieceType().toString() +  BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }
    public static class AttackMove extends Move {
        final Piece attackedPiece;

        public AttackMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public int hashCode() {
            return this.attackedPiece.hashCode() + super.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            Log.d("Major", "equals: attack equals called");
            if (this == obj) {
                return true;
            }

            if (!(obj instanceof AttackMove)) {
                return false;
            }
            final AttackMove otherAttackMove = (AttackMove) obj;
            return super.equals(otherAttackMove) && getAttackPiece().equals(otherAttackMove.getAttackPiece());
        }

        @Override
        public boolean isAttack() {
            return true;
        }

        @Override
        public Piece getAttackPiece() {
            return this.attackedPiece;
        }
    }


    public static final class PawnMove extends Move {
        public PawnMove(final Board board, final Piece movedPiece, final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public boolean equals(Object other) {
            return this == other || other instanceof PawnMove && super.equals(other);
        }

        @Override
        public String toString() {
            return BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }


    public static class PawnAttackMove extends AttackMove {
        public PawnAttackMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }

        @Override
        public boolean equals(Object other) {
            return this == other || other instanceof PawnAttackMove && super.equals(other);
        }

        @Override
        public String toString() {
            return BoardUtils.getPositionAtCoordinate(this.movedPiece.getPiecePosition()).substring(0,1) + "x" +
                    BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }

    public static final class PawnEnPassantAttackMove extends PawnAttackMove {
        public PawnEnPassantAttackMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }

        @Override
        public boolean equals(final Object other) {
            return  this == other || other instanceof PawnEnPassantAttackMove && super.equals(other);
        }

        @Override
        public Board execute() {
            final Builder builder = new Builder();
            for (final Piece piece : this.board.currentPlayer().getActivePieces()){
                if (!this.movedPiece.equals(piece)){
                    builder.setPiece(piece);
                }
            }

            for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
                Log.d("ENPASSANT", "execute: attack piece is " + this.getAttackPiece().toString() + this.getAttackPiece().getPiecePosition());
                if (!piece.equals(this.getAttackPiece())){
                    builder.setPiece(piece);
                }
            }

         //   builder.removePiece(this.getAttackPiece());

            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            Board newBoard = builder.build();
            Log.d("NEWBOARD", "execute: new board is " + newBoard.toString());
            return newBoard;
        }
    }

    public static final class PawnJump extends Move {
        public PawnJump(final Board board, final Piece movedPiece, final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public Board execute() {
            final Builder builder = new Builder();
            for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
                if (!this.movedPiece.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }
            final Pawn movedPawn = (Pawn) this.movedPiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnPassantPawn(movedPawn);
            Log.d("ENPASSANT", "execute: en passant pawn set");
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }

        @Override
        public String toString() {
            return BoardUtils.getPositionAtCoordinate(this.destinationCoordinate) ;
        }
    }

    public static class PawnPromotion extends Move {
        final Move decoratedMove;
        final Pawn promotedPawn;

        public PawnPromotion(final Move decoratedMove) {
            super(decoratedMove.getBoard(), decoratedMove.getMovedPiece(),decoratedMove.getDestinationCoordinate());
            this.decoratedMove = decoratedMove;
            this.promotedPawn = (Pawn)decoratedMove.getMovedPiece();
        }

        @Override
        public int hashCode() {
            return decoratedMove.hashCode() + (31 + promotedPawn.hashCode());
        }

        @Override
        public boolean equals(Object other) {
            return this == other || other instanceof PawnPromotion && (super.equals(other));
        }

        @Override
        public Board execute() {
            final Board pawnMoveBoard = this.decoratedMove.execute();
            final Board.Builder builder= new Builder();
            for (final Piece piece : pawnMoveBoard.currentPlayer().getActivePieces()){
                if (!this.promotedPawn.equals(piece)){
                    builder.setPiece(piece);
                }
            }

            for (final Piece piece : pawnMoveBoard.currentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
            builder.setPiece(this.promotedPawn.getPromotionPiece().movePiece(this));
            builder.setMoveMaker(pawnMoveBoard.currentPlayer().getAlliance());
            return builder.build();
        }

        @Override
        public boolean isAttack() {
            return this.decoratedMove.isAttack();
        }

        @Override
        public Piece getAttackPiece() {
            return this.decoratedMove.getAttackPiece();
        }

        @Override
        public String toString() {
            return "";
        }
    }

    private Board getBoard() {
        return this.board;
    }

    static abstract class CastleMove extends Move {
        protected final Rook castleRook;
        protected final int castleRookStart;
        protected final int castleRookDestination;


        public CastleMove(Board board, Piece movedPiece, int destinationCoordinate, final Rook castleRook, final int castleRookStart, final int castleRookDesintation) {
            super(board, movedPiece, destinationCoordinate);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDestination = castleRookDesintation;
        }

        public Rook getCastleRook() {
            return this.castleRook;
        }

        @Override
        public boolean isCastlingMove() {
            return true;
        }

        @Override
        public Board execute() {
            final Builder builder = new Builder();
            for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
                if (!this.movedPiece.equals(piece) && !this.castleRook.equals(piece)) {
                    builder.setPiece(piece);
                }
            }

            for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setPiece(new Rook(this.castleRook.getPieceAlliance(), this.castleRookDestination));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());

            return builder.build();
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + this.castleRook.hashCode();
            result = prime * result + this.castleRookDestination;
            return result;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other){
                return true;
            }

            if (!(other instanceof CastleMove)){
                return false;
            }

            final CastleMove otherCastleMove = (CastleMove) other;
            return super.equals(otherCastleMove) && this.castleRook.equals(otherCastleMove.getCastleRook());
        }
    }

    public static final class KingSideCastleMove extends CastleMove {
        public KingSideCastleMove(final Board board, final Piece movedPiece, final int destinationCoordinate,
                                  final Rook castleRook, final int castleRookStart, final int castleRookDesintation) {
            super(board, movedPiece, destinationCoordinate, castleRook, castleRookStart, castleRookDesintation);
        }

        @Override
        public boolean equals(Object other) {
            return this == other || other instanceof KingSideCastleMove && super.equals(other);
        }

        @Override
        public String toString() {
            return "0-0";
        }
    }

    public static final class QueenSideCastleMove extends CastleMove {
        public QueenSideCastleMove(final Board board, final Piece movedPiece, final int destinationCoordinate,
                                   final Rook castleRook, final int castleRookStart, final int castleRookDesintation) {
            super(board, movedPiece, destinationCoordinate, castleRook, castleRookStart, castleRookDesintation);
        }

        @Override
        public String toString() {
            return "0-0-0";
        }

        @Override
        public boolean equals(Object other) {
            return this == other || other instanceof QueenSideCastleMove && super.equals(other);
        }
    }

    public static final class NullMove extends Move {
        public NullMove() {
            super(null,  65);
        }

        @Override
        public Board execute() {
            throw new RuntimeException("Cannot execute a null move!");
        }

        @Override
        public int getCurrentCoordinate() {
            return -1;
        }
    }

    public static class MoveFactory {

        private MoveFactory() {
            throw new RuntimeException("Not instantiable!");
        }

        public static Move createMove(final Board board, final int currentCoordinate, final int destinationCoordinate) {
            for (final Move move : board.getAllLegalMoves()) {
                if (move.getCurrentCoordinate() == currentCoordinate &&
                        move.getDestinationCoordinate() == destinationCoordinate) {
                    return move;
                }
            }

            return NULL_MOVE;
        }

    }
}
