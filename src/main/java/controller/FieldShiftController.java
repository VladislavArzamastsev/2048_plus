package controller;

import enums.Direction;

public interface FieldShiftController {

    void shift(Direction direction);

    void cancelShifts();

}
