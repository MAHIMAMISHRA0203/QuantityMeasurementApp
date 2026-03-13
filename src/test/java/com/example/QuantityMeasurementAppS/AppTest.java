package com.example.QuantityMeasurementAppS;

package com.quantity;

import com.quantity.model.Quantity;
import com.quantity.units.LengthUnit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    @Test
    void testAddition() {

        Quantity<LengthUnit> q1 =
                new Quantity<>(1, LengthUnit.FEET);

        Quantity<LengthUnit> q2 =
                new Quantity<>(12, LengthUnit.INCHES);

        Quantity<LengthUnit> result =
                q1.add(q2, LengthUnit.FEET);

        assertEquals("Quantity(2.0, FEET)", result.toString());
    }
}