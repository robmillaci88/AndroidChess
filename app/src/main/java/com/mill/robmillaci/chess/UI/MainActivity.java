package com.mill.robmillaci.chess.UI;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mill.robmillaci.chess.R;
import com.mill.robmillaci.chess.board.Board;
import com.mill.robmillaci.chess.board.Move;
import com.mill.robmillaci.chess.board.Move.AttackMove;
import com.mill.robmillaci.chess.board.Move.PawnEnPassantAttackMove;
import com.mill.robmillaci.chess.board.Move.PawnPromotion;
import com.mill.robmillaci.chess.pieces.Alliance;
import com.mill.robmillaci.chess.pieces.Piece;
import com.mill.robmillaci.chess.pieces.Piece.PieceType;
import com.mill.robmillaci.chess.player.AI.AIThinkTank;
import com.mill.robmillaci.chess.player.MoveStatus;
import com.mill.robmillaci.chess.player.MoveTransition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

import static com.mill.robmillaci.chess.pieces.Piece.PieceType.BISHOP;
import static com.mill.robmillaci.chess.pieces.Piece.PieceType.KING;
import static com.mill.robmillaci.chess.pieces.Piece.PieceType.KNIGHT;
import static com.mill.robmillaci.chess.pieces.Piece.PieceType.PAWN;
import static com.mill.robmillaci.chess.pieces.Piece.PieceType.QUEEN;
import static com.mill.robmillaci.chess.pieces.Piece.PieceType.ROOK;

public class MainActivity extends AppCompatActivity implements AIThinkTank.minimaxcomplete {

    private Board inPlayBoard;
    private ArrayList<ImageView> tiles;
    private ArrayList<ImageView> hints;
    private ArrayList<Drawable> whitePiecesTaken;
    private ArrayList<Drawable> blackPiecesTaken;
    private ArrayList<RadioButton> promotionRadioButtons;

    RadioButton bishop_radio_btn;
    RadioButton queen_radio_btn;
    RadioButton knight_radio_btn;
    RadioButton rook_radio_btn;

    private TextView statusView;
    private TextView playerTurn;

    private ImageView destinationTile;

    private RecyclerView whitePiecesRecyclerView;
    private RecyclerView blackPiecesRecyclerView;
    private RecyclerViewAdaptor mWhiteAdaptor;
    private RecyclerViewAdaptor mBlackAdaptor;

    private static PieceType promotionPiece = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusView = findViewById(R.id.statusTextView);
        playerTurn = findViewById(R.id.playerturnText);

        whitePiecesTaken = new ArrayList<>();
        blackPiecesTaken = new ArrayList<>();

        whitePiecesRecyclerView = findViewById(R.id.whitePiecesRecyclerView);
        blackPiecesRecyclerView = findViewById(R.id.blackPiecesRecyclerView);

        mWhiteAdaptor = new RecyclerViewAdaptor(whitePiecesTaken);
        mBlackAdaptor = new RecyclerViewAdaptor(blackPiecesTaken);

        whitePiecesRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        blackPiecesRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));

        whitePiecesRecyclerView.setAdapter(mWhiteAdaptor);
        blackPiecesRecyclerView.setAdapter(mBlackAdaptor);


        inPlayBoard = Board.createStandardBoard();
        playerTurn.setText(inPlayBoard.currentPlayer().getAlliance().isBlack() ? "Black player's turn" : "White player's turn");
        System.out.println(inPlayBoard);

        //create an array to store all tiles and tile hints
        tiles = new ArrayList<>();

        createTiles();
        //set the board pieces and click listeners
        setBoardPieces(tiles);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.resetgame:
                inPlayBoard = Board.createStandardBoard();

                whitePiecesTaken = new ArrayList<>();
                mWhiteAdaptor = new RecyclerViewAdaptor(whitePiecesTaken);
                whitePiecesRecyclerView.setAdapter(mWhiteAdaptor);

                blackPiecesTaken = new ArrayList<>();
                mBlackAdaptor = new RecyclerViewAdaptor(blackPiecesTaken);
                blackPiecesRecyclerView.setAdapter(mBlackAdaptor);


                playerTurn.setText("White player's turn");
                statusView.setText("");

                for (ImageView i : tiles) {
                    i.setImageDrawable(null);
                }
                //create an array to store all tiles and tile hints
                tiles = new ArrayList<>();
                createTiles();

                //set the board pieces and click listeners
                setBoardPieces(tiles);
                break;
        }

        return true;
    }

    private void setBoardPieces(final ArrayList<ImageView> tiles) {
        for (final Piece p : inPlayBoard.getBlackPieces()) {
            ImageView thisTile = tiles.get(p.getPiecePosition());
            if (p.getPieceType() == BISHOP) {
                thisTile.setImageResource(R.drawable.blackbishop);
//                thisTile.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        setBlackOnClick(p);
//                    }
//                });

            }
            if (p.getPieceType() == KING) {
                thisTile.setImageResource(R.drawable.blackking);
//                thisTile.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        setBlackOnClick(p);
//                    }
//                });
            }
            if (p.getPieceType() == KNIGHT) {
                thisTile.setImageResource(R.drawable.blackknight);
//                thisTile.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        setBlackOnClick(p);
//                    }
//                });
            }
            if (p.getPieceType() == PAWN) {
                thisTile.setImageResource(R.drawable.blackpawn);
//                thisTile.setOnClickListener(new View.OnClickListener() {
//                    @SuppressLint("ResourceType")
//                    @Override
//                    public void onClick(View view) {
//                        setBlackOnClick(p);
//                    }
//                });
            }
            if (p.getPieceType() == QUEEN) {
                thisTile.setImageResource(R.drawable.blackqueen);
//                thisTile.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        setBlackOnClick(p);
//                    }
//                });
            }
            if (p.getPieceType() == ROOK) {
                thisTile.setImageResource(R.drawable.blackrook);
//                thisTile.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        setBlackOnClick(p);
//                    }
//                });
            }
        }

//set white pieces image
        for (final Piece p : inPlayBoard.getWhitePieces()) {
            ImageView thisTile = tiles.get(p.getPiecePosition());
            if (p.getPieceType() == BISHOP) {
                thisTile.setImageResource(R.drawable.whitebishop);
                thisTile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setWhiteOnClick(p);
                    }
                });
            }
            if (p.getPieceType() == KING) {
                thisTile.setImageResource(R.drawable.whiteking);
                thisTile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setWhiteOnClick(p);
                    }
                });
            }
            if (p.getPieceType() == KNIGHT) {
                thisTile.setImageResource(R.drawable.whiteknight);
                thisTile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setWhiteOnClick(p);
                    }
                });
            }
            if (p.getPieceType() == PAWN) {
                thisTile.setImageResource(R.drawable.whitepawn);
                thisTile.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onClick(View view) {
                        setWhiteOnClick(p);
                    }
                });

            }
            if (p.getPieceType() == QUEEN) {
                thisTile.setImageResource(R.drawable.whitequeen);
                thisTile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setWhiteOnClick(p);
                    }
                });
            }
            if (p.getPieceType() == ROOK) {
                thisTile.setImageResource(R.drawable.whiterook);
                thisTile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setWhiteOnClick(p);
                    }
                });
            }
        }
        if (inPlayBoard.currentPlayer().getAlliance() == Alliance.WHITE) {
            removeBlackOnClickListeners();
        } else {
            removeWhiteOnClickListeners();
        }
    }

    private void setBlackOnClick(Piece p) {
        Collection<Move> legalMoves = p.calculateLegalMoves(inPlayBoard);
        clearHints();

        for (final Move m : legalMoves) {
            int destinationTileLocation = m.getDestinationCoordinate();
            hints.get(destinationTileLocation).setVisibility(View.VISIBLE);

            destinationTile = tiles.get(destinationTileLocation);
            destinationTile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Drawable enPassantPieceDrawabe = null;
                    final Drawable pieceAtDestination = ((ImageView) view).getDrawable();
                    statusView.setText("");

                    if (!(m instanceof PawnPromotion)) {

                        final Move move = Move.MoveFactory.createMove(inPlayBoard, m.getCurrentCoordinate(),
                                m.getDestinationCoordinate());

                        if (move instanceof PawnEnPassantAttackMove) {
                            int enPassantPawnPosition = inPlayBoard.getEnPassantPawn().getPiecePosition();
                            enPassantPieceDrawabe = tiles.get(enPassantPawnPosition).getDrawable();
                            tiles.get(enPassantPawnPosition).setImageDrawable(null);
                            tiles.get(enPassantPawnPosition).setOnClickListener(null);
                        }

                        final MoveTransition moveTransition = inPlayBoard.currentPlayer().makeMove(move);
                        MoveStatus movestatusresponse = moveTransition.getMoveStatus();
                        if (movestatusresponse == MoveStatus.DONE) {
                            tiles.get(m.getCurrentCoordinate()).setImageDrawable(null);
                            clearHints();
                            inPlayBoard = moveTransition.getTransitionBoard();
                            setBoardPieces(tiles);
                            playerTurn.setText(inPlayBoard.currentPlayer().getAlliance().isBlack() ? "Black player's turn" : "White player's turn");


                            if (move instanceof AttackMove && !(move instanceof PawnEnPassantAttackMove)) {
                                blackPiecesTaken.add(pieceAtDestination);
                                mBlackAdaptor.notifyDataSetChanged();
                            } else if (move instanceof AttackMove && move instanceof PawnEnPassantAttackMove) {
                                blackPiecesTaken.add(enPassantPieceDrawabe);
                                mBlackAdaptor.notifyDataSetChanged();
                            }


                            if (inPlayBoard.currentPlayer().isInCheckMate()) {
                                View v = getLayoutInflater().inflate(R.layout.checkmate_dialog, null);
                                createAlertDialog(v, true);
                            } else {
                                if (inPlayBoard.currentPlayer().isInCheck()) {
                                    Toast.makeText(getApplicationContext(), "CHECK!", Toast.LENGTH_LONG).show();
                                } else if (inPlayBoard.currentPlayer().isInStaleMate()) {
                                    View v = getLayoutInflater().inflate(R.layout.checkmate_dialog, null);
                                    createAlertDialog(v, false);
                                }
                            }


                        } else if (movestatusresponse == MoveStatus.ILLEGAL_MOVE) {
                            statusView.setText("This move is illegal!");
                        } else if (movestatusresponse == MoveStatus.LEAVES_PLAYER_IN_CHECK) {
                            statusView.setText("Illegal move - player is in check!");
                            if (inPlayBoard.currentPlayer().isInCheckMate()) {
                                Toast.makeText(getApplicationContext(), "CHECKMATE", Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        createPromotionDialog(m, pieceAtDestination);
                    }
                }
            });
        }

    }

    private void createAlertDialog(View v, boolean isitCheckMate) {
        AlertDialog.Builder alertbuilder = new AlertDialog.Builder(this);
        alertbuilder.setView(v);
        ImageView alertImage = v.findViewById(R.id.checkalertdiagimage);

        if (isitCheckMate) {
            alertImage.setBackgroundResource(R.drawable.checkmate);
        } else {
            alertImage.setBackgroundResource(R.drawable.stalemate);
        }

        alertbuilder.show();
    }

    private void setWhiteOnClick(Piece p) {
        Collection<Move> legalMoves = p.calculateLegalMoves(inPlayBoard);
        clearHints();

        for (final Move m : legalMoves) {
            int destinationTileLocation = m.getDestinationCoordinate();
            hints.get(destinationTileLocation).setVisibility(View.VISIBLE);

            destinationTile = tiles.get(destinationTileLocation);
            destinationTile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Drawable enPassantPieceDrawabe = null;
                    final Drawable pieceAtDestination = ((ImageView) view).getDrawable();
                    final Move move = Move.MoveFactory.createMove(inPlayBoard, m.getCurrentCoordinate(),
                            m.getDestinationCoordinate());

                    if (!(m instanceof PawnPromotion)) {

                        if (move instanceof PawnEnPassantAttackMove) {
                            int enPassantPawnPosition = inPlayBoard.getEnPassantPawn().getPiecePosition();
                            enPassantPieceDrawabe = tiles.get(enPassantPawnPosition).getDrawable();
                            tiles.get(enPassantPawnPosition).setImageDrawable(null);
                            tiles.get(enPassantPawnPosition).setOnClickListener(null);
                        }

                        final MoveTransition moveTransition = inPlayBoard.currentPlayer().makeMove(move);
                        MoveStatus movestatusresponse = moveTransition.getMoveStatus();
                        if (movestatusresponse == MoveStatus.DONE) {
                            tiles.get(m.getCurrentCoordinate()).setImageDrawable(null);
                            clearHints();
                            inPlayBoard = moveTransition.getTransitionBoard();
                            setBoardPieces(tiles);
                            playerTurn.setText(inPlayBoard.currentPlayer().getAlliance().isBlack() ? "Black player's turn" : "White player's turn");


                            if (inPlayBoard.currentPlayer().isInCheckMate()) {
                                View v = getLayoutInflater().inflate(R.layout.checkmate_dialog, null);
                                createAlertDialog(v, true);
                            } else {
                                if (inPlayBoard.currentPlayer().isInCheck()) {
                                    Toast.makeText(getApplicationContext(), "CHECK!", Toast.LENGTH_LONG).show();
                                } else if (inPlayBoard.currentPlayer().isInStaleMate()) {
                                    View v = getLayoutInflater().inflate(R.layout.checkmate_dialog, null);
                                    createAlertDialog(v, false);
                                }
                            }

                            if (move instanceof AttackMove && !(move instanceof PawnEnPassantAttackMove)) {
                                whitePiecesTaken.add(pieceAtDestination);
                                mWhiteAdaptor.notifyDataSetChanged();
                            } else if (move instanceof AttackMove && move instanceof PawnEnPassantAttackMove) {
                                whitePiecesTaken.add(enPassantPieceDrawabe);
                                mWhiteAdaptor.notifyDataSetChanged();
                            }

                            try {
                                notifyAI();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else if (movestatusresponse == MoveStatus.ILLEGAL_MOVE) {
                            statusView.setText("This move is illegal!");
                        } else if (movestatusresponse == MoveStatus.LEAVES_PLAYER_IN_CHECK) {
                            statusView.setText("Illegal move - player is in check!");
                            if (inPlayBoard.currentPlayer().getOpponent().isInCheckMate()) {
                                Toast.makeText(getApplicationContext(), "CHECKMATE", Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        createPromotionDialog(m, pieceAtDestination);
                    }
                }
            });
        }
    }

    private void notifyAI() throws ExecutionException, InterruptedException {
        if (!inPlayBoard.currentPlayer().isInStaleMate() && !inPlayBoard.currentPlayer().isInCheckMate()) {
            //create AI thread
            //execute AI work
            final AIThinkTank thinkTank = new AIThinkTank(inPlayBoard, this, 2);
            thinkTank.execute();
        }

        if (inPlayBoard.currentPlayer().isInCheckMate()) {
            Toast.makeText(this, "Checkmate! game over", Toast.LENGTH_LONG).show();
        }


        if (inPlayBoard.currentPlayer().isInStaleMate()) {
            Toast.makeText(this, "Stalemate! game over", Toast.LENGTH_LONG).show();
        }

    }

    private void removeBlackOnClickListeners() {
        for (Piece p : inPlayBoard.getBlackPieces()) {
            tiles.get(p.getPiecePosition()).setOnClickListener(null);
        }
    }

    private void removeWhiteOnClickListeners() {
        for (Piece p : inPlayBoard.getWhitePieces()) {
            tiles.get(p.getPiecePosition()).setOnClickListener(null);
        }
    }

    private void clearHints() {
        for (ImageView i : hints) {
            i.setVisibility(View.GONE);
        }
    }

    private void createPromotionDialog(final Move m, final Drawable pieceAtDestination) {
        View v = getLayoutInflater().inflate(R.layout.promotion_diag, null);
        AlertDialog.Builder promotionDiag = new AlertDialog.Builder(this);
        promotionDiag.setTitle("Select a promotion piece");
        promotionDiag.setView(v);
        promotionDiag.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for (RadioButton r : promotionRadioButtons) {
                    if (r.isChecked()) {
                        switch (r.getTag().toString()) {
                            case "bishop":
                                promotionPiece = BISHOP;
                                continueMoveCreation(m, pieceAtDestination);
                                break;

                            case "knight":
                                promotionPiece = KNIGHT;
                                continueMoveCreation(m, pieceAtDestination);
                                break;

                            case "queen":
                                promotionPiece = QUEEN;
                                continueMoveCreation(m, pieceAtDestination);
                                break;

                            case "rook":
                                promotionPiece = ROOK;
                                continueMoveCreation(m, pieceAtDestination);
                                break;
                        }
                    }
                }
            }
        });
        promotionDiag.show();
        promotionRadioButtons = new ArrayList<>();
        bishop_radio_btn = v.findViewById(R.id.bishop_promo);
        knight_radio_btn = v.findViewById(R.id.knight_promo);
        queen_radio_btn = v.findViewById(R.id.queen_promo);
        rook_radio_btn = v.findViewById(R.id.rook_promo);

        promotionRadioButtons.add(bishop_radio_btn);
        promotionRadioButtons.add(queen_radio_btn);
        promotionRadioButtons.add(knight_radio_btn);
        promotionRadioButtons.add(rook_radio_btn);


        for (RadioButton r : promotionRadioButtons) {
            r.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (RadioButton r : promotionRadioButtons) {
                        if (!(r.getId() == view.getId())) {
                            r.setChecked(false);
                        }
                    }
                }
            });
        }

    }

    private void continueMoveCreation(Move m, Drawable pieceAtDestination) {
        Drawable enPassantPieceDrawabe = null;
        final Move move = Move.MoveFactory.createMove(inPlayBoard, m.getCurrentCoordinate(),
                m.getDestinationCoordinate());

        if (move instanceof PawnEnPassantAttackMove) {
            int enPassantPawnPosition = inPlayBoard.getEnPassantPawn().getPiecePosition();
            enPassantPieceDrawabe = tiles.get(enPassantPawnPosition).getDrawable();
            tiles.get(enPassantPawnPosition).setImageDrawable(null);
            tiles.get(enPassantPawnPosition).setOnClickListener(null);
        }

        final MoveTransition moveTransition = inPlayBoard.currentPlayer().makeMove(move);
        MoveStatus movestatusresponse = moveTransition.getMoveStatus();
        if (movestatusresponse == MoveStatus.DONE) {
            tiles.get(m.getCurrentCoordinate()).setImageDrawable(null);
            clearHints();
            inPlayBoard = moveTransition.getTransitionBoard();
            setBoardPieces(tiles);
            playerTurn.setText(inPlayBoard.currentPlayer().getAlliance().isBlack() ? "Black player's turn" : "White player's turn");


            if (move instanceof AttackMove && !(move instanceof PawnEnPassantAttackMove)) {
                blackPiecesTaken.add(pieceAtDestination);
                mBlackAdaptor.notifyDataSetChanged();
            } else if (move instanceof AttackMove && move instanceof PawnEnPassantAttackMove) {
                blackPiecesTaken.add(enPassantPieceDrawabe);
                mBlackAdaptor.notifyDataSetChanged();
            }

        } else if (movestatusresponse == MoveStatus.ILLEGAL_MOVE) {
            statusView.setText("This move is illegal!");
        } else if (movestatusresponse == MoveStatus.LEAVES_PLAYER_IN_CHECK) {
            statusView.setText("Illegal move - player is in check!");
        }
    }

    private void createTiles() {
        ImageView p0 = findViewById(R.id.p0);
        ImageView p1 = findViewById(R.id.p1);
        ImageView p2 = findViewById(R.id.p2);
        ImageView p3 = findViewById(R.id.p3);
        ImageView p4 = findViewById(R.id.p4);
        ImageView p5 = findViewById(R.id.p5);
        ImageView p6 = findViewById(R.id.p6);
        ImageView p7 = findViewById(R.id.p7);
        ImageView p8 = findViewById(R.id.p8);
        ImageView p9 = findViewById(R.id.p9);
        ImageView p10 = findViewById(R.id.p10);
        ImageView p11 = findViewById(R.id.p11);
        ImageView p12 = findViewById(R.id.p12);
        ImageView p13 = findViewById(R.id.p13);
        ImageView p14 = findViewById(R.id.p14);
        ImageView p15 = findViewById(R.id.p15);
        ImageView p16 = findViewById(R.id.p16);
        ImageView p17 = findViewById(R.id.p17);
        ImageView p18 = findViewById(R.id.p18);
        ImageView p19 = findViewById(R.id.p19);
        ImageView p20 = findViewById(R.id.p20);
        ImageView p21 = findViewById(R.id.p21);
        ImageView p22 = findViewById(R.id.p22);
        ImageView p23 = findViewById(R.id.p23);
        ImageView p24 = findViewById(R.id.p24);
        ImageView p25 = findViewById(R.id.p25);
        ImageView p26 = findViewById(R.id.p26);
        ImageView p27 = findViewById(R.id.p27);
        ImageView p28 = findViewById(R.id.p28);
        ImageView p29 = findViewById(R.id.p29);
        ImageView p30 = findViewById(R.id.p30);
        ImageView p31 = findViewById(R.id.p31);
        ImageView p32 = findViewById(R.id.p32);
        ImageView p33 = findViewById(R.id.p33);
        ImageView p34 = findViewById(R.id.p34);
        ImageView p35 = findViewById(R.id.p35);
        ImageView p36 = findViewById(R.id.p36);
        ImageView p37 = findViewById(R.id.p37);
        ImageView p38 = findViewById(R.id.p38);
        ImageView p39 = findViewById(R.id.p39);
        ImageView p40 = findViewById(R.id.p40);
        ImageView p41 = findViewById(R.id.p41);
        ImageView p42 = findViewById(R.id.p42);
        ImageView p43 = findViewById(R.id.p43);
        ImageView p44 = findViewById(R.id.p44);
        ImageView p45 = findViewById(R.id.p45);
        ImageView p46 = findViewById(R.id.p46);
        ImageView p47 = findViewById(R.id.p47);
        ImageView p48 = findViewById(R.id.p48);
        ImageView p49 = findViewById(R.id.p49);
        ImageView p50 = findViewById(R.id.p50);
        ImageView p51 = findViewById(R.id.p51);
        ImageView p52 = findViewById(R.id.p52);
        ImageView p53 = findViewById(R.id.p53);
        ImageView p54 = findViewById(R.id.p54);
        ImageView p55 = findViewById(R.id.p55);
        ImageView p56 = findViewById(R.id.p56);
        ImageView p57 = findViewById(R.id.p57);
        ImageView p58 = findViewById(R.id.p58);
        ImageView p59 = findViewById(R.id.p59);
        ImageView p60 = findViewById(R.id.p60);
        ImageView p61 = findViewById(R.id.p61);
        ImageView p62 = findViewById(R.id.p62);
        ImageView p63 = findViewById(R.id.p63);

        tiles.add(p0);
        tiles.add(p1);
        tiles.add(p2);
        tiles.add(p3);
        tiles.add(p4);
        tiles.add(p5);
        tiles.add(p6);
        tiles.add(p7);
        tiles.add(p8);
        tiles.add(p9);
        tiles.add(p10);
        tiles.add(p11);
        tiles.add(p12);
        tiles.add(p13);
        tiles.add(p14);
        tiles.add(p15);
        tiles.add(p16);
        tiles.add(p17);
        tiles.add(p18);
        tiles.add(p19);
        tiles.add(p20);
        tiles.add(p21);
        tiles.add(p22);
        tiles.add(p23);
        tiles.add(p24);
        tiles.add(p25);
        tiles.add(p26);
        tiles.add(p27);
        tiles.add(p28);
        tiles.add(p29);
        tiles.add(p30);
        tiles.add(p31);
        tiles.add(p32);
        tiles.add(p33);
        tiles.add(p34);
        tiles.add(p35);
        tiles.add(p36);
        tiles.add(p37);
        tiles.add(p38);
        tiles.add(p39);
        tiles.add(p40);
        tiles.add(p41);
        tiles.add(p42);
        tiles.add(p43);
        tiles.add(p44);
        tiles.add(p45);
        tiles.add(p46);
        tiles.add(p47);
        tiles.add(p48);
        tiles.add(p49);
        tiles.add(p50);
        tiles.add(p51);
        tiles.add(p52);
        tiles.add(p53);
        tiles.add(p54);
        tiles.add(p55);
        tiles.add(p56);
        tiles.add(p57);
        tiles.add(p58);
        tiles.add(p59);
        tiles.add(p60);
        tiles.add(p61);
        tiles.add(p62);
        tiles.add(p63);

        //create arraylist of hintimages
        ImageView h0 = findViewById(R.id.h0);
        ImageView h1 = findViewById(R.id.h1);
        ImageView h2 = findViewById(R.id.h2);
        ImageView h3 = findViewById(R.id.h3);
        ImageView h4 = findViewById(R.id.h4);
        ImageView h5 = findViewById(R.id.h5);
        ImageView h6 = findViewById(R.id.h6);
        ImageView h7 = findViewById(R.id.h7);
        ImageView h8 = findViewById(R.id.h8);
        ImageView h9 = findViewById(R.id.h9);
        ImageView h10 = findViewById(R.id.h10);
        ImageView h11 = findViewById(R.id.h11);
        ImageView h12 = findViewById(R.id.h12);
        ImageView h13 = findViewById(R.id.h13);
        ImageView h14 = findViewById(R.id.h14);
        ImageView h15 = findViewById(R.id.h15);
        ImageView h16 = findViewById(R.id.h16);
        ImageView h17 = findViewById(R.id.h17);
        ImageView h18 = findViewById(R.id.h18);
        ImageView h19 = findViewById(R.id.h19);
        ImageView h20 = findViewById(R.id.h20);
        ImageView h21 = findViewById(R.id.h21);
        ImageView h22 = findViewById(R.id.h22);
        ImageView h23 = findViewById(R.id.h23);
        ImageView h24 = findViewById(R.id.h24);
        ImageView h25 = findViewById(R.id.h25);
        ImageView h26 = findViewById(R.id.h26);
        ImageView h27 = findViewById(R.id.h27);
        ImageView h28 = findViewById(R.id.h28);
        ImageView h29 = findViewById(R.id.h29);
        ImageView h30 = findViewById(R.id.h30);
        ImageView h31 = findViewById(R.id.h31);
        ImageView h32 = findViewById(R.id.h32);
        ImageView h33 = findViewById(R.id.h33);
        ImageView h34 = findViewById(R.id.h34);
        ImageView h35 = findViewById(R.id.h35);
        ImageView h36 = findViewById(R.id.h36);
        ImageView h37 = findViewById(R.id.h37);
        ImageView h38 = findViewById(R.id.h38);
        ImageView h39 = findViewById(R.id.h39);
        ImageView h40 = findViewById(R.id.h40);
        ImageView h41 = findViewById(R.id.h41);
        ImageView h42 = findViewById(R.id.h42);
        ImageView h43 = findViewById(R.id.h43);
        ImageView h44 = findViewById(R.id.h44);
        ImageView h45 = findViewById(R.id.h45);
        ImageView h46 = findViewById(R.id.h46);
        ImageView h47 = findViewById(R.id.h47);
        ImageView h48 = findViewById(R.id.h48);
        ImageView h49 = findViewById(R.id.h49);
        ImageView h50 = findViewById(R.id.h50);
        ImageView h51 = findViewById(R.id.h51);
        ImageView h52 = findViewById(R.id.h52);
        ImageView h53 = findViewById(R.id.h53);
        ImageView h54 = findViewById(R.id.h54);
        ImageView h55 = findViewById(R.id.h55);
        ImageView h56 = findViewById(R.id.h56);
        ImageView h57 = findViewById(R.id.h57);
        ImageView h58 = findViewById(R.id.h58);
        ImageView h59 = findViewById(R.id.h59);
        ImageView h60 = findViewById(R.id.h60);
        ImageView h61 = findViewById(R.id.h61);
        ImageView h62 = findViewById(R.id.h62);
        ImageView h63 = findViewById(R.id.h63);

        hints = new ArrayList<>();
        hints.add(h0);
        hints.add(h1);
        hints.add(h2);
        hints.add(h3);
        hints.add(h4);
        hints.add(h5);
        hints.add(h6);
        hints.add(h7);
        hints.add(h8);
        hints.add(h9);
        hints.add(h10);
        hints.add(h11);
        hints.add(h12);
        hints.add(h13);
        hints.add(h14);
        hints.add(h15);
        hints.add(h16);
        hints.add(h17);
        hints.add(h18);
        hints.add(h19);
        hints.add(h20);
        hints.add(h21);
        hints.add(h22);
        hints.add(h23);
        hints.add(h24);
        hints.add(h25);
        hints.add(h26);
        hints.add(h27);
        hints.add(h28);
        hints.add(h29);
        hints.add(h30);
        hints.add(h31);
        hints.add(h32);
        hints.add(h33);
        hints.add(h34);
        hints.add(h35);
        hints.add(h36);
        hints.add(h37);
        hints.add(h38);
        hints.add(h39);
        hints.add(h40);
        hints.add(h41);
        hints.add(h42);
        hints.add(h43);
        hints.add(h44);
        hints.add(h45);
        hints.add(h46);
        hints.add(h47);
        hints.add(h48);
        hints.add(h49);
        hints.add(h50);
        hints.add(h51);
        hints.add(h52);
        hints.add(h53);
        hints.add(h54);
        hints.add(h55);
        hints.add(h56);
        hints.add(h57);
        hints.add(h58);
        hints.add(h59);
        hints.add(h60);
        hints.add(h61);
        hints.add(h62);
        hints.add(h63);

        clearHints();
    }

    public static PieceType promotePawn() {
        switch (promotionPiece) {
            case BISHOP:
                return BISHOP;

            case KNIGHT:
                return KNIGHT;

            case QUEEN:
                return QUEEN;

            case ROOK:
                return ROOK;

        }
        return null;
    }

    @Override
    public void getBestMove(Move bestMove) {
        Log.d("THINKTHINK", "getBestMove: called");
        Move m = bestMove;

        Drawable pieceAtDestination = tiles.get(m.getDestinationCoordinate()).getDrawable();
        int enPassantPawnPosition = 0;

        final Move move = Move.MoveFactory.createMove(inPlayBoard, m.getCurrentCoordinate(),
                m.getDestinationCoordinate());

        if (!(m instanceof PawnPromotion)) {

            if (move instanceof PawnEnPassantAttackMove) {
                enPassantPawnPosition = inPlayBoard.getEnPassantPawn().getPiecePosition();

                tiles.get(enPassantPawnPosition).setImageDrawable(null);
                tiles.get(enPassantPawnPosition).setOnClickListener(null);
            }

            final MoveTransition moveTransition = inPlayBoard.currentPlayer().makeMove(move);
            MoveStatus movestatusresponse = moveTransition.getMoveStatus();
            if (movestatusresponse == MoveStatus.DONE) {
                tiles.get(m.getCurrentCoordinate()).setImageDrawable(null);
                clearHints();
                inPlayBoard = moveTransition.getTransitionBoard();
                setBoardPieces(tiles);

                if (move instanceof AttackMove && !(move instanceof PawnEnPassantAttackMove)) {
                    blackPiecesTaken.add(pieceAtDestination);
                    mBlackAdaptor.notifyDataSetChanged();
                } else if (move instanceof AttackMove && move instanceof PawnEnPassantAttackMove) {
                    Drawable enPassantPieceDrawabe = tiles.get(enPassantPawnPosition).getDrawable();
                    blackPiecesTaken.add(enPassantPieceDrawabe);
                    mBlackAdaptor.notifyDataSetChanged();
                }

                if (inPlayBoard.currentPlayer().isInCheckMate()) {
                    View v = getLayoutInflater().inflate(R.layout.checkmate_dialog, null);
                    createAlertDialog(v, true);
                } else {
                    if (inPlayBoard.currentPlayer().isInCheck()) {
                        Toast.makeText(getApplicationContext(), "CHECK!", Toast.LENGTH_LONG).show();
                    } else if (inPlayBoard.currentPlayer().isInStaleMate()) {
                        View v = getLayoutInflater().inflate(R.layout.checkmate_dialog, null);
                        createAlertDialog(v, false);
                    }
                }

            }
            playerTurn.setText(inPlayBoard.currentPlayer().getAlliance().isBlack() ? "Black player's turn" : "White player's turn");

//                if (move instanceof AttackMove && !(move instanceof PawnEnPassantAttackMove)) {
//                    whitePiecesTaken.add(pieceAtDestination);
//                    mWhiteAdaptor.notifyDataSetChanged();
//                } else if (move instanceof AttackMove && move instanceof PawnEnPassantAttackMove) {
//                    whitePiecesTaken.add(enPassantPieceDrawabe);
//                    mWhiteAdaptor.notifyDataSetChanged();
//                }

        }
    }
}


