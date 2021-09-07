package com.epam.rd.autotasks.chesspuzles;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ChessPuzzlesTest {

    @ParameterizedTest
    @MethodSource("javaConfigLayouts")
    void testJavaConfigLayouts(String layout) throws Exception {
        final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Class.forName("com.epam.rd.autotasks.chesspuzles.layout." + layout));
        final ChessBoard board = ChessBoard.of(context.getBeansOfType(ChessPiece.class).values());
        Assertions.assertEquals(expectedBoard("boards", layout), board.state());
    }

    @ParameterizedTest
    @MethodSource("xmlConfigLayouts")
    void testXmlConfigLayouts(String layout) throws Exception {
        final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(layout.toLowerCase() + ".xml");
        context.refresh();
        final ChessBoard board = ChessBoard.of(context.getBeansOfType(ChessPiece.class).values());
        Assertions.assertEquals(expectedBoard("boards", layout), board.state());
    }

    private static String expectedBoard(final String dir, String name) throws IOException {
        return Files.lines(Paths.get("src", "test", "resources", dir, name + ".txt"))
                .collect(Collectors.joining("\n"));
    }

    private static Stream<String> javaConfigLayouts() {
        return Stream.of(
                "Default",
                "DefaultBlack",
                "DefaultWhite",
                "Puzzle01",
                "Puzzle02"
        );
    }

    private static Stream<String> xmlConfigLayouts() {
        return Stream.of(
                "Default",
                "Puzzle03",
                "Puzzle04",
                "Puzzle05"
        );

    }

}
