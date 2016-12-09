/*
 * Decompiled with CFR 0_118.
 */
package uk.ac.warwick.dcs.maze.tutorial;

import java.awt.Point;
import uk.ac.warwick.dcs.maze.logic.IRobot;
import uk.ac.warwick.dcs.maze.logic.IRobotController;

public abstract class SimpleController
implements IRobotController {
    protected IRobot robot;
    private boolean active = false;
    private int delay;

    protected abstract void controlRobot();

    @Override
    public void start() {
        this.active = true;
        while (!this.robot.getLocation().equals(this.robot.getTargetLocation()) && this.active) {
            this.controlRobot();
            if (this.delay <= 0) continue;
            this.robot.sleep(this.delay);
        }
    }

    @Override
    public void setDelay(int millis) {
        this.delay = millis;
    }

    @Override
    public int getDelay() {
        return this.delay;
    }

    @Override
    public void reset() {
        this.active = false;
    }

    @Override
    public void setRobot(IRobot robot) {
        this.robot = robot;
    }

    @Override
    public String getDescription() {
        return "SimpleController Implementation";
    }
}

