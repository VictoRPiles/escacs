package org.victorpiles.escacs.engine.util;

import org.junit.jupiter.api.Test;
import org.victorpiles.escacs.engine.Board;
import org.victorpiles.escacs.engine.util.parser.ContextParser;

class ContextParserTest {

    @Test
    void parse() {
        String openingContext = "rda8ndb8bdc8qdd8kde8bdf8ndg8rdh8pda7pdb7pdc7pdd7pde7pdf7pdg7pdh7pla2plb2plc2pld2ple2plf2plg2plh2rla1nlb1blc1qld1kle1blf1nlg1rlh1";

        Board parsed = ContextParser.parse(openingContext);
        System.out.println(parsed);
    }
}