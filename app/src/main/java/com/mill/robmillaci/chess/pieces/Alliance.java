package com.mill.robmillaci.chess.pieces;

import com.mill.robmillaci.chess.board.BoardUtils;
import com.mill.robmillaci.chess.player.BlackPlayer;
import com.mill.robmillaci.chess.player.Player;
import com.mill.robmillaci.chess.player.WhitePlayer;

public enum Alliance {
    WHITE {
        @Override
        public int getDirection() {
            return -1;
        }

        @Override
        public boolean isWhite() {
            return true;
        }

        @Override
        public boolean isBlack() {
            return false;
        }

        @Override
        public int getOppositeDirection() {
            return 1;
        }

        @Override
        public boolean isPawnPromotionSquare(int position) {
            return BoardUtils.EIGHT_RANK[position];
        }

        @Override
        public Player choosePlayer(final WhitePlayer whiteplayer, final BlackPlayer blackplayer) {
            return whiteplayer;
        }
    },
    BLACK {
        @Override
        public int getDirection() {
            return 1;
        }

        @Override
        public boolean isWhite() {
            return false;
        }

        @Override
        public boolean isBlack() {
            return true;
        }

        @Override
        public int getOppositeDirection() {
            return -1;
        }

        @Override
        public boolean isPawnPromotionSquare(int position) {
            return BoardUtils.FIRST_RANK[position];
        }

        @Override
        public Player choosePlayer(WhitePlayer whiteplayer, BlackPlayer blackplayer) {
            return blackplayer;
        }
    };

    public abstract int getDirection();
    public abstract boolean isWhite();
    public abstract boolean isBlack();
    public abstract int getOppositeDirection();
    public abstract boolean isPawnPromotionSquare(int position);

    public abstract Player choosePlayer(final WhitePlayer whiteplayer, final BlackPlayer blackplayer);
}
