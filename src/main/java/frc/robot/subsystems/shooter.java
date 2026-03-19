package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;

import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class shooter extends SubsystemBase {
    // two motors
    private TalonFX L, R;
    private InterpolatingDoubleTreeMap lut;

    // TODO: add more points
    private void addPoints() {
        // key -> distance (meters), value -> RPM
        lut.put(1.0, 1000.0);
    }

    public shooter(int Left_Motor, int Right_Motor, boolean isInverted) {
        L = new TalonFX(Left_Motor);
        R = new TalonFX(Right_Motor);
        lut = new InterpolatingDoubleTreeMap();
        addPoints();
        motorConfig(isInverted);
    }

    private void motorConfig(boolean invert) {
        var config = new TalonFXConfiguration();

        // tuneables
        var slot0 = config.Slot0;

        slot0.kP = Constants.KPofSHOOTER;

        var feedback = config.Feedback;

        feedback.RotorToSensorRatio = 36 / 30;

        var output = config.MotorOutput;

        output.Inverted = invert ? InvertedValue.CounterClockwise_Positive : InvertedValue.Clockwise_Positive;

        L.getConfigurator().apply(config);
        R.getConfigurator().apply(config);

    }

    private VelocityVoltage setter = new VelocityVoltage(0);

    // set rpm to both motors
    private void setRPM(double rpm) {
        setter.withVelocity(rpm);
        L.setControl(setter);
        R.setControl(setter);
    }

    public Command SETRPM(double RPM) {
        return Commands.runOnce(() -> setRPM(RPM), this);
    }

    // supply with distance from vision in order to get correct RPM
    public Command SHOOT(DoubleSupplier distance) {
        return Commands.runOnce(() -> setRPM(lut.get(distance.getAsDouble())), this);
    }

}
