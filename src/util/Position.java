package util;

public class Position {
    public final int X;
    public final int Y;

    public Position(int x, int y) {
        this.X = x;
        this.Y = y;
    }

    public static Position nullPos() {
        return new Position(-1, -1);
    }

    public boolean isNull() {
        return this.X == -1 && this.Y == -1;
    }

    @Override
    public int hashCode() {
        if (isNull())
            return -1;
        return X * 100000 + Y;
    }

    @Override
    public boolean equals(Object obj) {
        if (isNull())
            return false;
        if (obj instanceof Position other) {
            return other.X == this.X && other.Y == this.Y;
        }
        return false;
    }

    @Override
    public String toString() {
        if (isNull())
            return "Null Position";
        return "(" + this.X + ", " + this.Y + ")";
    }
}
