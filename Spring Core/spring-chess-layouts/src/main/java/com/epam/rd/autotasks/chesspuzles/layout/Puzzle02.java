package com.epam.rd.autotasks.chesspuzles.layout;

import com.epam.rd.autotasks.chesspuzles.Cell;
import com.epam.rd.autotasks.chesspuzles.ChessPiece;
import com.epam.rd.autotasks.chesspuzles.ChessPieceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Puzzle02 {

    @Bean
    public ChessPiece getBlackQueen() {
        return new ChessPieceImpl(Cell.cell('F', 4), 'Q');
    }

    @Bean
    public ChessPiece getBlackKing() {
        return new ChessPieceImpl(Cell.cell('D', 3), 'K');
    }

    @Bean
    public ChessPiece getBlackPawn1() {
        return new ChessPieceImpl(Cell.cell('E',2), 'P');
    }

    @Bean
    public ChessPiece getWhiteQueen() {
        return new ChessPieceImpl(Cell.cell('H', 8), 'q');
    }

    @Bean
    public ChessPiece getWhiteKing() {
        return new ChessPieceImpl(Cell.cell('B', 2), 'k');
    }

}
