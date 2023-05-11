package org.victorpiles.escacs.engine;

import org.junit.jupiter.api.Test;

import java.util.List;

class EngineTest {

    @Test
    void getValidMoveValues() {
        String knightB1 = "nlb1";
        String openingContext = "rda8ndb8bdc8qdd8kde8bdf8ndg8rdh8pda7pdb7pdc7pdd7pde7pdf7pdg7pdh7pla2plb2plc2pld2ple2plf2plg2plh2rla1nlb1blc1qld1kle1blf1nlg1rlh1";
        List<String> validMoveValues = Engine.getValidMoveValues(knightB1, openingContext);
        System.out.println(validMoveValues);
    }
}