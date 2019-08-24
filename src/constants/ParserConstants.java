package constants;

public interface ParserConstants
{
    int START_SYMBOL = 13;

    int FIRST_NON_TERMINAL    = 13;
    int FIRST_SEMANTIC_ACTION = 17;

    int[][] PARSER_TABLE =
    {
        { -1, -1, -1, -1, -1, -1, -1,  0, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1,  1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  2,  3 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1,  5,  4,  4 }
    };

    int[][] PRODUCTIONS = 
    {
        {  8, 14,  5 },
        {  9, 15, 10 },
        { 11,  2,  6,  3,  4, 16 },
        { 12,  2,  6,  3,  4, 16 },
        { 15, 11 },
        {  0 }
    };

    String[] PARSER_ERROR =
    {
        "",
        "Era esperado fim de programa",
        "Era esperado \"(\"",
        "Era esperado \")\"",
        "Era esperado \";\"",
        "Era esperado \".\"",
        "Era esperado id",
        "Era esperado num",
        "Era esperado prog",
        "Era esperado begin",
        "Era esperado end",
        "Era esperado read",
        "Era esperado write",
        "<INICIO> inválido",
        "<BLOCO> inválido",
        "<CMD> inválido",
        "<REPCMD> inválido"
    };
}
