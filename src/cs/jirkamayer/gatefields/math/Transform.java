package cs.jirkamayer.gatefields.math;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents position and rotation, transforms can be nested
 */
public class Transform {
    private Vector2D position = Vector2D.ZERO;
    private float rotation = 0f;

    private Transform parent = null;
    private List<Transform> children;

    // cache
    private Vector2D globalPosition = Vector2D.ZERO;
    private float globalRotation = 0f;

    public Transform() {
        this.children = new ArrayList<>();
    }

    public Transform(Vector2D pos, float rot, Transform par) {
        this();
        this.setParent(par);
        this.setLocalPosition(pos);
        this.setLocalRotation(rot);
    }

    public void setParent(Transform p) {
        // unregister as a child
        if (parent != null) {
            parent.children.remove(this);
        }

        // swap parent
        parent = p;

        // register as a child
        if (parent != null) {
            parent.children.add(this);
        }

        this.rebuildCache();
    }

    public Vector2D getPosition() {
        return globalPosition;
    }

    public Vector2D getLocalPosition() {
        return position;
    }

    public void setLocalPosition(Vector2D position) {
        this.position = position;
        this.rebuildCache();
    }

    public void setPosition(Vector2D p) {
        if (parent == null) {
            position = p;
        } else {
            position = parent.transformGlobalToLocal(p);
        }
        this.rebuildCache();
    }

    public void setLocalRotation(float r) {
        rotation = r;
        this.rebuildCache();
    }

    public void setLocalRotation(double r) {
        rotation = (float)r;
        this.rebuildCache();
    }

    public float getLocalRotation() {
        return rotation;
    }

    public float getRotation() {
        return globalRotation;
    }

    /**
     * Recalculates the world properties cache if some local property changes
     */
    public void rebuildCache() {
        if (parent == null) {
            globalPosition = position;
            globalRotation = rotation;
        } else {
            globalPosition = parent.transformLocalToGlobal(position);
            globalRotation = parent.transformLocalToGlobal(rotation);
        }

        // propagate
        for (Transform c : children)
            c.rebuildCache();
    }

    public Vector2D transformLocalToGlobal(Vector2D v) {
        v = position.plus(v.rotate(rotation));

        if (parent != null)
            v = parent.transformLocalToGlobal(v);

        return v;
    }

    public Vector2D transformGlobalToLocal(Vector2D v) {
        if (parent != null)
            v = parent.transformGlobalToLocal(v);

        return v.minus(position).rotate(-rotation);
    }

    public float transformLocalToGlobal(float angle) {
        angle += rotation;

        if (parent != null)
            angle = parent.transformLocalToGlobal(angle);

        return angle;
    }
}
