package model;

import entity.Field;
import enums.FieldDimension;
import util.CellGenerator;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;
import java.io.*;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@NotThreadSafe
public final class GameData implements Externalizable {

    private static final int MAX_HISTORY_SIZE = 3;

    private Field field;
    private BigInteger scores = BigInteger.ZERO;
    private List<Memento> history = new LinkedList<>();
    private boolean gameIsOver;

    GameData(GameData gameData) {
        this.field = gameData.field.copy();
        this.scores = gameData.scores;
        this.gameIsOver = gameData.gameIsOver;
        this.history = new LinkedList<>(gameData.history);
    }

    public GameData() {
        this(FieldDimension.FOUR_AND_FOUR);
    }

    public GameData(FieldDimension fieldDimension) {
        this.field = new Field(fieldDimension);
        CellGenerator.setRandomFieldElements(field, 2);
    }

    @Immutable
    private static final class Memento implements Serializable {

        private final BigInteger scores;
        private final Field field;

        private static final long serialVersionUID = 5761738890498234L;

        private Memento(BigInteger scores, Field field) {
            this.scores = scores;
            this.field = field.copy();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Memento memento = (Memento) o;
            return Objects.equals(scores, memento.scores) &&
                    Objects.equals(field, memento.field);
        }

        @Override
        public int hashCode() {
            return Objects.hash(scores, field);
        }

    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(field);
        out.writeObject(scores);
        out.writeObject(history);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.field = (Field) in.readObject();
        this.scores = (BigInteger) in.readObject();
        @SuppressWarnings("unchecked")
        List<Memento> hist = (List<Memento>) in.readObject();
        this.history = hist;
    }

    private Memento save() {
        return new Memento(scores, field);
    }

    /**
     * This method is designed to update state of this object and change
     */
    void updateAndSaveHistory(Field field, BigInteger scoresToAdd) {
        Objects.requireNonNull(field);
        Objects.requireNonNull(scoresToAdd);
        if (this.field.getFieldDimension() == field.getFieldDimension()) {
            Memento memento = save();
            saveHistory(memento);
            this.field = field.copy();
            this.scores = this.scores.add(scoresToAdd);
        }
    }

    private void saveHistory(Memento memento) {
        if (history.size() == MAX_HISTORY_SIZE) {
            history.remove(0);
        }
        history.add(memento);
    }

    void reset() {
        scores = BigInteger.ZERO;
        field.reset();
        CellGenerator.setRandomFieldElements(field, 2);
        history.clear();
        gameIsOver = false;
    }

    boolean restore() {
        if (history.isEmpty()) {
            return false;
        }
        int lastIndex = history.size() - 1;
        Memento last = history.remove(lastIndex);
        this.scores = last.scores;
        this.field = last.field;
        return true;
    }

    BigInteger getScores() {
        return scores;
    }

    Field getField() {
        return field.copy();
    }

    FieldDimension getFieldDimension() {
        return field.getFieldDimension();
    }

    boolean gameIsOver() {
        return gameIsOver;
    }

    void setGameIsOver(boolean gameIsOver) {
        this.gameIsOver = gameIsOver;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameData gameData = (GameData) o;
        return gameIsOver == gameData.gameIsOver && Objects.equals(field, gameData.field)
                && Objects.equals(scores, gameData.scores) && Objects.equals(history, gameData.history);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field, scores, history, gameIsOver);
    }

    @Override
    public String toString() {
        return "GameData{" +
                "field=" + field +
                ", scores=" + scores +
                ", history=" + history +
                ", gameIsOver=" + gameIsOver +
                '}';
    }
}
