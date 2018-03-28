
package net.codetojoy.common;

import java.io.Serializable;
import java.util.*;

public class Ball implements Serializable {
    private static final int MAX_HITS = 10;

    private final List<String> payload = new ArrayList<String>();
    private final String id;

    public Ball(String id) {
        this.id = id;
    }

    public Ball hit(String msg) {
        Ball newBall = new Ball(this.id);
        newBall.payload.addAll(this.payload);
        newBall.payload.add(msg);
        return newBall; 
    }

    public boolean isMaxedOut() {
        boolean result = (getNumHits() >= MAX_HITS);
        return result;
    }

    public int getNumHits() {
        return payload.size();
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("id: " + id + "\n");
        buffer.append("payload: \n");

        for (String s : payload) {
            buffer.append(s + "\n");
        }

        return buffer.toString();
    }
}
